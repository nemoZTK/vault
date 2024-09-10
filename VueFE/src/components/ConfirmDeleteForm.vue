<template>
    <BaseForm title="Confirm Deletion" submitButtonText="Confirm Delete" @submit="submit" @close="closeForm">
      <template v-slot:form-fields>
        <p class="alert">Are you sure you want to delete {{ itemName }}?</p>
      </template>
    </BaseForm>
  </template>
  
  <script>
  import BaseForm from './BaseForm.vue';
  
  export default {
    components: {
      BaseForm
    },
    props: {
      itemName: {
        type: String,
        required: true
      }
    },
    methods: {
      submit() {
        this.$emit('submit');
        
      },
      closeForm() {
        this.$emit('close');
      }
    }
  };
  </script>

<style scoped>
/* Stili specifici per InsertNameForm */
.alert{
font-weight: bold;
}
.form-container {
  position: fixed;
  top: 20%; /* Modifica questa posizione se necessario */
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000; /* Assicurati che sia sopra gli altri elementi */
  /* Aggiungi ulteriori stili se necessario */
}


.form-container h2 {
  margin: 0; /* Aggiusta il margine se necessario */
}

.form-container input {
  width: 93%; /* Assicurati che l'input si adatti al contenitore */
}

.form-container button {
  width: 100%; /* Assicurati che i pulsanti si adattino al contenitore */
}
</style>