<template>
  <div class="form-container" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <h2>{{ title }}</h2>
    <form @submit.prevent="submit">
      <slot></slot>
      <button type="submit">{{ submitButtonText }}</button>
      <button type="button" @click="closeForm">Annulla</button>
    </form>
    <!-- Elemento per l'effetto hover -->
    <div class="hover-effect" ref="hoverEffect"></div>
  </div>
</template>

<script>
import hoverEffect from '/src/utils/hoverEffect.js'; // Importa il mixin
import '/src/styles/hoverEffect.css'; // Importa il file CSS per l'effetto hover

export default {
  props: {
    title: String,
    submitButtonText: String,
  },
  mixins: [hoverEffect], // Aggiungi il mixin
  methods: {
    submit() {
      this.$emit('submit');
    },
    closeForm() {
      this.$emit('close');
    }
  },
};
</script>

<style scoped>
/* Stili del contenitore del modulo */
.form-container {
  
  position: relative;
  color: var(--menu-title-color);
  background-color: var(--menu-primary-color);
  padding: 2rem;
  backdrop-filter: blur(10px);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  max-width: 600px;
  margin: auto;
  overflow: hidden; /* Assicura che l'effetto hover non esca dal contenitore */
}

form {
  
  display: flex;
  flex-direction: column;
}

input {

  margin-bottom: 1.5rem;
  padding: 0.75rem;
  font-size: 1rem;
  color: var(--menu-title-color);
  background: var(--menu-bkg-bar-color);
  border: 1px solid #ccc;
  border-radius: 5px;
}

input::placeholder {
  color: var(--menu-placeholder-color);
}

button {
  margin-top: 1rem;
  padding: 0.75rem 1.5rem;
  background: var(--menu-btn-color);
  border: none;
  border-radius: 5px;
  color: var(--menu-font-color);
  font-size: 1rem;
  cursor: pointer;
}

button:hover {
  background: var(--menu-btn-hover-color);
}
</style>
