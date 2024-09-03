<template>
  <div class="folders">
    <BaseMainBlock actionType="folder" @add-new-folder="createNewFolder">
      <template #header>
        <div>
        <h1>{{ whereWeAre }}</h1>
        <h2 v-if="currentFolderName">{{ currentFolderName }}</h2>
      </div>
        <div>
          <button class="grey-button" @click="goHome">Home</button>
          <button class="grey-button" @click="goBack" :disabled="!folderStack.length">Back</button>
        </div>
        <div>
          <button class="grey-button">Upload</button>
          <button class="grey-button">Download</button>
        </div>
        </template>
      <template #default>
        <ul>
          <li v-for="folder in folders" :key="folder.id" @click="selectFolder(folder.id, folder.name)">
            📁 {{ folder.name }}
          </li>
          <li v-for="file in files" :key="file.id" @click="selectFile(file.id)">
            📄 {{ file.name }} ({{ formatFileSize(file.size) }})
          </li>
        </ul>
      </template>
    </BaseMainBlock>
  </div>
</template>

<script>
import BaseMainBlock from '@/components/BaseMainBlock.vue';
import protectedApiClient from '@/protectedApiClient'; 

export default {
  components: {
    BaseMainBlock
  },
  data() {
    return {
      folders: [],
      files: [],
      spaceId: null,
      userId: null,
      whereWeAre: 'spazi',
      folderStack: [], // Array to keep track of navigation history (ID and name)
      currentFolderName: '' // To keep track of the current folder's name
    };
  },
  async mounted() {
    this.spaceId = localStorage.getItem('selectedSpaceId');
    this.userId = localStorage.getItem('id');
    this.whereWeAre = localStorage.getItem('selectedSpaceName');
  
    if (this.spaceId && this.userId) {
      await this.fetchFoldersAndFiles();
    }
  },
  methods: {
    async fetchFoldersAndFiles(folderId = null) {
      try {
        const endpoint = folderId ? '/storage/folder' : '/storage/spaces';
        const params = folderId
          ? { userId: this.userId, folderId: folderId }
          : { userId: this.userId, spaceId: this.spaceId };

        const response = await protectedApiClient.get(endpoint, { params });
        
        this.folders = response.data.folders || [];
        this.files = response.data.files || [];

        if (folderId !== null) {
          // Solo se non siamo alla root, aggiungiamo l'ID e il nome della cartella corrente
          if (!this.folderStack.length || this.folderStack[this.folderStack.length - 1].id !== folderId) {
            const currentFolder = this.folders.find(folder => folder.id === folderId);
            if (currentFolder) {
              this.currentFolderName = currentFolder.name;
            }
            this.folderStack.push({ id: folderId, name: this.currentFolderName });
          }
        } else {
          // Se siamo alla root, svuotiamo lo stack e resettiamo il nome della cartella
          this.folderStack = [];
          this.currentFolderName = '';
        }
      } catch (error) {
        console.error('Errore nel recupero di cartelle e file:', error);
      }
    },
    async createNewFolder() {
      const folderName = prompt("Inserisci il nome della nuova cartella:");
      if (!folderName || !this.spaceId || !this.userId) {
        return;
      }
  
      const parentId = this.folderStack.length ? this.folderStack[this.folderStack.length - 1].id : null;
  
      try {
        const response = await protectedApiClient.post('/storage/newfolder', {
          userId: parseInt(this.userId),
          spaceId: parseInt(this.spaceId),
          parentId: parentId,
          name: folderName
        });
  
        if (response.status === 200) {
          alert(`Cartella "${response.data.name}" creata con successo!`);
          await this.fetchFoldersAndFiles(parentId);  // Ricarica la lista delle cartelle e file
        }
      } catch (error) {
        console.error('Errore nella creazione della cartella:', error);
      }
    },
    selectFolder(folderId, folderName) {
      this.currentFolderName = folderName;
      this.fetchFoldersAndFiles(folderId);
    },
    selectFile(fileId) {
      localStorage.setItem('selectedFileId', fileId);
      console.log(`File ID ${fileId} selezionato.`);
      // Puoi aprire il file o eseguire altre azioni
    },
    async goHome() {
      this.folderStack = [];
      this.currentFolderName = '';
      await this.fetchFoldersAndFiles();  // Vai alla radice dello spazio
    },
    async goBack() {
      if (this.folderStack.length > 0) {
        // Ottieni l'ultimo folderId e nome della cartella
        const currentFolder = this.folderStack.pop(); 

        // Ottieni il folderId e nome precedente
        const previousFolder = this.folderStack.length ? this.folderStack[this.folderStack.length - 1] : null;

        if (previousFolder) {
          this.currentFolderName = previousFolder.name;
        } else {
          this.currentFolderName = '';
        }

        try {
          await this.fetchFoldersAndFiles(previousFolder ? previousFolder.id : null);
        } catch (error) {
          console.error('Errore nel caricamento della cartella precedente:', error);
        }
      }
    },
    formatFileSize(size) {
      const i = Math.floor(Math.log(size) / Math.log(1024));
      return (size / Math.pow(1024, i)).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
    }
  }
};
</script>

  <style scoped>
  .folders ul {
    list-style-type: none;
    padding: 0;
  }
  
  .folders li {
    cursor: pointer;
  color: var(--menu-hover-color);
  padding: 10px;
  border-bottom: 1px solid var(--menu-primary-color);
  }
  
  .folders li:hover {
    background-color: var(--menu-hover-color);
    color:var(--menu-font-color);
   }
  </style>
  