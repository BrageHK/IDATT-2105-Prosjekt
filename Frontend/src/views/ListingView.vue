<template>
    <div class="listing-view">
      <div class="listing-image">
        <img v-bind:src="image" alt="" />
      </div>
      <div class="listing-details">
        <h2>{{ name }}</h2>
        <span>{{ price + ' NOK' }}</span>
        <div class="listing-actions">
          <button @click="$emit('add-to-cart', { name, price })">Add to Cart</button>
          <img
            v-bind:src="favoriteIcon"
            @click.stop="toggleFavorite"
            alt="Add to favorites"
            class="favorite-icon"
          />
        </div>
        <p>{{ description }}</p>
      </div>
    </div>
  </template>
  
  <script lang="ts">
    import axios from 'axios';
    import heartFilled from '@/assets/images/heartFilled.svg'
    import heartOutline from '@/assets/images/heartOutline.svg'
  export default {
    name: 'ListingView',
    
    data() {
        return {
            id : this.$route.params.id,
            name: '',
            price: '',
            image: '',
            description: '',
            isFavorite: false,
            favoriteIcon: heartOutline,
            favoriteIconFilled: heartFilled
        }
    },
    methods: {
        toggleFavorite() {
            this.isFavorite = !this.isFavorite;
            this.favoriteIcon = this.isFavorite ? this.favoriteIconFilled : heartOutline;
            console.log(this.isFavorite)
        }
    },
    created() {
        axios.get(`https://fakestoreapi.com/products/${this.id}`)
        .then(response => {
            this.name = response.data.title
            this.price = response.data.price
            this.image = response.data.image
            this.description = response.data.description
            console.log(response.data)
            console.log(this.name)
            console.log(this.price)
            console.log(this.image)
            console.log(this.description)
        })
        .catch(error => {
        console.error(error);
        });
    }}

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
  width: 100%;
  display: flex;
  justify-content: center; 

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
  