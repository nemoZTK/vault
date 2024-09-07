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
          <li v-for="folder in folders" :key="'folder-' + folder.id" class="folder-item"
            @click="selectFolder(folder.id, folder.name)">
            <button class="grey-button download-button" @click.stop="downloadItem(folder.id, 'folder')">Download</button>
            <span>📁 {{ folder.name }}</span>
            <button class="grey-button rename-button" @click.stop="rename('folder', folder.id, folder.name)">Rename</button>
          </li>
          <li v-for="file in files" :key="'file-' + file.id" class="file-item" @click="selectFile(file.id)">
            <span>📄 {{ file.name }} ({{ formatFileSize(file.size) }})</span>
            <button class="grey-button download-button" @click.stop="downloadItem(file.id, 'file')">Download</button>
            <button class="grey-button rename-button" @click.stop="rename('file', file.id, file.name)">Rename</button>
          </li>
        </ul>
      </template>
    </BaseMainBlock>

    <InputForm v-if="isInputFormVisible" @submit="handleRename" @cancel="isInputFormVisible = false" />

    <div v-if="isModalOpen" class="modal" @click.self="closeModal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <img :src="selectedFileUrl" alt="Immagine selezionata" style="max-width: 70%; height: auto;" />
      </div>
    </div>
  </div>
</template>



<script>
import BaseMainBlock from '@/components/BaseMainBlock.vue';
import InputForm from '../components/InsertNameForm.vue';
import { uploadFile } from '@/utils/uploadHandler';
import { createNewFolder } from '@/utils/folderGenerator';
import { downloadItem } from '@/utils/downloadHandler';
import { fetchFoldersAndFiles, goHome, goBack, formatFileSize } from '../utils/spaceContentHandler';
import visualizeImage from '@/utils/visualizeImage';
import { renameFile, renameFolder } from '@/utils/renameFolderAndFiles'; // Import the rename functions

export default {
  components: {
    BaseMainBlock,
    InputForm
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
      selectedFile: null,
      isModalOpen: false,
      selectedFileUrl: '',
      isInputFormVisible: false,
      itemToRename: null,
      itemType: null,
    };
  },
  async mounted() {
    this.spaceId = localStorage.getItem('selectedSpaceId');
    this.userId = localStorage.getItem('id');
    this.whereWeAre = localStorage.getItem('selectedSpaceName');

    if (this.spaceId && this.userId) {
      await fetchFoldersAndFiles(this);
    }
  },
  methods: {
    async fetchFoldersAndFiles(folderId = null) {
      await fetchFoldersAndFiles(this, folderId);
    },
    createNewFolder(folderName) {
      createNewFolder(this.userId, this.spaceId, this.folderStack, this.fetchFoldersAndFiles, folderName);
    },
    selectFolder(folderId, folderName) {
      this.currentFolderName = folderName;
      this.fetchFoldersAndFiles(folderId);
    },
    async selectFile(fileId) {
      localStorage.setItem('selectedFileId', fileId);
      console.log(`File ID ${fileId} selezionato.`);
      await visualizeImage.openImage(fileId, this.userId, this.spaceId);
      this.selectedFileUrl = visualizeImage.selectedFileUrl;
      this.isModalOpen = true;
    },
    async goHome() {
      await goHome(this);
    },
    async goBack() {
      await goBack(this);
    },
    formatFileSize(size) {
      return formatFileSize(size);
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
      downloadItem(id, type);
    },
    closeModal() {
      this.isModalOpen = false;
      if (this.selectedFileUrl) {
        URL.revokeObjectURL(this.selectedFileUrl);
        this.selectedFileUrl = null;
      }
    },
    rename(type, id, currentName) {
      this.itemToRename = { id, currentName };
      this.itemType = type;
      this.isInputFormVisible = true; 
    },
    async handleRename(newName) {
      let success;
      if (this.itemType === 'file') {
        success = await renameFile(this.userId, this.itemToRename.id, newName);
      } else if (this.itemType === 'folder') {
        success = await renameFolder(this.userId, this.itemToRename.id, newName);
      }

      if (success) {
        await this.fetchFoldersAndFiles(); // Refresh the list
      } else {
        console.error('Failed to rename');
      }
      this.isInputFormVisible = false; // Hide input form
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
  color: var(--menu-font-color);
}

.folder-item,
.file-item {
  position: relative;
  padding: 5px;
}

.download-button, .rename-button {
  display: none;
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}

.rename-button{
  margin-right: 9rem;
}


.folder-item:hover .download-button,
.folder-item:hover .rename-button,
.file-item:hover .rename-button,
.file-item:hover .download-button {
  display: inline-block;
}
</style>