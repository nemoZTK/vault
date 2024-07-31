<template>
  <div class="home">
    <Icon src="topics.png" alt="Topic" link="/topics" />
    <Icon src="codebase.png" alt="Codebase" link="/codebase" />
  </div>
</template>

<script>
import Icon from '../components/Icon.vue';

export default {
  components: {
    Icon
  },
  computed: {
    isLoggedIn() {
      return this.$root.isLoggedIn; // Accesso al root instance
    }
  }
};
</script>

<style scoped>
.home {
  text-align: center;
  padding: 8rem 2rem 2rem; /* Spazio sopra e sotto la vista */
  background-color: #56614fb7; /* Cambia il colore di sfondo specifico per questa vista */
  min-height: 100vh; /* Assicura che il colore di sfondo copra l'intera altezza della vista */
}

</style>
