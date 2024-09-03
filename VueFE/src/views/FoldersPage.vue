<template>
    <div class="folders">
      <BaseMainBlock actionType="folder" @add-new-folder="createNewFolder">
        <template #header>
            <h1>{{ whereWeAre }}</h1>
            <div>
            <button class="grey-button"> download</button>
            <button class="grey-button"> upload</button>
            </div>
        </template>
        <template #default>
          <ul>
            <li v-for="folder in folders" :key="folder.id" @click="selectFolder(folder.id)">
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
        whereWeAre: 'spazi'
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
      async fetchFoldersAndFiles() {
        try {
          const response = await protectedApiClient.get(`/storage/spaces`, {
            params: {
              userId: this.userId,
              spaceId: this.spaceId
            }
          });
          this.folders = response.data.folders || [];
          this.files = response.data.files || [];
        } catch (error) {
          console.error('Errore nel recupero di cartelle e file:', error);
        }
      },
      async createNewFolder() {
        const folderName = prompt("Inserisci il nome della nuova cartella:");
        if (!folderName || !this.spaceId || !this.userId) {
          return;
        }
  
        const parentId = null; // Imposta parentId a null o altro valore se necessario
  
        try {
          const response = await protectedApiClient.post('/storage/newfolder', {
            userId: parseInt(this.userId),
            spaceId: parseInt(this.spaceId),
            parentId: parentId,
            name: folderName
          });
  
          if (response.status === 200) {
            alert(`Cartella "${response.data.name}" creata con successo!`);
            await this.fetchFoldersAndFiles(); // Ricarica la lista delle cartelle e file
          }
        } catch (error) {
          console.error('Errore nella creazione della cartella:', error);
        }
      },
      selectFolder(folderId) {
        localStorage.setItem('selectedFolderId', folderId);
        console.log(`Folder ID ${folderId} selezionato.`);
        // Naviga alla vista della cartella o esegui altre azioni
      },
      selectFile(fileId) {
        localStorage.setItem('selectedFileId', fileId);
        console.log(`File ID ${fileId} selezionato.`);
        // Puoi aprire il file o eseguire altre azioni
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
    padding: 10px;
    border-bottom: 1px solid #ccc;
  }
  
  .folders li:hover {
    background-color: #f0f0f0;
  }
  </style>
  