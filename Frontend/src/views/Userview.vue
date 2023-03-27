<template>
	<div class="container">
		<nav class="sidebar">
			<ul>
				<li @click="selectedOption = 'user-info'" :class="{ active: selectedOption === 'user-info' }">{{ $t('userInfo') }}</li>
				<li @click="selectedOption = 'my-listings'" :class="{ active: selectedOption === 'my-listings' }">{{ $t('myListings') }}</li>
				<li @click="selectedOption = 'favorite-listings'" :class="{ active: selectedOption === 'favorite-listings' }">{{ $t('favoriteListings') }}</li>
			</ul>
		</nav>
		<div class="content">
			<div v-if="selectedOption === 'user-info'">
				<div class="edit-wrapper">
					<user-info />
					<password-change-form/>
				</div>
			</div>
			<div v-else-if="selectedOption === 'my-listings'">
				<div class="card-grid-wrapper"> 
					<listing-card-grid :Listings="owned"/>
				</div>
			</div>

			<div v-else-if="selectedOption === 'favorite-listings'">
				<div class="card-grid-wrapper"> 
					<listing-card-grid :Listings="favorites"/>
				</div>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import UserInfo from '@/components/UserInfo.vue';
import ListingCardGrid from '@/components/Listingcardgrid.vue';
import { getIp } from '@/globalState';
import axios from 'axios';
import PasswordChangeForm from '@/components/PasswordChangeForm.vue';

export default {
	components: {
		ListingCardGrid,
		UserInfo,
		PasswordChangeForm
	},
	data() {
		return {
			selectedOption: 'user-info',
			favorites: [],
			owned: [],
		};
	},
	setup() {
		const { serverIP } = getIp();
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
			alert(error.response.data);
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
			alert(error.response.data);
		});
	}
};
</script>

<style scoped>
.sidebar {
  background-color: #f1f1f1;
  padding: 1rem;
  height: calc(100vh - 248px);;
}

.container {
  display: grid;
  grid-template-columns: auto 1fr;
  justify-content: center;
  align-items: center;
  align-items: flex-start;
  overflow: auto;
}

.edit-wrapper {
	display: flex;
	flex-direction: row;
	justify-content: center;
  	align-items: flex-start;
}

.content {
	display: flex;
  justify-content: center;
  overflow: auto;
  height: calc(100vh - 20px);
  overflow: scroll;
  
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
	border-radius: 0.5rem;
}

nav li.active {
	background-color: #0718c4;
	color: white;
	border-radius: 0.5rem;
}

@media (max-width: 767px) {
  .container {
    grid-template-columns: 1fr;
  }

  .sidebar {
    height: auto;
    width: 100%;
  }

  .edit-wrapper {
	padding-top: 0px;
	flex-direction: column;
}

  nav ul {
    flex-direction: row;
    justify-content: space-around;
  }

  .content {
    height: auto;
  }

  .card-grid-wrapper {
    padding-top: 80px;
  }
}
</style>