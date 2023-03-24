<template>
	<div class="shopping-listing" @click="handleClick">
		<img :src="image" alt="">
		<h2>{{ name }}</h2>
		<span>{{ `${price} NOK` }}</span>   
		<img :src="favoriteIcon" @click.stop="toggleFavorite" alt="Add to favorites" class="favorite-icon">
	</div>
</template>

<script lang="ts">
import heartFilled from '@/assets/images/heartFilled.svg'
import heartOutline from '@/assets/images/heartOutline.svg'
import router from '../router'
import { useGlobalState } from '@/globalState'
import axios from 'axios'

export default {
	name: 'Listing',
	props: {
		id: { type: Number, required: true },
		name: { type: String, required: true },
		price: { type: Number, required: true },
		isFavorited: { type: Boolean, required: true }
	},
	setup() {
		const { serverIP } = useGlobalState()
		return { serverIP }
	},
	data() {
		const image = `${this.serverIP}/api/images/${this.id}/0`
		const isFavorite = this.isFavorited
		const favoriteIcon = isFavorite ? heartFilled : heartOutline
		const favoriteIconFilled = heartFilled
		return { image, isFavorite, favoriteIcon, favoriteIconFilled }
	},
	methods: {
		handleClick() {
			router.push(`/listing/${this.id}`)
		},
		toggleFavorite() {
			const token = localStorage.getItem('authToken')
			if (!token) {
				router.push('/login')
				return
			}
			const action = this.isFavorite ? 'removeFavorite' : 'addFavorite'
			axios.get(`${this.serverIP}/api/listing/${this.id}/${action}`, { headers: { 'Authorization': `Bearer ${token}` } })
				.then(() => {
					this.isFavorite = !this.isFavorite
					this.favoriteIcon = this.isFavorite ? this.favoriteIconFilled : heartOutline
				})
				.catch(console.error)
		}
	}
}
</script>

<style scoped>
.shopping-listing {
	display: flex;
	flex-direction: column;
	align-items: center;
	border: 1px solid #ccc;
	padding: 1rem;
	margin-bottom: 1rem;
	border-radius: 1rem;
	cursor: pointer;
}

.shopping-listing:hover {
	box-shadow: 0px 0px 10px #0718c4;
}

.shopping-listing img {
	max-width: 100%;
	height: 300px;
	object-fit: contain;
}

h2 {
	height: 70px;
	display: -webkit-box;
	-webkit-line-clamp: 3; 
	-webkit-box-orient: vertical;
	overflow: hidden;
	text-overflow: ellipsis;
	font-size: 1.2rem;
	margin: 0.5rem 0;
}

.shopping-listing span {
	font-size: 1.1rem;
	font-weight: bold;
	margin: 0.5rem 0;
}

.shopping-listing img.favorite-icon {
	width: 30px;
	height: 30px;
}
</style>