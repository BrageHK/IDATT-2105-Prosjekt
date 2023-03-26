<template>
    <div class="listing-view">
        <div class="listing-image">
            <button @click="previousImage">&lt;</button>
            <img v-bind:src="image" alt="" />
            <button @click="nextImage">></button>
			<h1>{{ (currentImageIndex+1) + "/" + numberOfPictures }}</h1>
        </div>
        <div class="listing-details">
            <h2>{{ briefDescription }}</h2>
            <span>{{ price + ' NOK' }}</span>
            <p>{{ $t('category') }}: {{ category }}</p>
            <div class="listing-actions">
                <button @click="">{{ $t('contactSeller') }}</button>
                <img v-bind:src="favoriteIcon" @click.stop="toggleFavorite" alt="Add to favorites" class="favorite-icon"/>
                <button v-if="isowner" @click="editListing">{{ $t('editListing') }}</button>
                <button v-if="isowner" @click="deleteListing">{{ $t('delete') }}</button>
            </div>
            <p>{{ description }}</p>
            <p>{{ $t('address') }}: {{ address }}</p>
            <p>{{ $t('latitude') }}: {{ latitude }}</p>
            <p>{{ $t('longitude') }}: {{ longitude }}</p>
        </div>
    </div>
</template>

<script lang="ts">
import axios from 'axios';
import heartFilled from '@/assets/images/heartFilled.svg';
import heartOutline from '@/assets/images/heartOutline.svg';
import { getIp } from '@/globalState';
import router from '@/router';

export default {
    name: 'ListingView',
    setup() {
        const { serverIP } = getIp();
        return {
            serverIP,
        };
    },
    data() {
		const timestamp = Date.now();
        return {
            id: this.$route.params.id,
            briefDescription: '',
            price: '',
            numberOfPictures: 1,
            image: `${this.serverIP}/api/images/${this.$route.params.id}/0?t=${timestamp}`,
            description: '',
            category: '',
            address: '',
            latitude: '',
            longitude: '',
            isowner: false,
            currentImageIndex: 0,
            isFavorite: false,
            favoriteIcon: heartOutline,
            favoriteIconFilled: heartFilled,
        };
    },
    methods: {
        async removeFavorite() {
			try {
				const token = localStorage.getItem("authToken");
				const response = await axios.delete(this.serverIP + "/api/listing/" + this.id + "/removeFavorite", {
				headers: {
					"Authorization": `Bearer ${token}`,
				},
				});
				this.isFavorite = false;
				this.favoriteIcon = heartOutline
				console.log(response);
			} catch (error) {
				console.error("Error removing favorite:", error);
			}
		},
		async addFavorite() {
			try {
				const token = localStorage.getItem("authToken");
				const response = await axios.post(this.serverIP + "/api/listing/" + this.id + "/addFavorite", null, {
				headers: {
					"Authorization": `Bearer ${token}`,
				},
				});
				this.isFavorite = true;
				this.favoriteIcon = heartFilled
				console.log(response);
			} catch (error) {
				console.error("Error adding favorite:", error);
			}
		},
		
		toggleFavorite() {
			console.log('toggle favorite')
			const token = localStorage.getItem('authToken')
			if (!token) {
				router.push('/login')
				return
			}
			this.isFavorite ? this.removeFavorite(): this.addFavorite();
			
		},

        nextImage() {
            if (this.currentImageIndex < this.numberOfPictures - 1) {
                this.currentImageIndex += 1;
            } else {
                this.currentImageIndex = 0;
            }
            const timestamp = Date.now();
        	this.image = `${this.serverIP}/api/images/${this.id}/${this.currentImageIndex}?t=${timestamp}`;
        	console.log(this.currentImageIndex);
            console.log(this.currentImageIndex);
        },
        previousImage() {
            if (this.currentImageIndex > 0) {
                this.currentImageIndex -= 1;
            } else {
                this.currentImageIndex = this.numberOfPictures - 1;
            }
            const timestamp = Date.now();
        	this.image = `${this.serverIP}/api/images/${this.id}/${this.currentImageIndex}?t=${timestamp}`;
        	console.log(this.currentImageIndex);
            console.log(this.currentImageIndex);
        },

        async editListing() {
            router.push(`/listing/${this.id}/edit`);
        },
        async deleteListing() {
            if (confirm("Are you sure you want to delete this listing?")) {
                const token = localStorage.getItem('authToken');
                try {
                    await axios.delete(`${this.serverIP}/api/listing/${this.id}/delete`, {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                        },
                    });
                    router.push('/');
                } catch (error) {
                    console.error("Error deleting listing:", error);
                }
            }
        },
    },
    created() {
        console.log(this.serverIP);

        const token = localStorage.getItem('authToken');

        let authenticate = {};

        if (token) {
            authenticate = {
                headers: {
                    Authorization: 'Bearer ' + token,
                },
            };
        }

        axios
            .get(this.serverIP + '/api/listing/' + this.id, authenticate)
            .then((response) => {
                this.briefDescription = response.data.briefDescription;
                this.price = response.data.price;
                this.numberOfPictures = response.data.numberOfPictures;
                this.description = response.data.description;
                this.category = response.data.category;
                this.address = response.data.address;
                this.latitude = response.data.latitude;
                this.longitude = response.data.longitude;
                this.isFavorite = response.data.isFavoriteToCurrentUser;
                this.isowner = response.data.isCurrentUserOwner;
                if (this.isFavorite) {
                    this.favoriteIcon = this.favoriteIconFilled;
                }

                console.log(response.data);
                console.log(this.briefDescription);
                console.log(this.price);
                console.log(this.image);
                console.log(this.description);
            })
            .catch((error) => {
                console.error(error);
            });
    },
};
</script>

<style scoped>
.listing-view {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 1rem;
    margin-bottom: 1rem;
    max-width: 1524px;
    margin: 0 auto;
}

.listing-image {
    height: 400px;
    width: 100%;
    display: flex;
    justify-content: center;
}

.listing-image img {
    max-width: 100%;
}

.listing-details {
    width: 100%;
    padding-top: 1rem;
    text-align: center;
}

.listing-details h2 {
    font-size: 1.5rem;
    margin: 0.5rem 0;
}

.listing-details span {
    display: block;
    font-size: 1.3rem;
    font-weight: bold;
    margin: 0.5rem 0;
}

.listing-details p {
    font-size: 1.1rem;
    line-height: 1.6;
    margin-bottom: 1rem;
    max-width: 700px;
    margin-left: auto;
    margin-right: auto;
}

.listing-details button {
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
    margin-right: 1rem;
}

.listing-actions {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 1rem;
}

.favorite-icon {
    width: 30px;
    height: 30px;
    margin-left: 1rem;
}
</style>