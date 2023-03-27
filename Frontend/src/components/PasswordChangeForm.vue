<template>
    <div class="form-container">
      <h2 class="form-container__title">{{ $t('changePassword') }}</h2>
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

</style>