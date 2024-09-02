<template>
  <div class="spaces">
    <BaseMainBlock @add-new-space="createNewSpace">
      <template #header>
        <h1>S P A C E S</h1>
      </template>
      <template #default>
        <ul>
          <li v-for="space in spaces" :key="space.id" @click="selectSpace(space.id)">
            {{ space.name }}
          </li>
        </ul>
      </template>
    </BaseMainBlock>
  </div>
</template>

<script>
import BaseMainBlock from '../components/BaseMainBlock.vue';
import protectedApiClient from '../protectedApiClient'; // Assicurati che il percorso sia corretto

export default {
  components: {
    BaseMainBlock
  },
  data() {
    return {
      spaces: [] // Array per memorizzare gli spazi recuperati
    };
  },
  async mounted() {
    await this.fetchSpaces();
  },
  methods: {
    async fetchSpaces() {
      try {
        const userId = localStorage.getItem('id');
        if (userId != null) {
          const response = await protectedApiClient.get(`/storage/spaces/all/${userId}`);
          this.spaces = response.data.spaces; // Assumi che `spaces` sia il campo nella risposta
        }
      } catch (error) {
        console.error('Errore nel recupero degli spazi:', error);
      }
    },
    async createNewSpace() {
      const userId = localStorage.getItem('id');
      const spaceName = prompt("Inserisci il nome del nuovo spazio:");
      if (!spaceName || !userId) {
        return;
      }

      try {
        const response = await protectedApiClient.post('/storage/newspace', {
          userId: parseInt(userId), 
          spaceName: spaceName
        });

        if (response.status === 200) {
          alert(`Spazio "${response.data.name}" creato con successo!`);
          await this.fetchSpaces(); // Ricarica la lista degli spazi
        }
      } catch (error) {
        console.error('Errore nella creazione dello spazio:', error);
      }
    },
    selectSpace(spaceId) {
      localStorage.setItem('selectedSpaceId', spaceId);
      console.log(`Space ID ${spaceId} selezionato.`);
      // Puoi anche navigare a una nuova pagina o effettuare altre azioni
    }
  }
};
</script>

<style scoped>
.spaces ul {
  list-style-type: none;
  padding: 0;
}

.spaces li {
  cursor: pointer;
  padding: 10px;
  border-bottom: 1px solid #ccc;
}

.spaces li:hover {
  background-color: #f0f0f0;
}

/* Stili specifici per la pagina spaces */
.base-main-header-block h1 {
  margin-left: 8rem;
}
@media (max-width: 768px) {
  .base-main-header-block h1 {
    letter-spacing: 0.6rem;
    padding: 0;
    margin-bottom: 0.5rem;
    margin-right: 14rem;
  }
  .base-main-header-block h1 {
    margin-left: 14rem;
  }
}
</style>