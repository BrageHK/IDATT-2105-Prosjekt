<template>
	<div class="login-form-container">
		<h2 class="form-container__title" >{{ $t('signIn') }}</h2>
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
import { getIp } from '@/globalState';

export default {
	data() {
		return {
			email: '',
			password: '',
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

</style>
