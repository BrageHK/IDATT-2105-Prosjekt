import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ListingView from '../views/ListingView.vue';
import LoginView from '../views/LoginView.vue';
import CreateListingView from '../views/CreateListingView.vue';
import UserviewVue from '@/views/Userview.vue';
import EditListingView from '@/views/EditListingView.vue';
import AdminViewVue from '@/views/AdminView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/listing/:id',
      component: ListingView,
      meta: { requiresAuth: false }
    },
    {
      path: '/create-listing',
      name: 'create-listing',
      component: CreateListingView,
      meta: { requiresAuth: true }
    },
    {
      path: '/listing/:id/edit',
      name: 'edit-listing',
      component: EditListingView,
      meta: { requiresAuth: true }
    },
    {
      path: '/user',
      name: 'user',
      component: UserviewVue,
      meta: { requiresAuth: true }
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminViewVue,
      meta: { requiresAuth: true }
    },
  ]
}) 

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('authToken');
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (isAuthenticated) {
      next(); 
    } else {
      next({ path: '/login' }); 
    }
  } else if (to.path === '/login' && isAuthenticated) {
    next({ path: '/' });
  } else {
    next(); 
  }
});



export default router
