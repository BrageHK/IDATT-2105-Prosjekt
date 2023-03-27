<template>
	<div class="create-listing listing-view">
		<h1>{{ $t('createListing') }}</h1>
		<form @submit.prevent="submitForm">
			<div class="listing-details">
				<div>
					<label for="name">{{ $t('briefDescription') }}:</label>
					<input type="text" id="name" v-model="listingData.briefDescription" required />
				</div>
				<div>
					<label for="price">{{ $t('price') }}:</label>
					<input type="number" id="price" v-model="listingData.price" step="any" required />
				</div>
				<div>
					<label for="image">{{ $t('image') }}:</label>
					<div class="image-previews">
						<img v-for="(preview, index) in imagePreviews" :key="index" :src="preview" alt="Preview" />
					</div>
					<input type="file" id="image" multiple @change="onImageChange" required />
				</div>
				<div>
					<label for="description">{{ $t('description') }}:</label>
					<textarea id="description" v-model="listingData.description" required></textarea>
				</div>
				<div>
					<label for="category">{{ $t('category') }}:</label>
					<select v-model="listingData.category" required >
        			<option disabled value="">{{ $t('selectCategory') }}</option>
        			<option v-for="category in categories" :key="category.id" :value="category.id">
		  			{{ $t(category.name) }}
					</option>		
					</select>
				</div>
				<div>
					<label for="address">{{ $t('address') }}:</label>
					<input type="text" id="address" v-model="listingData.address" required />
				</div>
				<div>
					<label for="latitude">{{ $t('latitude') }}:</label>
					<input type="number" id="latitude" v-model="listingData.latitude" step="any" required />
				</div>
				<div>
					<label for="longitude">{{ $t('longitude') }}:</label>
					<input type="number" id="longitude" v-model="listingData.longitude" step="any" required />
				</div>
				<button type="submit">{{ $t('createListing') }}</button>
			</div>
		</form>
	</div>
</template>

<script lang="ts">
	import axios from 'axios';
	import { getIp, getCategories } from '@/globalState';
	import router from '@/router';

	interface category {
		id: number;
		name: string;
	}

	export default {
		name: 'CreateListingView',
		data() {
			return {
				listingData: {
					briefDescription: '',
					description: '',
					category: '',
					address: '',
					latitude: '',
					longitude: '',
					price: '',
				},
				files: [] as File[],
				imagePreviews: [] as string[],
				categories: [] as category[],
			};
		},
		async mounted() {
			
			try {
				const response = await axios.get(this.serverIP + '/api/category/getAllCategories');
				this.categories = response.data;
			} catch (error) {
				console.error('Error fetching categories:', error);
			}
		},
		setup() {
			const { serverIP } = getIp();

			return {
				serverIP,
			};
		},
		methods: {
			onImageChange(event: Event) {
				const target = event.target as HTMLInputElement;
				if (!target.files) return;
				this.files = Array.from(target.files);
				this.imagePreviews = this.files.map(file => URL.createObjectURL(file));
			},
			async submitForm() {
				console.log(this.listingData.category);
				try {

					const formData = new FormData();
					for (let i = 0; i < this.files.length; i++) {
						formData.append('files', this.files[i]);
					}

					const listingJson = JSON.stringify(this.listingData);
					formData.append('listing', listingJson);

					const token = localStorage.getItem('authToken');
					const response = await axios.post(this.serverIP + '/api/listing/create', formData, {
						headers: {
							'Content-Type': 'multipart/form-data',
							'Authorization': `Bearer ${token}`,
						},
					});
					router.push('/listing/' + response.data);
					console.log(response.data);
				} catch (error) {
					console.error('Error submitting the form:', error);
				}
			},
		},
	};
</script>

<style scoped>
	.create-listing {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 1rem;
		margin-bottom: 1rem;
		max-width: 1024px;
		margin: 0 auto;
	}
	.create-listing h1 {
		font-size: 2rem;
		margin-bottom: 1rem;
	}
	.create-listing form {
		width: 100%;
		max-width: 700px;
		display: flex;
		flex-direction: column;
		margin-left: auto;
		margin-right: auto;
	}
	.create-listing label {
		font-weight: bold;
		margin-top: 1rem;
		font-size: 1.1rem;
	}
	.create-listing input,
	.create-listing textarea {
		width: 100%;
		margin-top: 0.5rem;
		padding: 0.5rem;
		border: 1px solid #ccc;
		border-radius: 4px;
		font-size: 1rem;
	}
	.create-listing textarea {
		resize: vertical;
	}
	.create-listing button {
		background-color: #0718c4;
		border: none;
		color: white;
		padding: 0.5rem 1rem;
		text-align: center;
		text-decoration: none;
		display: inline-block;
		font-size: 1rem;
		border-radius: 0.25rem;
		cursor: pointer;
		margin-top: 1rem;
		transition: background-color 0.3s;
	}
	.create-listing button:hover {
		background-color: #0a43d1;
	}
	.image-previews {
		display: flex;
		flex-wrap: wrap;
		gap: 1rem;
		margin-bottom: 1rem;
	}
	.image-previews img {
		max-width: 150px;
		max-height: 150px;
		object-fit: cover;
		border-radius: 4px;
	}
	.listing-details {
		width: 100%;
		padding-top: 1rem;
		text-align: center;
	}
</style>
