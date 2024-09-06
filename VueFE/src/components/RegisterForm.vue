<template>
  <BaseForm title="Registrati" submitButtonText="Conferma" @submit="register" @close="hideForm">
    <template v-slot:form-fields>
      <input type="text" v-model="username" placeholder="Username" required />
      <input type="email" v-model="email" placeholder="Email" required />
      <p v-if="emailError" class="error-message">{{ emailError }}</p>
      <input type="password" v-model="password" placeholder="Password" required />
      <input type="password" v-model="confirmPassword" placeholder="Confirm Password" required />
      <p v-if="passwordError" class="error-message">{{ passwordError }}</p>
    </template>
    <template v-slot:form-footer>
      <p>Do you already have an account?<button type="button" @click="switchToLogin">Login</button></p>
      <p v-if="genericError" class="error-message">{{ genericError }}</p>
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
      confirmPassword: '',
      emailError: '',  // Variabile per errori email
      passwordError: '', // Variabile per errori password
      genericError: '',  // Variabile per errori generici
    };
  },
  methods: {
    validateEmail(email) {
      // Usa una semplice regex per validare l'email
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(email);
    },
    async register() {
      // Resetta i messaggi di errore
      this.emailError = '';
      this.passwordError = '';
      this.genericError = '';

      // Verifica se le password coincidono
      if (this.password !== this.confirmPassword) {
        this.passwordError = 'Le password non coincidono.';
        return;
      }

      // Verifica se l'email è valida
      if (!this.validateEmail(this.email)) {
        this.emailError = 'Indirizzo email non valido.';
        return;
      }

      try {
        // Invia i dati al server
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
          // Messaggio di errore generico per bad request (400)
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

<style scoped>
.error-message {
  color: red;
  margin-top: 5px;
  text-align: center;
  font-weight: bold;
}

.form-container .footer-slot button {
  margin-left: 2rem;
}
</style>
