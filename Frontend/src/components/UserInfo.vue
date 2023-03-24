<template>
	<div class="user-info">
		<h2>User Information</h2>
		<div v-if="user" class="form">
			<div class="form-group">
				<label for="first-name">First Name:</label>
				<input id="first-name" v-model="user.firstName" />
			</div>
			<div class="form-group">
				<label for="last-name">Last Name:</label>
				<input id="last-name" v-model="user.lastName" />
			</div>
			<div class="form-group">
				<label for="email">Email:</label>
				<input id="email" v-model="user.email" />
			</div>
			<div class="form-group">
				<label for="phone">Phone:</label>
				<input id="phone" v-model="user.phoneNumber" />
			</div>
			<div class="form-group">
				<label for="address">Address:</label>
				<input id="address" v-model="user.address" />
			</div>
			<div class="form-group">
				<button @click="saveUser">Save</button>
			</div>
		</div>
		<div v-else>
			<p>Loading user information...</p>
		</div>
	</div>
</template>

<script lang="ts">
import axios from 'axios';
import { useGlobalState } from '@/globalState';
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
		const { serverIP } = useGlobalState();
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
		async saveUser() {},
	},
};
</script>

<style scoped>
.user-info {
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