// utils/downloadHandler.js

import protectedApiClient from '../protectedApiClient';

/**
 * Gestisce il download di file e cartelle.
 * @param {number} id - L'ID del file o della cartella.
 * @param {string} type - Il tipo di elemento da scaricare ('file' o 'folder').
 */
export async function downloadItem(id, type) {
    if (!id || !type) {
        console.error('ID e tipo di elemento sono obbligatori per il download.');
        return;
    }

    if (type !== 'file' && type !== 'folder') {
        console.error('Tipo di elemento non valido. Deve essere "file" o "folder".');
        return;
    }

    const userId = localStorage.getItem('id');
    const spaceId = localStorage.getItem('selectedSpaceId');

    if (!userId || !spaceId) {
        console.error('ID utente e ID spazio sono obbligatori.');
        return;
    }

    const endpoint = '/storage/image';
    const params = {
        userId: userId,
        spaceId: spaceId,
        download: true
    };

    if (type === 'file') {
        params.fileId = id;
    } else if (type === 'folder') {
        params.folderId = id;
    }

    try {
        const response = await protectedApiClient.get(endpoint, { params, responseType: 'blob' });

        if (response.status === 200) {
            const blob = response.data;
            const contentDisposition = response.headers['content-disposition'] ||
            response.headers['Content-Disposition'] ||
            response.headers['Content-Disposition'.toLowerCase()];

            console.log("la contentDescription è ---> "+contentDisposition);
            const filename = contentDisposition ? contentDisposition.split('filename=')[1] : `${type}_${id}.zip`;
console.log("file name ottenuto---> "+ filename);

            const url = URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = filename;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        } else if (response.status === 400) {
            console.error('Errore nella richiesta di download: la richiesta non è andata a buon fine.');
        } else {
            console.error('Errore sconosciuto durante il download.');
        }
    } catch (error) {
        console.error('Errore durante il download:', error);
    }
}