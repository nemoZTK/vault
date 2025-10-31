<template>
    <BaseForm title="Confirm Deletion" submitButtonText="Confirm Delete" @submit="submit" @close="closeForm">
      <template v-slot:form-fields>
        <p class="alert">Are you sure you want to delete {{ itemName }}?</p>
      </template>
    </BaseForm>
  </template>
  
  <script>
  import BaseForm from './BaseForm.vue';
  import '@/styles/Form.css'
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

