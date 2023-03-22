import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import ListingView from '../views/ListingView.vue';

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
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/listing/:id',
      component: ListingView,
      meta: { requiresAuth: true }
    }
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
    // Redirect to the home page if the user is authenticated and tries to access the login page
    next({ path: '/' });
  } else {
    next(); 
  }
});



export default router
