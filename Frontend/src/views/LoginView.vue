<template>
	<div class="container">
		<component :is="activeComponent" @switch-component="switchComponent" />
		<p>
			{{ activeComponent === 'Login' ? "Har du ikke konto? " : 'Har du allerede konto? ' }}
			<a href="#" @click.prevent="switchComponent">
				{{ activeComponent === 'Login' ? 'Opprett konto' : 'Logg inn' }}
			</a>
		</p>
	</div>
</template>

<script lang="ts">
    import Login from '@/components/Login.vue';
    import Register from '@/components/Register.vue';

    export default {
        components: {
            Login,
            Register,
        },
        data() {
            return {
                activeComponent: 'Login',
            };
        },
        methods: {
            switchComponent() {
                this.activeComponent = this.activeComponent === 'Login' ? 'Register' : 'Login';
            },
        },
    };
</script>

<style scoped>
.container {
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: fit-content;
	margin: 0 auto;
	border: 1px solid #ccc;
	border-radius: 5px;
	padding: 20px;
	background-color: #f9f9f9;
}

a {
	color: #007bff;
	text-decoration: underline;
	cursor: pointer;
}
</style>
  