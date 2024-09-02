<template>
  <div class="spaces">
      <BaseMainBlock>
          <template #header>
              <h1> S P A C E S </h1> 
          </template>
          <template #default>
              <ul>
                <!-- not yet implemented -->
              </ul>
          </template>
      </BaseMainBlock>
  </div>
</template>

<script>
import BaseMainBlock from '@/components/BaseMainBlock.vue';
export default {
  components: {
      BaseMainBlock
  },
  data() {
      return {
      };
    }
}
</script>

  <style scoped>
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
  