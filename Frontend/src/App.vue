<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import LoginComponent from './components/Login.vue';
</script>

<template>
  <header>
    <div class="wrapper">
      <img src="./assets/images/logo.svg" alt="My Logo" class="logo">
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
      </nav>
    </div>
  </header>
  <RouterView />
  <v-footer>
    <div class="wrapper">
      <p>Copyright © {{ new Date().getFullYear() }} Handel uten Hemninger</p>
    </div>
  </v-footer>
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 2rem 0 0;
  height: 150px; 
  width: auto; 
}

nav {
  display: flex;
  align-items: center;
  justify-content: center;
}

nav a {
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

nav a:hover,
nav a.router-link-exact-active {
  background-color: #0718c4;
  color: #fff;
}

v-footer {
    background-color: #0718c4;
    color: white;
    height: 100px;
    display: flex;
    align-items: center;
    margin-top: auto;
  }

  v-footer .wrapper {
    max-width: 1200px;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  v-footer p {
    margin: 0;
    font-size: 1.2rem;
  }

@media (min-width: 1024px) {
  header {
    display: flex;
    align-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-wrap: wrap;
  }

  nav {
    margin-left: auto;
    font-size: 1rem;
  }
}
</style>