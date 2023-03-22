

<template>
<div class="wrapper">
    <nav>

        <RouterLink to="/"><img :src="logoPath" alt="My Logo" class="logo" /></RouterLink>
        <div v-if="isAuthenticated" class="nav-link" @click="logout">logg ut</div>
      <div v-if="!isAuthenticated"><router-link class="nav-link" to="/login">Log In</router-link></div>

    </nav>
  </div>
</template>

<script setup lang="ts">

import logoPath from '@/assets/images/logo.svg';
import router from '@/router';

import { ref } from 'vue';

const isAuthenticated = ref(!!localStorage.getItem('authToken'));


function logout() {
  localStorage.removeItem('authToken');
  window.location.reload();
  router.push('/');
}



</script>

<style scoped>
.logo {
  display: block;
  margin: 0 2rem 0 0;
  height: 75px; 
  width: auto; 
}

nav {
  display: flex;
  align-items: center;
  justify-content: flex-start;;
  position: fixed;
  height: 100px;
  background-color: #fff;
  top: 0;
  left: 0;
  right: 0;
  z-index: 9999;
  border-bottom: 1px solid #0718c4;
}

nav .nav-link {
  display: inline-block;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  margin: 0 1rem;
  font-size: 1.2rem;
  font-weight: bold;
  color: #333;
  text-decoration: none;
  background-color: #fff;
  transition: all 0.2s ease;
}

nav .nav-link:hover,
nav .nav-link .router-link-active,
nav .nav-link.router-link-exact-active {
  background-color: #0718c4;
  color: #fff;
  text-decoration: none;
}

nav .nav-link .router-link {
  color: #333;
  text-decoration: none;
}


.logo:hover{
  border: 4px solid #0718c4;
  border-radius: 1rem;
}
</style>