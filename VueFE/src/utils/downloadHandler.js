// downloadHandler.js


import protectedApiClient from '@/protectedApiClient';

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
        const response = await protectedApiClient.get(endpoint, { params, responseType: 'blob', withCredentials: true });

        if (response.status === 200) {
            const contentDisposition = response.headers['content-disposition'];
            let filename = 'downloaded_file';

            if (contentDisposition) {
                const filenameMatch = contentDisposition.match(/filename="(.+)"/);
                if (filenameMatch.length === 2) {
                    filename = filenameMatch[1];
                }
            }

            const url = URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.download = filename;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            URL.revokeObjectURL(url);
        } else {
            console.error('Errore sconosciuto durante il download.');
        }
    } catch (error) {
        console.error('Errore durante il download:', error);
    }
}
