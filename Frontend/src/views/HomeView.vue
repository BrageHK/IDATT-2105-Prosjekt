<script lang="ts">

import Listingcardgrid from '@/components/Listingcardgrid.vue';
import axios from 'axios';
import { useGlobalState } from '@/globalState';
import { onMounted, onUnmounted, ref } from 'vue'

export default {
	name: 'HomeView',
	components: {
		Listingcardgrid,
	},
	setup() {
		const { serverIP } = useGlobalState();
		
		const Listing = ref<Array<any>>([]);
		const isLoading = ref(false);
		const page = ref(0);
		
		const token = localStorage.getItem('authToken');
		const authorization = token ? {headers: {Authorization: 'Bearer ' + token}} : {};

		const loadMore = () => {
			if (!isLoading.value) {
				isLoading.value = true;
				axios
					.post( serverIP.value + `/api/listing/search`, {
						"filters": [],
						"sorts": [],
						"page": page.value,
						"size": 15
					},
					authorization
					)
					.then((response) => {
						page.value++;
						Listing.value = [...Listing.value, ...response.data.content];
						isLoading.value = false;
					})
					.catch((error) => {
						console.error(error);
						isLoading.value = false;
					});
			}
		};

		const observeBottom = () => {
			const bottomElement = document.querySelector("#bottom-element");
			const observer = new IntersectionObserver(
				(entries) => {
					entries.forEach((entry) => {
						if (entry.isIntersecting) {
							loadMore();
						}
					});
				},
				{ threshold: 1 }
			);
			if (bottomElement) {
				observer.observe(bottomElement);
			}
		};

		onMounted(async () => {
			await observeBottom();
			loadMore();
		});

		onUnmounted(() => {
			const bottomElement = document.querySelector("#bottom-element");
			const observer = new IntersectionObserver(() => {}, { threshold: 1 });
			if (bottomElement) {
				observer.unobserve(bottomElement);
			}
		});
		return {
			Listing,
		};
	},
};

</script>

<template>
	<main>
		<div class="grid-wrapper">
			<listingcardgrid :Listings="Listing"/>
			
		</div>
		<div id="bottom-element"></div>
	</main>
</template>

<style scoped>

.grid-wrapper{
	--max-width: 1524px; 
	max-width: var(--max-width);
	display: flex;
	justify-content: center;
	margin: 0 auto;
}
</style>