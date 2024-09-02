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
import axios from '../axios'; // Importa l'istanza axios configurata
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
    async register() {
      if (this.password !== this.confirmPassword) {
        alert("Le password non corrispondono");
        return;
      }

      try {
        const response = await axios.post('/api/users/create', {
          username: this.username,
          email: this.email,
          password: this.password
        });

        if (response.status === 200) {
          // Gestione della risposta di successo
          const { id, username, token } = response.data;
          console.log('Registrazione avvenuta con successo:', { id, username, token });

          // Puoi salvare il token nel localStorage se necessario
          localStorage.setItem('token', token);

          // Qui potresti fare altre operazioni come login, reindirizzamento, ecc.
          this.$store.commit('login', { username, token });
          this.$emit('registered', { username, token }); // Emesso evento registrazione con successo
          this.hideForm();
        } else {
          // Gestisci errore di registrazione
          alert('Errore nella registrazione');
        }
      } catch (error) {
        // Gestisci errore di rete o di backend
        if (error.response && error.response.status === 400) {
          // Gestisci errore specifico di backend se necessario
          alert('Errore di registrazione: dati non validi');
        } else {
          console.error('Errore durante la registrazione:', error);
          alert('Si è verificato un errore durante la registrazione');
        }
      }
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
.form-container .footer-slot button {
  margin-left: 3.85rem;
}
</style>
