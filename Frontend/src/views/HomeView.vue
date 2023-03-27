<template>
	
		<div class="searchbar-wrapper">
			<div class="search-wrapper">
				<input type="text" v-model="searchText" @input="searchHandler()" :placeholder="$t('search')+'...'" />  
				<select v-model="selectedSearchParam" @change="searchHandler()">
					<option :value=searchParamOptions[0]>{{ $t('briefDescription') }}</option>
					<option :value=searchParamOptions[1]>{{ $t('description') }}</option>
					<option :value=searchParamOptions[2]>{{ $t('address') }}</option>
				</select>
			</div>

			<div class="category-wrapper">
				<select v-model="selectedCategory" @change="searchHandler()">
					<option disabled value=0>{{ $t('selectCategory') }}</option>
					<option value=0>{{ $t('all') }}</option>
					<option v-for="category in categories" :key="category.id" :value="category.id">
					{{  $t(category.name) }}
					</option>
				</select>
			</div>

			<div class="sort-wrapper">
				<select v-model="sort" @change="searchHandler()">
					<option :value=sortOptions[0]>{{ $t('sortByPriceDESC') }}</option>
					<option :value=sortOptions[1]>{{ $t('sortByPriceASC') }}</option>
					<option :value=sortOptions[2]>{{ $t('sortByDateDESC') }}</option>
					<option :value=sortOptions[3]>{{ $t('sortByDateASC') }}</option>
				</select>
			</div>
		</div>

		<div class="grid-wrapper">
			<listingcardgrid :Listings="Listing"/>
		</div>
		<div id="bottom-element"></div>
	
</template>

<script lang="ts">

import Listingcardgrid from '@/components/Listingcardgrid.vue';
import axios from 'axios';
import { getIp, getCategories } from '@/globalState';
import { onMounted, onUnmounted, ref } from 'vue'

interface Filter {
  key: string;
  operator: string;
  field_type: string;
  value: string | number;
}

export default {
	name: 'HomeView',
	components: {
		Listingcardgrid,
	},
	async setup() {
		const { serverIP } = getIp();
		
		const Listing = ref<Array<any>>([]);
		const isLoading = ref(false);
		const page = ref(0);
		const searchText = ref('');
		const selectedCategory = ref(0);
    	const categories = ref<Array<{ id: number; name: string }>>([]);

		async function fetchCategories() {
  			try {
    			const response = await axios.get(serverIP.value + '/api/category/getAllCategories');
    			categories.value = response.data;
    			console.log(response.data);
  			} catch (error) {
    		console.error(error);
  			}
		}
		
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

				
				const filters: Filter[] = [
					{
						key: selectedSearchParam.value,
						operator: 'LIKE',
						field_type: 'STRING',
						value: searchText.value,
					},
				];

				if (selectedCategory.value != 0) {
					filters.push({
						key: 'category',
						operator: 'EQUAL',
						field_type: 'LONG',
						value: selectedCategory.value,
					});
				}

				
				axios
					.post( serverIP.value + `/api/listing/search`, {
						filters,
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
						console.log(response.data);
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
			await fetchCategories();
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


<style scoped>
.grid-wrapper {
    --max-width: 1524px;
    max-width: var(--max-width);
    display: flex;
    justify-content: center;
    margin: 0 auto;
}

.searchbar-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    gap: 16px;
    padding: 16px;
    background-color: #f8f8f8;
    margin-bottom: 16px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search-wrapper,
.category-wrapper,
.sort-wrapper {
    display: flex;
    align-items: center;
    gap: 8px;
}

input[type="text"],
select {
    padding: 8px 12px;
    margin: 0; /* Reset margin */
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: #fff;
    font-size: 16px;
    box-sizing: border-box; /* Fix for width and height */
}

input[type="text"] {
    width: 300px;
}

select {
    width: 250px;
    appearance: none; /* Remove default styling for consistent appearance */
}

/* Media query for mobile devices */
@media (max-width: 767px) {
    input[type="text"] {
        width: 100%;
    }

    select {
        width: 100%;
    }

    .searchbar-wrapper {
        flex-direction: column;
        gap: 8px;
    }

    .search-wrapper,
    .category-wrapper,
    .sort-wrapper {
        width: 100%;
    }
}
</style>