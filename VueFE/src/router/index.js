import Vue from 'vue';
import Router from 'vue-router';
import Home from '../views/Home.vue';
import Topics from '../views/TopicsPage.vue';
import Codebase from '../views/CodebasePage.vue';
import "/src/styles/buttons.css";
import '/src/styles/hoverEffect.css';
import '/src/styles/navbar.css';
import '/src/styles/scrollbar.css';

Vue.use(Router);

const routes = [
  { path: '/', component: Home },
  { path: '/topics', component: Topics },
{path:'/codebase',component:Codebase}
];

const router = new Router({
  mode: 'history',
  routes
});

export default router;
