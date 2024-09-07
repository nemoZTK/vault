// renameFolderAndFiles.js
import protectedApiClient from '@/protectedApiClient';

function getFileExtension(fileName) {
    const parts = fileName.split('.');
    return parts.length > 1 ? parts.pop() : '';
}

function ensureExtension(fileName, extension) {
    return fileName.endsWith(`.${extension}`) ? fileName : `${fileName}.${extension}`;
}
export async function renameFile(userId, fileId, oldName, newName) {
    try {
        console.log("Sto per rinominare il file:");
        console.log("oldName: ", oldName);
        console.log("newName: ", newName);

        const oldExtension = getFileExtension(oldName);
        const newNameWithExtension = ensureExtension(newName, oldExtension);

        console.log("newNameWithExtension: ", newNameWithExtension);

        const response = await protectedApiClient.put(
            '/storage/rename/file',
            {
                userId: Number(userId),
                fileId: Number(fileId),
                newName: newNameWithExtension
            }
        );
        return response.status === 200;
    } catch (error) {
        console.error('Error renaming file:', error);
        return false;
    }
}

export async function renameFolder(userId, folderId, newName) {
    try {
        console.log("sto per mandare user " + userId + " con nuovo nome: " + newName + " e folderId: " + folderId);

        const newNameLowerCase = newName.toLowerCase();

        // Convertiamo userId e folderId in numeri
        const response = await protectedApiClient.put(
            '/storage/rename/folder',
            {
                userId: Number(userId),
                folderId: Number(folderId),
                newName: newNameLowerCase
            }
        );
        return response.status === 200;
    } catch (error) {
        console.error('Error renaming folder:', error);
        return false;
    }
}
