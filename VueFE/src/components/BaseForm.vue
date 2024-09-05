<template>
  <div class="form-container" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <h2>{{ title }}</h2>
    <form @submit.prevent="submit">
      <slot name="form-fields"></slot> <!-- Slot per i campi del modulo -->
      <button type="submit">{{ submitButtonText }}</button>
      <button type="button" @click="closeForm">Annulla</button>
      <div class="footer-slot">
      <slot name="footer"></slot> <!-- Slot per i pulsanti di cambio modulo -->
      </div>
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

.form-container .footer-slot {
  margin-top: -1rem;
  font-size: 1rem; /* Dimensione del testo del footer */
  text-align: center; /* Allinea il testo al centro */
  white-space: nowrap;
  font-weight: bold;
}
/* Stili del contenitore del modulo */
.form-container {/*questo dovrebbe stare sopra*/

  position: fixed;
  color: var(--menu-title-color);
  background-color: var(--menu-primary-color);
  padding: 2rem;
  backdrop-filter: blur(10px);
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  height:min-content;
  width: 25%;
  pointer-events: auto; /* Permette l'interazione */
  margin: auto;
  overflow: hidden; /* Assicura che l'effetto hover non esca dal contenitore */
}
.form-container h2 {
  margin-top: -1rem;
}

form {

  display: flex;
  flex-direction: column;
}

input {

  margin-bottom: 0.6rem;
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
  margin-top: 0.6rem;
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
@media (max-width: 768px) {
 
  .form-container {
  
  max-width: max-content;
  width: min-content;
  height: min-content;


}

}
</style>