<template>
	<header>
		<Navbar />
	</header>
	<main >
		<Suspense>
      		<template #default>
        		<RouterView />
      		</template>
      		<template #fallback>
        		<div>{{ $t('loading') }}...</div>
      		</template>
    	</Suspense>
	</main>
	<v-footer>
		<div class="wrapper">
			<p>Copyright © {{ new Date().getFullYear() }} Handel uten Hemninger</p>
		</div>
	</v-footer>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import Navbar from './components/Navbar.vue';
import { Suspense } from 'vue';

</script>

<style scoped>

@import './assets/main.css';

	main {
		padding-top: 100px;
		min-height: calc(100vh - 215px);
	}

	v-footer {
		background-color: #0718c4;
		color: white;
		height: 100px;
		display: flex;
		align-items: center;
		margin-top: auto;
		bottom: 0; 
	}

	v-footer .wrapper {
		max-width: 1200px;
		margin: 0 auto;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	v-footer p {
		margin: 0;
		font-size: 1.2rem;
	}

	@media (min-width: 1024px) {
		header {
			display: flex;
			align-items: center;
			padding-right: calc(var(--section-gap) / 2);
		}

		header .wrapper {
			display: flex;
			align-items: center;
			justify-content: space-between;
			flex-wrap: wrap;
		}

		nav {
			margin-left: auto;
			font-size: 1rem;
		}
	}
</style>