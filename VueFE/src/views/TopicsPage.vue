<template>
  <div class="topics">
      <BaseMainBlock>
          <template #header>
              <h1>T O P I  C S</h1> 
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
  /* Stili specifici per la pagina Topics */
  @media (max-width: 768px) {
  .base-main-header-block h1 {
        letter-spacing: 1.6rem;
        padding: 0;
        margin-bottom: 0.5rem;
        margin-right: 14rem;
    }
  }
  </style>
  