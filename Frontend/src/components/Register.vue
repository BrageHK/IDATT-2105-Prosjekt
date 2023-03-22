<template>
    <div class="signup-container">
      <h2>Opprett konto</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="name">Fornavn:</label>
          <input type="text" id="firstname" v-model="firstName" required />
        </div>
        <div class="form-group">
          <label for="name">Etternavn:</label>
          <input type="text" id="lastname" v-model="lastName" required />
        </div>
        <div class="form-group">
          <label for="email">Epost:</label>
          <input type="email" id="email" v-model="email" required />
        </div>
        <div class="form-group">
          <label for="address">Adresse:</label>
          <input type="text" id="address" v-model="address" required />
        </div>
        <div class="form-group">
          <label for="password">Passord:</label>
          <input type="password" id="password" v-model="password" required />
        </div>
        <div class="form-group">
          <label for="phoneNumber">Telefonnummer:</label>
          <input type="number" id="phoneNumber" v-model="phoneNumber" required />
        </div>
        <button type="submit">Opprett</button>
      </form>
    </div>
  </template>
  
  <script lang="ts">
  import axios from 'axios';

  export default {
    data() {
      return {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        address: '',
        phoneNumber : '',
      };
    },
    methods: {
      handleSubmit() {
        const phoneNumberAsLong = parseInt(this.phoneNumber, 10);
        axios 
        .post('http://192.168.86.40:8080/api/v1/auth/register', {
            firstname: this.firstName,
            lastname: this.lastName,
            email: this.email,
            password: this.password,
            address: this.address,
            phonenumber: phoneNumberAsLong,
        })
        .then((response) => {
          const token = response.data.token;
          localStorage.setItem('authToken', token);
          console.log(token)
          console.log(response);
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
  .signup-container {
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
  