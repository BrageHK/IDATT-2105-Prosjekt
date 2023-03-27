<template>
	<div class="edit-listing listing-view">
		<h1>{{ $t('editListing') }}</h1>
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
                        <h3>{{ $t('existingImages') }}</h3>
                        <div v-for="(preview, index) in existingImages" :key="'existing-' + index" class="image-preview">
                        <img :src="preview" alt="Preview" />
                        <button type="button" @click="deleteImage(index, 'existing')">{{ $t('deleteImage') }}</button>
                        </div>
                    </div>
                    <div class="image-previews">
                        <h3>{{ $t('newImages') }}</h3>
                        <div v-for="(preview, index) in imagePreviews" :key="'new-' + index" class="image-preview">
                        <img :src="preview" alt="Preview" />
                        <button type="button" @click="deleteImage(index, 'new')">{{ $t('delete') }}</button>
                    </div>
                </div>
					<input type="file" id="image" multiple @change="onImageChange"  />
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
				<button type="submit">{{ $t('save') }}</button>
			</div>
		</form>
	</div>
</template>

<script lang="ts">
	import axios from 'axios';
	import { getIp } from '@/globalState';
	import router from '@/router';
import { ref } from 'vue';

	interface category {
		id: number;
		name: string;
	}

	export default {
		name: 'EditListingView',
	
	data() {
		return {
            id: this.$route.params.id,
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
            existingImages: [] as string[],
            imagesToAdd: [] as string[],
            imagesToRemove: [] as string[],
			categories: [] as category[],
			isAdmin: ref(false),
		};
	},
	async mounted() {
		this.checkAdmin();
		this.fetchListingData();
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
		async checkAdmin(){
            try
				{	
					console.log("checkAdmin");
					const token = localStorage.getItem('authToken');
					const response = await axios.get(this.serverIP + "/api/user/getUser/isAdmin", {
						headers: {
							"Authorization": `Bearer ${token}`,
						},
					}
					).then((response) => {
						this.isAdmin = response.data ;
					});
				} catch (error) {
					console.log(error);
				}
        },
		async fetchListingData() {
			try {
				const token = localStorage.getItem('authToken');
				const response = await axios.get(this.serverIP + '/api/listing/' + this.id, {
					headers: {
						'Authorization': `Bearer ${token}`,
					},
				});
                if (!response.data.isCurrentUserOwner && !this.isAdmin) {
                    console.log(response.data.isOwner + ' is not the owner');
                    alert('You are not the owner of this listing!')
                    router.push('/listing/' + this.id);
                }
				this.listingData = response.data
                this.existingImages = Array.from({ length: response.data.numberOfPictures }, (_, index) => this.serverIP + '/api/images/' + this.id + '/' + index);
			} catch (error) {
				console.error('Error fetching the listing data:', error);
			}
		},
        deleteImage(index: number, type: 'existing' | 'new') {
			if ((type === 'existing' && this.existingImages.length <= 1 && this.imagePreviews.length === 0) || 
        		(type === 'new' && this.imagePreviews.length <= 1 && this.existingImages.length === 0)) {
        		alert("You cannot delete the last image.");
        		return;
    		}
            if (type === 'existing') {
                this.imagesToRemove.push(this.existingImages[index]);
                this.existingImages.splice(index, 1);
            } else {
                this.imagesToAdd.splice(index, 1);
                this.imagePreviews.splice(index, 1);
            }
        },
        onImageChange(event: Event) {
				const target = event.target as HTMLInputElement;
				if (!target.files) return;
				this.files = Array.from(target.files);
				this.imagePreviews = this.files.map(file => URL.createObjectURL(file));
		},

		async submitForm() {
			try {
				const token = localStorage.getItem('authToken');

				const response = await axios.put(this.serverIP + '/api/listing/' + this.id + '/edit', this.listingData, {
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`,
                },
                });
            
                const formDataToAdd = new FormData();
                for (let i = 0; i < this.files.length; i++) {
                    formDataToAdd.append('files', this.files[i]);
                }
				if(this.files.length > 0){
					await axios.put(this.serverIP + '/api/listing/' + this.id + '/edit/addPictures', formDataToAdd, {
						headers: {
							'Content-Type': 'multipart/form-data',
							'Authorization': `Bearer ${token}`,
						},
					});
				}
                
				
				this.imagesToRemove.sort((a, b) => {
    				const aIndex = parseInt(a.split('/').pop() as string);
    				const bIndex = parseInt(b.split('/').pop() as string);
    			return bIndex - aIndex;
				});

                for (let image of this.imagesToRemove) {
                    const pictureId = image.split('/').pop();
                    await axios.delete(this.serverIP + '/api/listing/' + this.id + '/edit/removePicture/' + pictureId, {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                        },
                    });
                }
                
                
                router.push('/listing/' + this.id);
                console.log(response.data);
				
			} catch (error) {
				console.error('Error submitting the form:', error);
			}
		},
	},
};
</script>

<style scoped>

.edit-listing {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 1rem;
		margin-bottom: 1rem;
		max-width: 1024px;
		margin: 0 auto;
	}
	.edit-listing h1 {
		font-size: 2rem;
		margin-bottom: 1rem;
	}
	.edit-listing form {
		width: 100%;
		max-width: 700px;
		display: flex;
		flex-direction: column;
		margin-left: auto;
		margin-right: auto;
	}
	.edit-listing label {
		font-weight: bold;
		margin-top: 1rem;
		font-size: 1.1rem;
	}
	.edit-listing input,
	.edit-listing textarea {
		width: 100%;
		margin-top: 0.5rem;
		padding: 0.5rem;
		border: 1px solid #ccc;
		border-radius: 4px;
		font-size: 1rem;
	}
	.edit-listing textarea {
		resize: vertical;
	}
	.edit-listing button {
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
	.edit-listing button:hover {
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
