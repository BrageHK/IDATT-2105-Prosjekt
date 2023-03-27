<template>
    <div class="listing-view">
        <div class="listing-image">
            <button @click="previousImage">&lt;</button>
            <div class="image-container">
                <img v-bind:src="image" alt="" />
                <div v-if="isSold" class="sold-overlay">{{ $t('sold') }}</div>
            </div>
            <button @click="nextImage">></button>
			<h1>{{ (currentImageIndex+1) + "/" + numberOfPictures }}</h1>
        </div>
        <div class="listing-details">
            <h2>{{ briefDescription }}</h2>
            <span v-if="!isSold">{{ price + ' NOK' }}</span>
            <span v-else>{{ $t('itemIsSold') }}</span>
            <p>{{ $t('category') }}: {{ $t(category) }}</p>
            <div class="listing-actions">
                <button v-if="!isowner" @click="">{{ $t('contactSeller') }}</button>
                <button v-if="isowner || isAdmin" @click="editListing">{{ $t('editListing') }}</button>
                <button v-if="isowner || isAdmin" @click="deleteListing">{{ $t('delete') }}</button>
                <button v-if="(isowner || isAdmin) && !isSold" @click="markAsSold">{{ $t('markAsSold') }}</button>
            </div>
            <div class="favorite-wrapper">
                <img v-bind:src="favoriteIcon" @click.stop="toggleFavorite" alt="Add to favorites" class="favorite-icon"/>
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
import { ref } from 'vue';

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
            isAdmin : ref(false),
            isSold: false,
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
                    router.go(-1);
                    //router.push('/');
                } catch (error) {
                    console.error("Error deleting listing:", error);
                }
            }
        },
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
        async markAsSold() {
            try {
                const token = localStorage.getItem("authToken");
                const response = await axios.put(`${this.serverIP}/api/listing/${this.id}/edit/setSold`, null, {
                headers: {
                    "Authorization": `Bearer ${token}`,
                },
                });
                this.isSold = true;
                console.log(response);
            } catch (error) {
                console.error("Error marking listing as sold:", error);
            }
        },
    },
    created() {
        console.log(this.serverIP);

        this.checkAdmin();

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
                this.category = response.data.categoryName;
                this.address = response.data.address;
                this.latitude = response.data.latitude;
                this.longitude = response.data.longitude;
                this.isFavorite = response.data.isFavoriteToCurrentUser;
                this.isowner = response.data.isCurrentUserOwner;
                this.isSold = response.data.isSold;
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

.image-container {
  position: relative;
}

.sold-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 3rem;
  font-weight: bold;
  color: #f00;
}

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
    height: auto;
    width: 100%;
    display: flex;
    justify-content: center;
    position: relative;
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