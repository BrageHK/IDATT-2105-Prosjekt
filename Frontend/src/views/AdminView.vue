<template>
	<div class="container">
		<nav class="sidebar">
			<ul>
				<li @click="selectedOption = 'user-accounts'" :class="{ active: selectedOption === 'user-accounts' }">{{ $t('userAccounts') }}</li>
                <li @click="selectedOption = 'edit-categories'" :class="{ active: selectedOption === 'edit-categories' }">{{ $t('editCategories') }}</li>
                <li @click="selectedOption = 'register-admin'" :class="{ active: selectedOption === 'register-admin' }">{{ $t('registerAdmin') }}</li>
            </ul>
		</nav>
        <div class="content">
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
.container {
  display: grid;
  grid-template-columns: auto 1fr;
  justify-content: center;
}

.sidebar {
  background-color: #f1f1f1;
  padding: 1rem;
  height: calc(100vh - 248px);
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
  border-radius: 0.5rem;

}

nav li.active {
  background-color: #0718c4;
  color: white;
  border-radius: 0.5rem;

}

.content { 
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem;
  max-height: calc(100vh - 248px);
}

@media (max-width: 767px) {
  .container {
    grid-template-columns: 1fr;
  }

  .sidebar {
    height: auto;
    width: 100%;
  }

  nav ul {
    flex-direction: row;
    justify-content: space-around;
  }

  .content {
    height: auto;
  }
}
</style>
