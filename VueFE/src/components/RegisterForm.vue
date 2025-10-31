<template>
  <BaseForm title="Registrati" submitButtonText="Conferma" @submit="register" @close="hideForm">
    <template v-slot:form-fields>
      <input type="text" v-model="username" placeholder="Username" required />
      <input type="email" v-model="email" placeholder="Email" required />
      <p v-if="genericError" class="error-message">{{ genericError }}</p>
      <input type="password" v-model="password" placeholder="Password" required />
      <input type="password" v-model="confirmPassword" placeholder="Confirm Password" required />
      <p v-if="passwordError" class="error-message">{{ passwordError }}</p>
    </template>
    <template v-slot:footer>
      <p>Do you already have an account?<button type="button" @click="switchToLogin">Login</button></p>
    </template>
  </BaseForm>
</template>
<script>
import BaseForm from './BaseForm.vue';
import axios from '../publicApiClient';
import '@/styles/Form.css'

export default {
  components: { BaseForm },
  data() {
    return {
      username: '',
      email: '',
      password: '',
      confirmPassword: '',
      genericError: '', 
    };
  },
  methods: {
    validateEmail(email) {

      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(email);
    },
    async register() {
      this.genericError = '';

      if (this.password !== this.confirmPassword) {
        this.genericError = 'Le password non coincidono.';
        return;
      }
      try {

        const response = await axios.post('/users/create', {
          username: this.username,
          email: this.email,
          password: this.password
        });

        if (response.status === 200) {
          const { id, username, token } = response.data;
          localStorage.setItem('authToken', token);
          console.log("done for ", username, id, token);
          this.$store.commit('register', { username, id , token });
          this.hideForm();
          window.location.reload();
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {

          this.genericError = 'Errore nella registrazione. Per favore, riprova.';
        } else {
          console.error('Errore:', error);
        }
      }
    },
    switchToLogin() {
      this.$emit('switch-form', 'login');
    },
    hideForm() {
      this.$emit('close');
    }
  }
};
</script>
