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
        const response = await protectedApiClient.get(endpoint, { params, responseType: 'blob', withCredentials: true });

        if (response.status === 200) {
            console.log(response.headers);
            const contentDisposition = response.headers['content-disposition'];

            console.log("La contentDisposition è ---> " + contentDisposition);
            let filename = 'downloaded_file';

            // Estrai il nome del file dalla content-disposition
            if (contentDisposition) {
                const filenameMatch = contentDisposition.match(/filename="(.+)"/);
                if (filenameMatch.length === 2) {
                    filename = filenameMatch[1];
                }
            }

            console.log("File name ottenuto ---> " + filename);

            // Crea un URL per il blob e scarica il file
            const url = URL.createObjectURL(response.data);
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
