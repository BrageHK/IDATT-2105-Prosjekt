<template>
  <div class="login-container">
    <h2>Logg inn</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="email">Epost:</label>
        <input type="email" id="email" v-model="email" required />
      </div>
      <div class="form-group">
        <label for="password">Passord:</label>
        <input type="password" id="password" v-model="password" required />
      </div>
      <button type="submit">Logg inn</button>
    </form>
  </div>
</template>

<script lang="ts">
import router from '@/router';
import axios from 'axios';
export default {
  data() {
    return {
      email: '',
      password: '',
    };
  },
  methods: {
    handleSubmit() {
      axios 
        .post('http://192.168.86.40:8080/api/v1/auth/authenticate', {
          email: this.email,
          password: this.password,
        })
        .then((response) => {
          const token = response.data.token;
          localStorage.setItem('authToken', token);
          console.log(token)
          console.log(response);
          router.push('/');
          window.location.reload();
        })
        .catch((error) => {
          console.error(error);
        });
    },
  },
};
</script>

<style scoped>
.login-container {
  width: 300px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 3px;
}

button {
  background-color: #007bff;
  color: #fff;
  padding: 8px 16px;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}
</style>
