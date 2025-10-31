import { createStore } from 'vuex';

export default createStore({
  state: {
    isLoggedIn: false,
    username: '',
    id: 0,
    token: ''
  },
  mutations: {
    login(state, { username, id, token }) {
      state.isLoggedIn = true;
      state.username = username;
      state.id = id;
      state.token = token;
      localStorage.setItem('authToken', token);
      localStorage.setItem('username', username);
      localStorage.setItem('id', id);
      console.log("login ---> ", state.isLoggedIn, username, id);
    },
    logout(state) {
      state.isLoggedIn = false;
      state.username = '';
      state.id = 0;
      state.token = '';
      localStorage.removeItem('authToken');
      localStorage.removeItem('username');
      localStorage.removeItem('id');
      console.log("logout ---> ", state.isLoggedIn, state.username, state.id);
    },
    register(state, { username, id, token }) {
      state.username = username;
      state.id = id;
      state.token = token;
      state.isLoggedIn = true;
      localStorage.setItem('authToken', token);
      localStorage.setItem('username', username);
      localStorage.setItem('id', id);
      console.log("register ---> ", state.isLoggedIn, username, id);
    },
    setAuthData(state, { username, id, token }) {
      state.username = username;
      state.id = id;
      state.token = token;
      state.isLoggedIn = true;
    }
  },
  actions: {
    initializeStore({ commit }) {
      const username = localStorage.getItem('username');
      const id = localStorage.getItem('id');
      const token = localStorage.getItem('authToken');
      if (username && id && token) {
        commit('setAuthData', { username, id, token });
      }
    }
  }
});
