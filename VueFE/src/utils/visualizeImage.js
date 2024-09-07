import protectedApiClient from '@/protectedApiClient';

export default {
    async openImage(fileId, userId, spaceId) {
        try {
            const response = await protectedApiClient.get('storage/image', {
                params: {
                    userId: userId,
                    spaceId: spaceId,
                    fileId: fileId,
                    download: false
                },
                responseType: 'blob'
            });

            this.selectedFileUrl = URL.createObjectURL(response.data);
            this.isModalOpen = true;
        } catch (error) {
            console.error('Errore nel recupero dell\'immagine:', error);
        }
    },
    closeModal() {
        this.isModalOpen = false;
        if (this.selectedFileUrl) {
            URL.revokeObjectURL(this.selectedFileUrl);
            this.selectedFileUrl = null;
        }
    }
};
