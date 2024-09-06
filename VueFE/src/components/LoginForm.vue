<template>
  <BaseForm title="Login" submitButtonText="Confirm" @submit="login" @close="hideForm">
    <template v-slot:form-fields>
      <input type="text" v-model="username" placeholder="Username" required />
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <input type="password" v-model="password" placeholder="Password" required />
    </template>
    <!-- Aggiungi il messaggio di errore -->
    <template v-slot:footer>
      <p>Don't have an account?<button type="button" @click="switchToRegister">Sign-up!</button></p>
    </template>
  </BaseForm>
</template>

<script>
import BaseForm from './BaseForm.vue';
import axios from '../publicApiClient'; // Assicurati che il percorso sia corretto

export default {
  components: { BaseForm },
  data() {
    return {
      username: '',
      password: '',
      errorMessage: '', // Variabile per memorizzare il messaggio di errore
    };
  },
  methods: {
    async login() {
      try {
        // Invia una richiesta POST al server con username e password
        const response = await axios.post('/users/login', {
          username: this.username,
          password: this.password
        });

        if (response.status === 200) {
          const [id, token] = response.data.split('|');

          // Memorizza l'ID, il token e l'username nel localStorage
          localStorage.setItem('authToken', token);
          localStorage.setItem('username', this.username);
          localStorage.setItem('id', id);
          console.log("done for ", this.username, id, token);

          // Aggiorna lo stato di Vuex
          this.$store.commit('login', { username: this.username, id, token });

          // Chiudi il modulo di login e ricarica
          this.hideForm();
          window.location.reload();
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          // Assegna il messaggio di errore se l'errore è 400
          this.errorMessage = 'Wrong username or password';
        } else {
          console.error('Errore:', error.response ? error.response.data : error.message);
          this.errorMessage = 'Errore durante il login. Per favore, riprova.';
        }
      }
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
.error-message {
  color: var(--menu-font-color);
  font-weight: bold;
  margin-right: 1rem;
  text-align: center;
}
.form-container .footer-slot button {
  margin-left: 1.55rem;
}
</style>
