<template>
  <header>
    <div class="wrapper">
      <nav>
        <router-link to="/"><img src="@/assets/images/logo.svg" alt="My Logo" class="logo"></router-link>
        <RouterLink class="test" to="/login">Login</RouterLink>
      </nav>
    </div>
  </header>
  <main>
    <RouterView />
  </main>
  <v-footer>
    <div class="wrapper">
      <p>Copyright © {{ new Date().getFullYear() }} Handel uten Hemninger</p>
    </div>
  </v-footer>
</template>

<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import LoginComponent from './components/Login.vue';
import logoPath from '@/assets/images/logo.svg'

const logo = logoPath
</script>



<style scoped>


main {
  padding-top: 100px;
  min-height: calc(100vh - 215px);
}

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

nav a.test:hover,
nav a.test.router-link-exact-active {
  background-color: #0718c4;
  color: #fff;
}

.logo:hover{
  border: 4px solid #0718c4;
  border-radius: 1rem;
}


v-footer {
    background-color: #0718c4;
    color: white;
    height: 100px;
    display: flex;
    align-items: center;
    margin-top: auto;
    bottom: 0; 
    
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