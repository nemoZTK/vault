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
        console.log("sto per mandare user " + userId + newName + fileId);

        const oldExtension = getFileExtension(oldName);
        const newNameWithExtension = ensureExtension(newName, oldExtension);

        const response = await protectedApiClient.put(
            'http://localhost:8080/api/storage/rename/file',
            {
                userId,
                fileId,
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
        console.log("sto per mandare user " + userId + newName + folderId);

        const newNameLowerCase = newName.toLowerCase();

        const response = await protectedApiClient.put(
            'http://localhost:8080/api/storage/rename/folder',
            {
                userId,
                folderId,
                newName: newNameLowerCase
            }
        );
        return response.status === 200;
    } catch (error) {
        console.error('Error renaming folder:', error);
        return false;
    }
}
