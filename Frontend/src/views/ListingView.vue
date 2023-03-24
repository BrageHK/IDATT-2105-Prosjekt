<template>
    <div class="listing-view">
        <div class="listing-image">
            <button @click="previousImage">&lt;</button>
            <img v-bind:src="image" alt="" />
            <button @click="nextImage">></button>
        </div>
        <div class="listing-details">
            <h2>{{ briefDescription }}</h2>
            <span>{{ price + ' NOK' }}</span>
            <p>{{ $t('category') }}: {{ category }}</p>
            <div class="listing-actions">
                <button @click="">{{ $t('contactSeller') }}</button>
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
import { useGlobalState } from '@/globalState';
import router from '@/router';

export default {
    name: 'ListingView',
    setup() {
        const { serverIP } = useGlobalState();
        return {
            serverIP,
        };
    },
    data() {
        return {
            id: this.$route.params.id,
            briefDescription: '',
            price: '',
            numberOfPictures: 1,
            image: this.serverIP + '/api/images/' + this.$route.params.id + '/0',
            description: '',
            category: '',
            address: '',
            latitude: '',
            longitude: '',
            currentImageIndex: 0,
            isFavorite: false,
            favoriteIcon: heartOutline,
            favoriteIconFilled: heartFilled,
        };
    },
    methods: {
        toggleFavorite() {
            const token = localStorage.getItem('authToken');

            let action;

            if (this.isFavorite) {
                action = 'removeFavorite'
            } else {
                action = 'addFavorite'
            }
            if (!token) {
                router.push('/login');
            }

            axios
                .get(this.serverIP + '/api/listing/' + this.id + '/' + action, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                })
                .then((response) => {
                    this.isFavorite = !this.isFavorite;
                    this.favoriteIcon = this.isFavorite ? this.favoriteIconFilled : heartOutline;
                    console.log(this.isFavorite);
                })
                .catch((error) => {
                    console.error(error);
                });
        },
        nextImage() {
            if (this.currentImageIndex < this.numberOfPictures - 1) {
                this.currentImageIndex += 1;
            } else {
                this.currentImageIndex = 0;
            }
            this.image = this.serverIP + '/api/images/' + this.id + '/' + this.currentImageIndex;
            console.log(this.currentImageIndex);
        },
        previousImage() {
            if (this.currentImageIndex > 0) {
                this.currentImageIndex -= 1;
            } else {
                this.currentImageIndex = this.numberOfPictures - 1;
            }
            this.image = this.serverIP + '/api/images/' + this.id + '/' + this.currentImageIndex;
            console.log(this.currentImageIndex);
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