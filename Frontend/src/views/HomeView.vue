<script setup lang="ts">
import ShoppingListing from '../components/Listing.vue';
</script>

<template>
  <main>
    <div class="grid">
      <ShoppingListing v-for="id in 20" :id=id />
    </div>
  </main>
</template>

<style scoped>

.grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(calc(20% - 1rem), 1fr));
  grid-gap: 1rem;
  margin-left: auto; 
  margin-right: auto; 
  max-width: 1524px;
}



@media screen and (min-width: 480px) {
  .grid {
    grid-template-columns: repeat(1, minmax(0, 1fr));
  }
}

@media screen and (max-width: 480px) {
  .grid {
    grid-template-columns: repeat(1, minmax(0, 1fr));
  }
}

@media screen and (min-width: 640px) {
  .grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media screen and (min-width: 768px) {
  .grid {
    grid-template-columns: repeat(2, minmax(calc(50% - 0.5rem), 1fr));
  }
}

@media screen and (min-width: 1024px) {
  .grid {
    grid-template-columns: repeat(3, minmax(calc(33.33% - 0.666rem), 1fr));
  }
}

@media screen and (min-width: 1200px) {
  .grid {
    grid-template-columns: repeat(4, minmax(calc(25% - 0.75rem), 1fr));
  }
}

@media screen and (min-width: 1524px) {
  .grid {
    grid-template-columns: repeat(5, minmax(calc(20% - 1rem), 1fr));
  }
}

</style>