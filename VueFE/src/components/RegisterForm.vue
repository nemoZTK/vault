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
import axios from '../publicApiClient'; // Assicurati che questo percorso sia corretto

export default {  
  components: { BaseForm },
  data() {
    return {
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    };
  },
  methods: {
    async register() {
      if (this.password !== this.confirmPassword) {
        alert('Le password non coincidono.');
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
          this.$store.commit('register', { username, id , token }); // Usa Vuex per aggiornare lo stato
          this.hideForm();
          window.location.reload();
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          alert('Errore nella registrazione. Per favore, riprova.');
        } else {
          console.error('Errore:', error);
        }
      }
    },
    switchToLogin() {
      this.$emit('switch-form','login');
    },
    hideForm() {
      this.$emit('close');
    }
  }
};
</script>

<style scoped>
.form-container .footer-slot button {
  margin-left: 3.85rem;
}
</style>
