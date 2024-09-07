import protectedApiClient from '@/protectedApiClient';

/**
 * Cancella uno o più file.
 * @param {Array} fileIds - Lista degli ID dei file da cancellare.
 * @param {String} userId - ID dell'utente.
 * @returns {Promise} - Risultato della richiesta API.
 */
export async function deleteFile(fileIds, userId) {
  try {
    console.log("ricevuto delete files " + fileIds + " per user " + userId);

    // Passa i dati come query string
    const response = await protectedApiClient.delete('/storage/delete/file', {
        params: {
          fileIds: fileIds.join(','),  // Unisci gli ID con una virgola
          userId: userId
        }
    });
    return response.data;
  } catch (error) {
    console.error('Errore durante la cancellazione del file:', error);
    throw error;
  }
}

/**
 * Cancella una cartella.
 * @param {String} folderId - ID della cartella da cancellare.
 * @param {String} userId - ID dell'utente.
 * @returns {Promise} - Risultato della richiesta API.
 */
export async function deleteFolder(folderId, userId) {
  console.log("ricevuto delete folder " + folderId + " per user " + userId);
  try {
    // Passa i dati come query string
    const response = await protectedApiClient.delete('/storage/delete/folder', {
      params: {
        folderId: folderId,
        userId: userId
      }
    });
    return response.data;
  } catch (error) {
    console.error('Errore durante la cancellazione della cartella:', error);
    throw error;
  }
}
