<template>
    <div class="password-change-form">
      <h2>{{ $t('changePassword') }}</h2>
      <form @submit.prevent="changePassword">
        <div class="form-group">
          <label for="old-password">{{ $t('oldPassword') }}:</label>
          <input id="old-password" type="password" v-model="oldPassword" required />
        </div>
        <div class="form-group">
          <label for="new-password">{{ $t('newPassword') }}:</label>
          <input id="new-password" type="password" v-model="newPassword" required />
        </div>
        <div class="form-group">
            <label for="confirm-password">{{ $t('confirmPassword') }}:</label>
            <input id="confirm-password" type="password" v-model="confirmPassword" required />
        </div>
        <div class="form-group">
          <button type="submit" :disabled="!passwordsMatch">{{ $t('change') }}</button>
        </div>
      </form>
    </div>
  </template>
  
  <script lang="ts">
  import axios from 'axios';
  import { getIp } from '@/globalState';
  
  export default {
    data() {
      return {
        oldPassword: '',
        newPassword: '',
        confirmPassword: '',
      };
    },
    computed: {
        passwordsMatch() {
            return this.newPassword === this.confirmPassword;
        },
    },
    setup() {
      const { serverIP } = getIp();
      return {
        serverIP,
      };
    },
    methods: {
      async changePassword() {
        try {
          const token = localStorage.getItem('authToken');
          const response = await axios.put(
            this.serverIP + '/api/user/editUser/password',
            {
              oldPassword: this.oldPassword,
              newPassword: this.newPassword,
            },
            {
              headers: {
                'Authorization': `Bearer ${token}`,
              },
            },
          );
          localStorage.removeItem('authToken');
          localStorage.setItem('authToken', response.data);
          this.$router.push('/');
        } catch (error) {
          console.log(error);
        }
      },
    },
  };
  </script>

<style scoped>
.password-change-form {
  max-width: 600px;
  margin: auto;
}

h2 {
  text-align: center;
}

.form {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 1rem;
  width: 100%;
}

label {
  font-weight: bold;
}

input {
  padding: 0.5rem;
  border: 1px solid #ccc;
border-radius: 4px;
width: 100%;
}

button {
padding: 0.5rem;
background-color: #007bff;
color: white;
border: none;
border-radius: 4px;
cursor: pointer;
transition: background-color 0.3s ease;
}

button:hover {
background-color: #0062cc;
}

@media (max-width: 767px) {
input {
font-size: 0.8rem;
}

button {
font-size: 0.8rem;
}
}
</style>