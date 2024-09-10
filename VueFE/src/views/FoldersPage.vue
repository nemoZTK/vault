<template>
  <div class="folders">
    <BaseMainBlock actionType="folder" @add-new-folder="createNewFolder">
      <template #header>
        <div>
          <h1 id="space-name">{{ whereWeAre }}</h1>
          <h2 id="actual-folder" v-if="currentFolderName">{{ currentFolderName }}</h2>
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
            <button 
              class="grey-button download-button" 
              :data-folder-name="folder.name" 
              @click.stop="downloadItem(folder.id, 'folder')">
              Download
            </button>
            <span>📁 {{ folder.name }}</span>
            <button 
              class="grey-button rename-button" 
              :data-folder-name="folder.name" 
              @click.stop="rename('folder', folder.id, folder.name)">
              Rename
            </button>
            <button 
              class="grey-button delete-button" 
              :data-folder-name="folder.name" 
              @click.stop="deleteItem('folder', folder.id)">
              Delete
            </button>
          </li>
          <li v-for="file in files" :key="'file-' + file.id" class="file-item" @click="selectFile(file.id)">
            <span>📄 {{ file.name }} ({{ formatFileSize(file.size) }})</span>
            <button 
              class="grey-button download-button" 
              :data-file-name="file.name" 
              @click.stop="downloadItem(file.id, 'file')">
              Download
            </button>
            <button 
              class="grey-button rename-button" 
              :data-file-name="file.name" 
              @click.stop="rename('file', file.id, file.name)">
              Rename
            </button>
            <button 
              class="grey-button delete-button" 
              :data-file-name="file.name" 
              @click.stop="deleteItem('file', file.id)">
              Delete
            </button>
          </li>
        </ul>
      </template>
    </BaseMainBlock>
    
    <InputForm v-if="isInputFormVisible" @submit="handleRename" @close="isInputFormVisible = false" />

  <div v-if="isModalOpen" class="modal" @click.self="closeModal">
    <div class="modal-content">
      <span class="close" @click="closeModal">&times;</span>
      <embed :src="selectedFileUrl" style="width: 100%; height: 500px;" />
    </div>
  </div>

    
    <ConfirmForm
      v-if="isConfirmFormVisible"
      :title="'Confirm Delete'"
      :itemType="itemType"
      @submit="handleDelete"
      @close="isConfirmFormVisible = false"
    />
  </div>
</template>


<script>
import BaseMainBlock from '@/components/BaseMainBlock.vue';
import InputForm from '../components/InsertNameForm.vue';
import ConfirmForm from '../components/ConfirmDeleteForm.vue';
import { uploadFile } from '@/utils/uploadHandler';
import { createNewFolder } from '@/utils/folderGenerator';
import { downloadItem } from '@/utils/downloadHandler';
import { fetchFoldersAndFiles, goHome, goBack, formatFileSize } from '../utils/spaceContentHandler';
import visualizeImage from '@/utils/visualizeImage';
import { renameFile, renameFolder } from '@/utils/renameFolderAndFiles'; 
import { deleteFile, deleteFolder } from '@/utils/holdDeleteFileAndFolders';

export default {
  components: {
    BaseMainBlock,
    InputForm,
    ConfirmForm
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
      isConfirmFormVisible: false,
      itemsToDelete: null,
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
      const currentFolderId = this.folderStack.length ? this.folderStack[this.folderStack.length - 1].id : null;

      if (this.itemType === 'file') {
        success = await renameFile(this.userId, this.itemToRename.id, this.itemToRename.currentName, newName);
      } else if (this.itemType === 'folder') {
        success = await renameFolder(this.userId, this.itemToRename.id, newName);
      }

      if (success) {
        // Refresh the list with the current folder ID
        await this.fetchFoldersAndFiles(currentFolderId);
      } else {
        console.error('Failed to rename');
      }
      this.isInputFormVisible = false; // Hide input form
    },
    deleteItem(type, ids) {
      this.itemType = type;
      this.itemsToDelete = ids;
      this.isConfirmFormVisible = true; 
    },
    async handleDelete() {
      let success;
      const currentFolderId = this.folderStack.length ? this.folderStack[this.folderStack.length - 1].id : null;
      try {
        if (this.itemType === 'file') {
          // Gestisce l'eliminazione di file (array o singolo ID)
          if (Array.isArray(this.itemsToDelete)) {
            success = await deleteFile(this.itemsToDelete, this.userId);
          } else {
            success = await deleteFile([this.itemsToDelete], this.userId);
          }
        } else if (this.itemType === 'folder') {
          // Gestisce l'eliminazione di una cartella (singolo ID)
          success = await deleteFolder(this.itemsToDelete, this.userId);
        }
        if (success) {
          // Aggiorna la lista dei file e delle cartelle
          await this.fetchFoldersAndFiles(currentFolderId);
       //      this.isConfirmFormVisible = false; o qui?
        } else {
          console.error(`Failed to delete ${this.deleteType}`);
        }
      } catch (error) {
        console.error(`Errore durante l'eliminazione del ${this.deleteType}:`, error);
      }
      // Resetta lo stato dell'eliminazione
      this.itemsToDelete = null;
      this.itemType = null;
      this.isConfirmFormVisible = false;
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
.delete-button,
.download-button, .rename-button {
  display: none;
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
}
.delete-button{
  margin-right: 15rem;
}
.rename-button{
  margin-right: 9rem;
}

.folder-item:hover .delete-button,
.folder-item:hover .download-button,
.folder-item:hover .rename-button,
.file-item:hover .delete-button,
.file-item:hover .rename-button,
.file-item:hover .download-button {
  display: inline-block;
}
</style>