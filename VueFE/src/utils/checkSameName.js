// checkSameName.js

export function checkSameFolderName(newName, existingFolders) {
    return existingFolders.some(folder => folder.name.toLowerCase() === newName.toLowerCase());
}

export function checkSameFileName(newName, existingFiles) {
    return existingFiles.some(file => file.name === newName);
}
