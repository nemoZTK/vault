// store.js
import { createStore } from 'vuex';

export default createStore({
  state: {
    isLoggedIn: false,
    username: ''
  },
  mutations: {
    login(state, username) {
      state.isLoggedIn = true;
      state.username = username;
    },
    logout(state) {
      state.isLoggedIn = false;
      state.username = '';
    }
  }
});
