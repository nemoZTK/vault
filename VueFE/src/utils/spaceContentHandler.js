// /utils/spaceContentHandler.js
import protectedApiClient from '../protectedApiClient';

/**
 * Funzione per ottenere le cartelle e i file.
 * @param {string} userId - ID dell'utente.
 * @param {string} spaceId - ID dello spazio.
 * @param {string|null} folderId - ID della cartella. Se null, recupera le cartelle principali.
 * @param {Function} setFolders - Funzione per aggiornare lo stato delle cartelle.
 * @param {Function} setFiles - Funzione per aggiornare lo stato dei file.
 * @param {Function} setCurrentFolderName - Funzione per aggiornare il nome della cartella corrente.
 * @param {Function} setFolderStack - Funzione per aggiornare lo stack delle cartelle.
 */
export async function fetchFoldersAndFiles(userId, spaceId, folderId, setFolders, setFiles, setCurrentFolderName, setFolderStack) {
  try {
    const endpoint = folderId ? '/storage/folder' : '/storage/spaces';
    const params = folderId
      ? { userId, folderId }
      : { userId, spaceId };

    const response = await protectedApiClient.get(endpoint, { params });
    
    setFolders(response.data.folders || []);
    setFiles(response.data.files || []);

    if (folderId !== null) {
      if (!setFolderStack.length || setFolderStack[setFolderStack.length - 1].id !== folderId) {
        const currentFolder = response.data.folders.find(folder => folder.id === folderId);
        if (currentFolder) {
          setCurrentFolderName(currentFolder.name);
        }
        setFolderStack([...setFolderStack, { id: folderId, name: currentFolder.name }]);
      }
    } else {
      setFolderStack([]);
      setCurrentFolderName('');
    }
  } catch (error) {
    console.error('Errore nel recupero di cartelle e file:', error);
  }
}

/**
 * Funzione per formattare la dimensione dei file.
 * @param {number} size - Dimensione del file in byte.
 * @returns {string} - Dimensione formattata.
 */
export function formatFileSize(size) {
  const i = Math.floor(Math.log(size) / Math.log(1024));
  return (size / Math.pow(1024, i)).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
}