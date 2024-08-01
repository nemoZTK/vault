import Vue from 'vue';
import App from './App.vue';
import router from './router';
import './styles/variables.css';
import './styles/scrollbar.css'; 
import './styles/dropdown.css';
Vue.config.productionTip = true;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
