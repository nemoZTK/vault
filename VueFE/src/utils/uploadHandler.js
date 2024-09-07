import protectedApiClient from '@/protectedApiClient';

export async function uploadFile(selectedFile, userId, spaceId, folderStack, fetchFoldersAndFiles) {
  if (!selectedFile) {
    alert('Nessun file selezionato!');
    return;
  }

  const formData = new FormData();
  formData.append('file', selectedFile);
  formData.append('userId', userId);
  formData.append('spaceId', spaceId);
  if (folderStack.length > 0) {
    formData.append('parentId', folderStack[folderStack.length - 1].id);
  }

  try {
    const response = await protectedApiClient.post('/storage/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });

    if (response.status === 200) {
      const responseData = response.data;
      if (responseData.result === 'done') {

        await fetchFoldersAndFiles(folderStack.length ? folderStack[folderStack.length - 1].id : null);
      } else {
      }
    } else {
    }
  } catch (error) {
    console.error('Errore durante il caricamento del file:', error);

  }
}