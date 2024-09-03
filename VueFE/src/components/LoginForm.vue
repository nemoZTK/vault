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
import axios from '../publicApiClient'; // Assicurati che il percorso sia corretto

export default {
  components: { BaseForm },
  data() {
    return {
      username: '',
      password: '',
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
          // Recupera la risposta del server
          const [id, token] = response.data.split('|');

          // Memorizza l'ID, il token e l'username nel localStorage
          localStorage.setItem('authToken', token);
          localStorage.setItem('username', this.username);
          localStorage.setItem('id', id);
          console.log("done for ", this.username, id, token);
          // Aggiorna lo stato di Vuex
          this.$store.commit('login', { username: this.username, id, token });

          // Chiudi il modulo di login
          this.hideForm();
        }
      } catch (error) {
        console.error('Errore:', error.response ? error.response.data : error.message);
        alert('Errore durante il login. Per favore, riprova.');
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
.form-container .footer-slot button{
  margin-left: 1.55rem;
}
</style>