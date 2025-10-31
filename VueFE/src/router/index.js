import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import SpacesPage from '../views/SpacesPage.vue';
import FoldersPage from '../views/FoldersPage.vue';
import '/src/styles/buttons.css';
import '/src/styles/hoverEffect.css';
import '/src/styles/navbar.css';
import '/src/styles/scrollbar.css';
import '/src/styles/imageModal.css';

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/spaces', name: 'Spaces', component: SpacesPage },
  { path: '/folders', name: 'Folders', component: FoldersPage },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
