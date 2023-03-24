<template>
	<main>
		<div class="grid">
			<listing-card v-for="listing in Listings"
				:key="listing.id"
				:id="listing.id"
				:name="listing.briefDescription"
				:price="listing.price"
				:is-favorited="listing.isFavoriteToCurrentUser">
			</listing-card>
		</div>
	</main>
</template>
  
<script lang="ts">
	import ListingCard from '@/components/Listingcard.vue';
	import type { PropType } from 'vue';
	  
	export default {
		components: { ListingCard },
		props: {
			Listings: {
				type: Array as PropType<Array<{
					id: number;
					briefDescription: string;
					price: number;
					isFavoriteToCurrentUser: boolean;
				}>>,
				required: true,
				default: () => [],
			},
		},
	};
</script>
  
<style scoped>
	.grid {
		display: grid;
		grid-template-columns: repeat(5, minmax(calc(20% - 1rem), 1fr));
		grid-gap: 1rem;
		margin-left: auto; 
		margin-right: auto; 
		max-width: var(--max-width);
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