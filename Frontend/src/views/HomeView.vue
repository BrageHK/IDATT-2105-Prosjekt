<script lang="ts">

import Listingcardgrid from '@/components/Listingcardgrid.vue';
import axios from 'axios';
import { getIp, getCategories } from '@/globalState';
import { onMounted, onUnmounted, ref } from 'vue'

export default {
	name: 'HomeView',
	components: {
		Listingcardgrid,
	},
	setup() {
		const { serverIP } = getIp();
		
		const Listing = ref<Array<any>>([]);
		const isLoading = ref(false);
		const page = ref(0);
		const searchText = ref('');
		const selectedCategory = ref("");
    	const categories = getCategories().categories
		
		const sortOptions = ref([
			{ key: "price", direction: "DESC" },
			{ key: "price", direction: "ASC" },
			{ key: "dateCreated", direction: "DESC" },
			{ key: "dateCreated", direction: "ASC" },
		]);
		
		const searchParamOptions = ref([
			"briefDescription",
      		"description",
      		"address",
    	]);

		const selectedSearchParam = ref(searchParamOptions.value[0]);

		const sort = ref(sortOptions.value[0]);
		
		const token = localStorage.getItem('authToken');
		const authorization = token ? {headers: {Authorization: 'Bearer ' + token}} : {};

		const loadMore = () => {
			console.log(sort);
			if (!isLoading.value) {
				isLoading.value = true;
				axios
					.post( serverIP.value + `/api/listing/search`, {
						"filters": [
						{
                			key: selectedSearchParam.value,
                			operator: 'LIKE',
                			field_type: 'STRING',
                			value: searchText.value,
              			},
						{
							key: "category",
							operator: "LIKE",
							field_type: "STRING",
							value: selectedCategory.value,
						},
						],
						"sorts": [
						{
							key: sort.value.key,
							direction: sort.value.direction
						}
						],
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

		const searchHandler = () => {
  			page.value = 0;
  			Listing.value = [];
  			loadMore();
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
			searchText,
			searchHandler,
			selectedCategory,
			categories,
			sort,
			sortOptions,
			selectedSearchParam,
			searchParamOptions,
		};
	},
};

</script>

<template>
	<main>

		<div class="search-wrapper">
		<input type="text" v-model="searchText" @input="searchHandler()" :placeholder="$t('search')+'...'" />  
		<select v-model="selectedSearchParam" @change="searchHandler()">
		<option :value=searchParamOptions[0]>{{ $t('briefDescription') }}</option>
		<option :value=searchParamOptions[1]>{{ $t('description') }}</option>
		<option :value=searchParamOptions[2]>{{ $t('address') }}</option>
		</select>
		</div>

		<select v-model="selectedCategory" @change="searchHandler()">
        <option disabled value="">{{ $t('selectCategory') }}</option>
		<option value="">{{ $t('all') }}</option>
        <option v-for="category in categories" :key="category" :value="category">
		  {{  $t(category) }}
		</option>
		</select>


		<select v-model="sort" @change="searchHandler()">
		<option :value=sortOptions[0]>{{ $t('sortByPriceDESC') }}</option>
		<option :value=sortOptions[1]>{{ $t('sortByPriceASC') }}</option>
		<option :value=sortOptions[2]>{{ $t('sortByDateDESC') }}</option>
		<option :value=sortOptions[3]>{{ $t('sortByDateASC') }}</option>
		</select>

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