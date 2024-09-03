import protectedApiClient from '../protectedApiClient'; 

export async function createNewFolder(userId, spaceId, folderStack, fetchFoldersAndFiles) {
  const folderName = prompt("Inserisci il nome della nuova cartella:");
  if (!folderName || !spaceId || !userId) {
    return;
  }

  const parentId = folderStack.length ? folderStack[folderStack.length - 1].id : null;

  try {
    const response = await protectedApiClient.post('/storage/newfolder', {
      userId: parseInt(userId),
      spaceId: parseInt(spaceId),
      parentId: parentId,
      name: folderName
    });

    if (response.status === 200) {
      alert(`Cartella "${response.data.name}" creata con successo!`);
      await fetchFoldersAndFiles(parentId);  // Ricarica la lista delle cartelle e file
    }
  } catch (error) {
    console.error('Errore nella creazione della cartella:', error);
  }
}
