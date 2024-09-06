<template>
    <BaseForm title="Insert Name" submitButtonText="Confirm" @submit="submit" @close="closeForm">
      <template v-slot:form-fields>
        <input type="text" v-model="name" placeholder="Name..." required />
      </template>
    </BaseForm>
  </template>
  
  <script>
  import BaseForm from './BaseForm.vue';
  
  export default {
    components: {
      BaseForm
    },
    data() {
      return {
        name: ''
      };
    },
    methods: {
      submit() {
        console.log("clicked on confirm for "+this.name);
        
        this.$emit('submit', this.name);
      },
      closeForm() {
        this.$emit('close');
      }
    }
  };
  </script>