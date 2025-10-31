<template>
  <div class="form-container" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <h2>{{ title }}</h2>
    <form @submit.prevent="submit">
      <slot name="form-fields"></slot> <!-- Slot per i campi del modulo -->
      <button type="submit">{{ submitButtonText }}</button>
      <button type="button" @click="closeForm">Cancel</button>
      <div class="footer-slot">
      <slot name="footer"></slot> <!-- Slot per i pulsanti di cambio modulo -->
      </div>
    </form>
    <!-- Elemento per l'effetto hover -->
    <div class="hover-effect" ref="hoverEffect"></div>
  </div>
</template>

<script>
import hoverEffect from '@/utils/hoverEffect.js';
import '@/styles/hoverEffect.css'; 
import '@/styles/Form.css'
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
