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
      <div v-if="isLoggedIn" class="welcome-container">
        <div class="welcome-text">
          <span>Welcome</span>
          <span>{{ username }}</span>
        </div>
        <button class="grey-button logout-button" @click="$emit('logout')">Logout</button>
      </div>
    </div>
    <div class="hover-effect" ref="hoverEffect"></div>
  </nav>
</template>


<script>
import { mapState } from 'vuex';
import hoverEffect from '/src/utils/hoverEffect.js';

export default {
  mixins: [hoverEffect],
  data() {
    return {
      searchQuery: '',
    };
  },
  computed: {
    ...mapState({
      isLoggedIn: state => state.isLoggedIn,
      username: state => state.username
    }),
    isHome() {
      return this.$route.path === '/';
    }
  },
  methods: {
    performSearch() {
      console.log('Ricerca eseguita per:', this.searchQuery);
    },
    logout() {
      this.$store.commit('logout');
    }
  }
};
</script>