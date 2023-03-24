<template>
	<div class="sidebar">
		<nav>
			<ul>
				<li @click="selectedOption = 'user-info'">User Info</li>
				<li @click="selectedOption = 'my-listings'">My Listings</li>
				<li @click="selectedOption = 'favorite-listings'">Favorite Listings</li>
			</ul>
		</nav>
		<div v-if="selectedOption === 'user-info'">
			<user-info />
		</div>
		<div v-else-if="selectedOption === 'my-listings'">
			<listing-card-grid :Listings="owned"/>
		</div>

		<div v-else-if="selectedOption === 'favorite-listings'">
			<listing-card-grid :Listings="favorites"/>
		</div>
	</div>
</template>

<script lang="ts">
import UserInfo from '@/components/UserInfo.vue';
import ListingCardGrid from '@/components/Listingcardgrid.vue';
import { useGlobalState } from '@/globalState';
import axios from 'axios';

export default {
	components: {
		ListingCardGrid,
		UserInfo
	},
	data() {
		return {
			selectedOption: 'user-info',
			favorites: [],
			owned: [],
		};
	},
	setup() {
		const { serverIP } = useGlobalState();
		return {
			serverIP,
		};
	},
	created() {
		const token = localStorage.getItem('authToken');
		axios.get(this.serverIP + '/api/user/getUser/favorites', {
			headers: {
				'Authorization': `Bearer ${token}`,
			},
		})
		.then(response => {
			this.favorites = response.data;
			console.log(response.data);
			console.log('favorites' + this.favorites);
		})
		.catch(error => {
			console.error(error);
		});

		axios.get(this.serverIP + '/api/user/getUser/listings', {
			headers: {
				'Authorization': `Bearer ${token}`,
			},
		})
		.then(response => {
			this.owned = response.data;
			console.log(response.data);
		})
		.catch(error => {
			console.error(error);
		});
	}
};
</script>

<style scoped>
.sidebar {
	display: flex;
	flex-direction: row;
}

nav {
	background-color: #f1f1f1;
	overflow: hidden;
	display: flex;
	flex-direction: column;
}

nav ul {
	margin: 0;
	padding: 0;
	list-style: none;
	display: flex;
	flex-direction: column;
}

nav li {
	text-align: center;
	padding: 14px 16px;
	cursor: pointer;
}

nav li:hover {
	background-color: #ddd;
}

nav li.active {
	background-color: #4CAF50;
	color: white;
}
</style>