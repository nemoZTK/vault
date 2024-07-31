<template>
  <div id="app">
    <Background />
    <Navbar @show-login="toggleForm('login')" @show-register="toggleForm('register')" @logout="logout" :is-logged-in="isLoggedIn" :username="username" />
    <div v-if="showForm" class="overlay">
      <LoginForm v-if="currentForm === 'login'" @close="hideForm" @switch-form="toggleForm" />
      <RegisterForm v-if="currentForm === 'register'" @close="hideForm" @switch-form="toggleForm" />
    </div>
    <router-view></router-view>
  </div>
</template>

<script>
import Navbar from './components/Navbar.vue';
import LoginForm from './components/LoginForm.vue';
import RegisterForm from './components/RegisterForm.vue';
import Background from './components/Background.vue';

export default {
  components: {
    Navbar,
    LoginForm,
    RegisterForm,
    Background
  },
  data() {
    return {
      showForm: false,
      currentForm: '',
      isLoggedIn: false,
      username: '',
    };
  },
  methods: {
    toggleForm(formType) {
      if (this.currentForm === formType) {
        this.showForm = false;  // Chiudi il form se lo stesso pulsante viene premuto di nuovo
        this.currentForm = '';
      } else {
        this.currentForm = formType;
        this.showForm = true;   // Apri il form selezionato
      }
    },
    hideForm() {
      this.showForm = false;
      this.currentForm = '';
    },
    logout() {
      this.isLoggedIn = false;
      this.username = '';
    }
  }
};
</script>

<style>
/* Impostazioni globali per rimuovere margini e padding */
html, body {
  margin: 0;
  width: 100%;
  height: 100%;
}

#app {
  min-height: 100vh;
  margin: 0;
  font-family: Arial, sans-serif;
  position: relative;
}

.overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  z-index: 2000;
  align-items: center;
  pointer-events: none; /* Permette il pass-through degli eventi */
}

</style>
