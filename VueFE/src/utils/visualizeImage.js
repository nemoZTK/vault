// visualizeImage.js
import protectedApiClient from '../protectedApiClient'; // Importa il modulo API

export default {
    data() {
        return {
            selectedFileUrl: null,
            isModalOpen: false
        };
    },
    methods: {
        async openImage(fileId, userId, spaceId) {
            try {
                // Richiesta API per ottenere l'URL dell'immagine
                const response = await protectedApiClient.get('storage/image', {
                    params: {
                        userId: userId,
                        spaceId: spaceId,
                        fileId: fileId,
                        download: false
                    },
                    responseType: 'blob' // Assumi che l'API restituisca un blob
                });

                // Crea un URL oggetto per l'immagine
                this.selectedFileUrl = URL.createObjectURL(response.data);
                this.isModalOpen = true;
            } catch (error) {
                console.error('Errore nel recupero dell\'immagine:', error);
            }
        },
        closeModal() {
            this.isModalOpen = false;
            this.selectedFileUrl = null;

            // Revoca l'URL oggetto per liberare memoria
            if (this.selectedFileUrl) {
                URL.revokeObjectURL(this.selectedFileUrl);
            }
        }
    }
};
