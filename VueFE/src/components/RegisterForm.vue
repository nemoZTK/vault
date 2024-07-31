<template>
  <BaseForm title="Registrati" submitButtonText="Conferma" @submit="register" @close="hideForm">
    <template v-slot:form-fields>
      <input type="text" v-model="username" placeholder="Username" required />
      <input type="email" v-model="email" placeholder="Email" required />
      <input type="password" v-model="password" placeholder="Password" required />
      <input type="password" v-model="confirmPassword" placeholder="Conferma Password" required />
    </template>
    <template v-slot:footer>

      <p>Hai già un account? <button type="button" @click="switchToLogin">Login</button></p>

    </template>
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
    hideForm() {
      this.$emit('close');
    },
    switchToLogin() {
      this.$emit('switch-form', 'login');
    }
  },
};
</script>
<style scoped>
.form-container .footer-slot button{
  margin-left: 3.85rem;
}
</style>