import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store/store';
import './styles/variables.css';
import './styles/scrollbar.css'; 
import './styles/dropdown.css';
Vue.config.productionTip = true;

new Vue({
store,router,
  render: h => h(App),
}).$mount('#app');
