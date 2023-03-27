<template>
	<div class="form-container">
		<h2 class="form-container__title">{{ $t('createAdmin') }}</h2>
		<form @submit.prevent="handleSubmit">
			<div class="form-group">
				<label for="name">{{ $t('firstName') }}:</label>
				<input type="text" id="firstname" v-model="firstName" required />
			</div>
			<div class="form-group">
				<label for="name">{{ $t('lastName') }}:</label>
				<input type="text" id="lastname" v-model="lastName" required />
			</div>
			<div class="form-group">
				<label for="email">{{ $t('email') }}:</label>
				<input type="email" id="email" v-model="email" required />
			</div>
			<div class="form-group">
				<label for="address">{{ $t('address') }}:</label>
				<input type="text" id="address" v-model="address" required />
			</div>
			<div class="form-group">
				<label for="password">{{ $t('password') }}:</label>
				<input type="password" id="password" v-model="password" required />
			</div>
			<div class="form-group">
				<label for="phoneNumber">{{ $t('phone') }}:</label>
				<input type="number" id="phoneNumber" v-model="phoneNumber" required />
			</div>
            <div class="checkbox-container">
                <label for="isAdmin">{{ $t('admin') }}:</label>
                <input type="checkbox" id="isAdmin" v-model="isAdmin" />
            </div>
			<button type="submit">{{ $t('create') }}</button>
	  </form>
	</div>
</template>

<script lang="ts">
	import axios from 'axios';
	import { getIp } from '@/globalState';

	export default {
		data() {
			return {
				firstName: '',
				lastName: '',
				email: '',
				password: '',
				address: '',
				phoneNumber : '',
                isAdmin: false,
			};
		},
		setup() {
			const { serverIP } = getIp();
			return {
				serverIP,
			};
		},
		methods: {
			handleSubmit() {
                const token = localStorage.getItem('authToken');
				const phoneNumberAsLong = parseInt(this.phoneNumber, 10);
				axios 
				.post( this.serverIP + '/api/auth/register', {
						firstname: this.firstName,
						lastname: this.lastName,
						email: this.email,
						password: this.password,
                        role: this.isAdmin ? "ADMIN" : "USER",
						address: this.address,
						phonenumber: phoneNumberAsLong,
					},
                    {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    })
				.then(({ data }) => {
				
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
.checkbox-container {
  display: flex;
  align-items: center;
}

.checkbox-container input[type="radio"] {
  margin-right: 0.5rem;
}
</style>