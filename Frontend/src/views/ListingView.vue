<template>
    <div class="listing-view">
      <div class="listing-image">
        <img v-bind:src="image" alt="">
      </div>
      <div class="listing-details">
        <h2>{{ name }}</h2>
        <span>{{ price }}</span>
        <p>{{ description }}</p>
        <button @click="$emit('add-to-cart', { name, price })">Add to Cart</button>
      </div>
    </div>
  </template>
  
  <script lang="ts">
    import axios from 'axios';
  export default {
    name: 'ListingView',
    
    data() {
        return {
            id : this.$route.params.id,
            name: '',
            price: '',
            image: '',
            description: ''
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
    flex-direction: row;
    align-items: flex-start;
    border: 1px solid #ccc;
    padding: 1rem;
    margin-bottom: 1rem;
  }
  
  .listing-image {
    width: 50%;
  }
  
  .listing-image img {
    max-width: 100%;
  }
  
  .listing-details {
    width: 50%;
    padding-left: 1rem;
  }
  
  .listing-details h2 {
    font-size: 1.5rem;
    margin: 0.5rem 0;
  }
  
  .listing-details span {
    font-size: 1.3rem;
    font-weight: bold;
    margin: 0.5rem 0;
  }
  
  .listing-details p {
    font-size: 1.1rem;
    line-height: 1.6;
    margin-bottom: 1rem;
  }
  
  .listing-details button {
    background-color: #4CAF50;
    border: none;
    color: white;
    padding: 0.5rem 1rem;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 1rem;
    border-radius: 0.25rem;
    cursor: pointer;
  }
  </style>
  