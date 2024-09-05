import Vue from 'vue';
import Router from 'vue-router';
import Home from '../views/Home.vue';
//import Topics from '../views/TopicsPage.vue';
import SpacesPage from '../views/SpacesPage.vue';
import FoldersPage from '../views/FoldersPage.vue';
import "/src/styles/buttons.css";
import '/src/styles/hoverEffect.css';
import '/src/styles/navbar.css';
import '/src/styles/scrollbar.css';
import '/src/styles/imageModal.css';

Vue.use(Router);

const routes = [
  // { path: '/topics', component: Topics },
  {
    path: '/', name: 'Home',
    component: Home
  },

  {
    path: '/spaces',
    name: 'Spaces',
    component: SpacesPage
  },
  {
    path: '/folders',
    name: 'Folders',
    component: FoldersPage

  }

];

const router = new Router({
  mode: 'history',
  routes
});

export default router;
