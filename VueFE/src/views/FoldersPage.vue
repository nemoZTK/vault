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
          <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" />
          <button class="grey-button" @click="triggerFileInput">Upload</button>
        </div>
      </template>
      <template #default>
        <ul>
          <li v-for="folder in folders" :key="'folder-' + folder.id" class="folder-item" @click="selectFolder(folder.id, folder.name)">
            <button class="grey-button download-button" @click.stop="downloadItem(folder.id, 'folder')">Download</button>
          <span>📁 {{ folder.name }}</span>
          </li>
          <li v-for="file in files" :key="'file-' + file.id" class="file-item" @click="selectFile(file.id)">
            <span>📄 {{ file.name }} ({{ formatFileSize(file.size) }})</span>
            <button class="grey-button download-button" @click.stop="downloadItem(file.id, 'file')">Download</button>
          </li>
        </ul>
      </template>
    </BaseMainBlock>
  </div>
</template>


<script>
import BaseMainBlock from '@/components/BaseMainBlock.vue';
import { uploadFile } from '@/utils/uploadHandler';
import { createNewFolder } from '@/utils/folderGenerator';
import { downloadItem } from '@/utils/downloadHandler'; // Importa la funzione downloadItem
import protectedApiClient from '../protectedApiClient';

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
      folderStack: [],
      currentFolderName: '',
      selectedFile: null
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
          if (!this.folderStack.length || this.folderStack[this.folderStack.length - 1].id !== folderId) {
            const currentFolder = this.folders.find(folder => folder.id === folderId);
            if (currentFolder) {
              this.currentFolderName = currentFolder.name;
            }
            this.folderStack.push({ id: folderId, name: this.currentFolderName });
          }
        } else {
          this.folderStack = [];
          this.currentFolderName = '';
        }
      } catch (error) {
        console.error('Errore nel recupero di cartelle e file:', error);
      }
    },
    createNewFolder() {
      createNewFolder(this.userId, this.spaceId, this.folderStack, this.fetchFoldersAndFiles);
    },
    selectFolder(folderId, folderName) {
      this.currentFolderName = folderName;
      this.fetchFoldersAndFiles(folderId);
    },
    selectFile(fileId) {
      localStorage.setItem('selectedFileId', fileId);
      console.log(`File ID ${fileId} selezionato.`);
    },
    async goHome() {
      this.folderStack = [];
      this.currentFolderName = '';
      await this.fetchFoldersAndFiles();
    },
    async goBack() {
      if (this.folderStack.length > 0) {
        const currentFolder = this.folderStack.pop(); 
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
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.selectedFile = file;
        this.uploadFile(); 
      }
    },
    async uploadFile() {
      await uploadFile(this.selectedFile, this.userId, this.spaceId, this.folderStack, this.fetchFoldersAndFiles);
    },
    downloadItem(id, type) {
      downloadItem(id, type); // Usa la funzione di download dal modulo separato
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

   .folder-item, .file-item {
  position: relative;
  padding: 5px;
}

.download-button {
  display: none;
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}

.folder-item:hover .download-button, 
.file-item:hover .download-button {
  display: inline-block;
}

  </style>