<template>
	<div class="form-container">
		<h2 class="form-container__title">{{ $t('userInfo') }}</h2>
		<div v-if="user" class="form">
			<form @submit.prevent="saveUser">
				<div class="form-group">
					<label for="first-name">{{ $t('firstName') }}:</label>
					<input id="first-name" v-model="user.firstName" />
				</div>
				<div class="form-group">
					<label for="last-name">{{ $t('lastName') }}:</label>
					<input id="last-name" v-model="user.lastName" />
				</div>
				<div class="form-group">
					<label for="email">{{ $t('email') }}:</label>
					<input id="email" type="email" v-model="user.email" required />
				</div>
				<div class="form-group">
					<label for="phone">{{ $t('phone') }}:</label>
					<input id="phone" type="number" v-model="user.phoneNumber" />
				</div>
				<div class="form-group">
					<label for="address">{{ $t('address') }}:</label>
					<input id="address" v-model="user.address" />
				</div>
				<div class="form-group">
					<button type="submit">{{ $t('save') }}</button>
				</div>
			</form>
		</div>
		<div v-else>
			<p>{{ $t('loadUserInfo') }}</p>
		</div>
	</div>
</template>

<script lang="ts">
import axios from 'axios';
import { getIp } from '@/globalState';
export default {
	data() {
		return {
			user: {
				firstName: '',
				lastName: '',
				email: '',
				phoneNumber: '',
				address: '',
			},
		};
	},
	setup() {
		const { serverIP } = getIp();
		return {
			serverIP,
		};
	},
	mounted() {
		this.loadUser();
	},
	methods: {
		async loadUser() {
			try {
				const token = localStorage.getItem('authToken');
				console.log('Token:', token);
				const response = await axios.get(this.serverIP + '/api/user/getUser', {
					headers: {
						'Authorization': `Bearer ${token}`,
					},
				});
				this.user = response.data;
				console.log(response.data);
			} catch (error) {
				console.log(error);
			}
		},
		async saveUser() {
			try {
				const token = localStorage.getItem('authToken');
				const response = await axios.put(this.serverIP + '/api/user/editUser', this.user, {
					headers: {
						'Authorization': `Bearer ${token}`,
					},
				});
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