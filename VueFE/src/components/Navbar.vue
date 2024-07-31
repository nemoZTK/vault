<template>
  <nav class="navbar" :class="{ 'with-search': !isHome }" @mousemove="handleMouseMove" @mouseleave="handleMouseLeave">
    <div class="navbar-brand">
      <a href="./" :class="{ 'compressed': !isHome }">Vault</a>
    </div>
    <div class="navbar-search" v-if="!isHome">
  <input type="text" placeholder="Cerca..." v-model="searchQuery" />
  <button @click="performSearch">🔍</button>
</div>
    <div class="navbar-menu">
      <button class="grey-button" v-if="!isLoggedIn" @click="$emit('show-login')">Login</button>
      <button  class="grey-button" v-if="!isLoggedIn" @click="$emit('show-register')">Registrati</button>
      <div v-if="isLoggedIn">
        <span>Benvenuto {{ username }}</span>
        <button @click="$emit('logout')">Logout</button>
      </div>
    </div>
    <div class="hover-effect" ref="hoverEffect"></div>
  </nav>
</template>


<script>
import hoverEffect from '/src/utils/hoverEffect.js';


export default {
  props: {
    isLoggedIn: Boolean,
    username: String
  },
  mixins: [hoverEffect],
  data() {
    return {
      searchQuery: '',
    };
  },
  computed: {
    isHome() {
      return this.$route.path === '/';
    }
  },
  methods: {
    performSearch() {
      // Placeholder per la logica di ricerca
      console.log('Ricerca eseguita per:', this.searchQuery);
    }
  }
};
</script>
