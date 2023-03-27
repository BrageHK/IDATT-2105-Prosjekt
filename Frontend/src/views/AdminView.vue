<template>
	<div class="sidebar">
		<nav>
			<ul>
				<li @click="selectedOption = 'user-accounts'">{{ $t('userAccounts') }}</li>
				<li @click="selectedOption = 'edit-categories'">{{ $t('editCategories') }}</li>
                <li @click="selectedOption = 'register-admin'">{{ $t('registerAdmin') }}</li>
			</ul>
		</nav>
		<div v-if="selectedOption === 'user-accounts'">
            <UserAccounts />
		</div>
		<div v-else-if="selectedOption === 'edit-categories'">
            <EditCategory />
		</div>
        <div v-else-if="selectedOption === 'register-admin'">
            <RegisterAdmin />
        </div>
	</div>
</template>
<script lang="ts">


import RegisterAdmin from '@/components/RegisterAdmin.vue';
import EditCategory from '@/components/EditCategory.vue';
import UserAccounts from '@/components/UserAccounts.vue';


export default {
	components: {
		RegisterAdmin,
        EditCategory,
        UserAccounts,
	},
	data() {
		return {
			selectedOption: 'user-accounts',
		};
	},
};

</script>
<style scoped>
.sidebar {
	display: flex;
	flex-direction: row;
}

nav {
    background-color: #f1f1f1;
    overflow: hidden;
    display: flex;
    flex-direction: column;
}

nav ul {
    margin: 0;
    padding: 0;
    list-style: none;
    display: flex;
    flex-direction: column;
}

nav li {
    text-align: center;
    padding: 14px 16px;
    cursor: pointer;
}

nav li:hover {
    background-color: #ddd;
}

nav li.active {
    background-color: #4CAF50;
    color: white;
}
</style>
