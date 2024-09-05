import protectedApiClient from '../protectedApiClient';

export async function createNewFolder(userId, spaceId, folderStack, fetchFoldersAndFiles, folderName) {
  // Controlla se il nome della cartella è stato fornito
  if (!folderName || !spaceId || !userId) {
    console.log("qalcosa non c'era " + folderName + " space id" + spaceId + " usr " + userId);

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
      console.log("created new folder " + folderName);

      await fetchFoldersAndFiles(parentId);  // Ricarica la lista delle cartelle e file
    }
  } catch (error) {
    console.error('Errore nella creazione della cartella:', error);
  }
}