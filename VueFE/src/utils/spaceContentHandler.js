import protectedApiClient from '../protectedApiClient';


// recupera cartelle e file 
export async function fetchFoldersAndFiles(vm, folderId = null) {
  try {
    const endpoint = folderId ? '/storage/folder' : '/storage/spaces';
    const params = folderId
      ? { userId: vm.userId, folderId: folderId }
      : { userId: vm.userId, spaceId: vm.spaceId };

    const response = await protectedApiClient.get(endpoint, { params });

    vm.folders = response.data.folders || [];
    vm.files = response.data.files || [];

    if (folderId !== null) {
      if (!vm.folderStack.length || vm.folderStack[vm.folderStack.length - 1].id !== folderId) {
        const currentFolder = vm.folders.find(folder => folder.id === folderId);
        if (currentFolder) {
          vm.currentFolderName = currentFolder.name;
        }
        vm.folderStack.push({ id: folderId, name: vm.currentFolderName });
      }
    } else {
      vm.folderStack = [];
      vm.currentFolderName = '';
    }
  } catch (error) {
    console.error('Errore nel recupero di cartelle e file:', error);
  }
}

//  bottone "Home"
export async function goHome(vm) {
  vm.folderStack = [];
  vm.currentFolderName = '';
  await fetchFoldersAndFiles(vm);
}

// bottone "Back"
export async function goBack(vm) {
  if (vm.folderStack.length > 0) {
    const currentFolder = vm.folderStack.pop();
    const previousFolder = vm.folderStack.length ? vm.folderStack[vm.folderStack.length - 1] : null;

    if (previousFolder) {
      vm.currentFolderName = previousFolder.name;

    } else {
      vm.currentFolderName = '';
    }

    try {
      await fetchFoldersAndFiles(vm, previousFolder ? previousFolder.id : null);
    } catch (error) {
      console.error('Errore nel caricamento della cartella precedente:', error);
    }
  }
}

// formatta la dimensione dei file
export function formatFileSize(size) {
  const i = Math.floor(Math.log(size) / Math.log(1024));
  return (size / Math.pow(1024, i)).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
}
