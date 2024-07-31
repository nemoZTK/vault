<template>
  <BaseForm title="Login" submitButtonText="Conferma" @submit="login" @close="$emit('close')">
    <input type="text" v-model="username" placeholder="Username" required />
    <input type="password" v-model="password" placeholder="Password" required />
  </BaseForm>
</template>

<script>
import BaseForm from './BaseForm.vue';

export default {
  components: { BaseForm },
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    login() {
      // Logica di login
      this.$emit('close');
    },
  },
};
</script>
