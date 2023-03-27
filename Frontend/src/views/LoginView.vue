<template>
	<div class="container">
		<component :is="activeComponent" @switch-component="switchComponent" />
		<p>
			{{ activeComponent === 'Login' ? $t('noAccount') : $t('alreadyHaveAccount') }}
			<a href="#" @click.prevent="switchComponent">
				{{ activeComponent === 'Login' ? $t('createAccount') : $t('signIn')}}
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
	
	border-radius: 5px;
	padding: 20px;
	
}

a {
	color: #007bff;
	text-decoration: underline;
	cursor: pointer;
}
</style>
  