<template>
	<div class="create-listing listing-view">
		<h1>Create Listing</h1>
		<form @submit.prevent="submitForm">
			<div class="listing-details">
				<div>
					<label for="name">Name:</label>
					<input type="text" id="name" v-model="listingData.briefDescription" required />
				</div>
				<div>
					<label for="price">Price:</label>
					<input type="number" id="price" v-model="listingData.price" required />
				</div>
				<div>
					<label for="image">Image:</label>
					<div class="image-previews">
						<img v-for="(preview, index) in imagePreviews" :key="index" :src="preview" alt="Preview" />
					</div>
					<input type="file" id="image" multiple @change="onImageChange" required />
				</div>
				<div>
					<label for="description">Description:</label>
					<textarea id="description" v-model="listingData.description" required></textarea>
				</div>
				<div>
					<label for="category">Category:</label>
					<input type="text" id="category" v-model="listingData.category" required />
				</div>
				<div>
					<label for="address">Address:</label>
					<input type="text" id="address" v-model="listingData.address" required />
				</div>
				<div>
					<label for="latitude">Latitude:</label>
					<input type="number" id="latitude" v-model="listingData.latitude" step="any" required />
				</div>
				<div>
					<label for="longitude">Longitude:</label>
					<input type="number" id="longitude" v-model="listingData.longitude" step="any" required />
				</div>
				<button type="submit">Create Listing</button>
			</div>
		</form>
	</div>
</template>

<script lang="ts">
	import axios from 'axios';
	import { useGlobalState } from '@/globalState';
	import router from '@/router';

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
			};
		},
		setup() {
			const { serverIP } = useGlobalState();
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
				console.log(this.files.length)
				if (this.files.length === 0) {
					alert('Please upload at least one image.');
					return;
				}
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
