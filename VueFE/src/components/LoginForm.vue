<template>
  <BaseForm title="Login" submitButtonText="Conferma" @submit="login" @close="hideForm">
    <template v-slot:form-fields>
      <input type="text" v-model="username" placeholder="Username" required />
      <input type="password" v-model="password" placeholder="Password" required />
    </template>
    <template v-slot:footer>
      <p>Non hai un account? <button type="button" @click="switchToRegister">Registrati</button></p>
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
      password: '',
    };
  },
  methods: {
    login() {
      // Logica di login
      this.$emit('close');
    },
    hideForm() {
      this.$emit('close');
    },
    switchToRegister() {
      this.$emit('switch-form', 'register');
    }
  },
};
</script>
<style scoped>
.form-container .footer-slot button{
  margin-left: 1.55rem;
}
</style>