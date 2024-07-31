<template>
  <BaseForm title="Registrati" submitButtonText="Conferma" @submit="register" @close="$emit('close')">
    <input type="text" v-model="username" placeholder="Username" required />
    <input type="email" v-model="email" placeholder="Email" required />
    <input type="password" v-model="password" placeholder="Password" required />
    <input type="password" v-model="confirmPassword" placeholder="Conferma Password" required />
  </BaseForm>
</template>

<script>
import BaseForm from './BaseForm.vue';

export default {
  components: { BaseForm },
  data() {
    return {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
    };
  },
  methods: {
    register() {
      // Logica di registrazione
      this.$emit('close');
    },
  },
};
</script>

