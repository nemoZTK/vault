<template>
  <div class="spaces">
    <BaseMainBlock @add-new-space="createNewSpace">
      <template #header>
        <h1>S P A C E S</h1>
      </template>
      <template #default>
        <ul>
          <li v-for="space in spaces" :key="space.id" 
              :data-space-name="space.name" 
              @click="goToFoldersPage(space.id, space.name)">
            {{ space.name }}
          </li>
        </ul>
      </template>
    </BaseMainBlock>
  </div>
</template>


<script>
import BaseMainBlock from '../components/BaseMainBlock.vue';
import InputForm from '../components/InsertNameForm.vue';
import protectedApiClient from '../protectedApiClient'; 

export default {
  components: {
    BaseMainBlock,
    InputForm
  },
  data() {
    return {
      spaces: []
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
          this.spaces = response.data.spaces;
        }
      } catch (error) {
        console.error('Errore nel recupero degli spazi:', error);
      }
    },

    async createNewSpace(spaceName) {
      const userId = localStorage.getItem('id');
      if (!userId || !spaceName) {
        return;
      }

      try {
        const response = await protectedApiClient.post('/storage/newspace', {
          userId: parseInt(userId),
          spaceName: spaceName
        });

        if (response.status === 200) {
          console.log("space creato");
          

          await this.fetchSpaces(); // Ricarica la lista degli spazi
        }
      } catch (error) {
        console.error('Errore nella creazione dello spazio:', error);
      }
    },
    goToFoldersPage(spaceId, spaceName) {
      localStorage.setItem('selectedSpaceId', spaceId);
      localStorage.setItem('selectedSpaceName', spaceName);
      this.$router.push('/folders'); // Naviga alla pagina delle cartelle
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
  color: var(--menu-hover-color);
  padding: 10px;
  border-bottom: 1px solid var(--menu-primary-color);
}

.spaces li:hover {
  background-color: var(--menu-hover-color);
  color:var(--menu-font-color);
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
