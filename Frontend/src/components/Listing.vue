<template>
    <div class="shopping-listing" @click="handleClick">
        <img v-bind:src="image" alt="">
        <h2>{{ name }}</h2>
        <span>{{ price + " NOK" }}</span>   
        <img v-bind:src="favoriteIcon" @click.stop="toggleFavorite" alt="Add to favorites" class="favorite-icon">
    </div>
</template>

  <script lang="ts">
  
  import axios from 'axios';
  import heartFilled from '@/assets/images/heartFilled.svg'
  import heartOutline from '@/assets/images/heartOutline.svg'
  import router from '../router'

  export default {
    name: 'Listing',
    props: {
      id: {
        type: Number,
        required: true
      }
    },
    data() {
        return {
            name: '',
            price: '',
            image: '',
            isFavorite: false,
            favoriteIcon: heartOutline,
            favoriteIconFilled: heartFilled
        }
    },
    methods: {
        handleClick() {router.push('/listing/' + this.id) 
        console.log('clicked')
        },
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
            console.log(response.data)
            console.log(this.name)
            console.log(this.price)
            console.log(this.image)
        })
        .catch(error => {
        console.error(error);
        });
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

  h2 {
    height: 70px;
    display: -webkit-box;
    -webkit-line-clamp: 3; 
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
}

  .shopping-listing:hover {
  box-shadow: 0px 0px 10px #0718c4;
}
  
  .shopping-listing img {
    max-width: 100%;
    height: 300px;
    object-fit: contain;
  }
  
  .shopping-listing h2 {
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
