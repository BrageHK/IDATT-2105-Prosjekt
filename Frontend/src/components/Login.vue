<template>
	<div class="login-container">
		<h2>{{ $t('signIn') }}</h2>
		<form @submit.prevent="handleSubmit">
			<div class="form-group">
				<label for="email">{{ $t('email') }}:</label>
				<input type="email" id="email" v-model="email" required />
			</div>
			<div class="form-group">
				<label for="password">{{ $t('password') }}:</label>
				<input type="password" id="password" v-model="password" required />
			</div>
			<button type="submit">{{ $t('signIn') }}</button>
		</form>
	</div>
</template>

<script lang="ts">
import axios from 'axios';
import { useGlobalState } from '@/globalState';

export default {
	data() {
		return {
			email: '',
			password: '',
		};
	},
	setup() {
		const { serverIP } = useGlobalState();
		return {
			serverIP,
		};
	},
	methods: {
		handleSubmit() {
			axios 
				.post( `${this.serverIP}/api/auth/authenticate`, {
					email: this.email,
					password: this.password,
				})
				.then(({ data }) => {
					localStorage.setItem('authToken', data.token);
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
