<template>
    <div class="form-container">
        <h1 class="form-container__title">{{ $t("editCategories") }}</h1>
        <ul class="list">
            <li class="list__item" v-for="category in categories" :key="category.id">{{ $t(category.name) }}
                <button @click="removeCategory(category.id)">{{ $t("delete") }}</button>
            </li>
        </ul>
        <form @submit.prevent="addCategory">
            <input v-model="newCategoryName" placeholder="$t('enterCategoryName')" />
            <button type="submit">{{ $t("addCategory") }}</button>
        </form>
    </div>
</template>

<script lang="ts">

import { getIp } from '@/globalState';
import axios from 'axios';

interface category {
    id: number;
    name: string;
}

export default {
    name: 'EditCategory',
    setup() {
        const { serverIP } = getIp();
        return {
            serverIP,
        };
    },
    data() {
        return {
            categories: [] as category[],
            newCategoryName: ""
        };
    },
    mounted() {
        this.fetchCategories();
    },
    methods: {
        async fetchCategories(){
            try {
     
                const response = await axios.get(this.serverIP + "/api/category/getAllCategories");
                this.categories = response.data;
            } catch (error) {
                console.error(error);
            }
        },
        async addCategory() {
            try {
                const token = localStorage.getItem("authToken");
                await axios.post(this.serverIP + `/api/category/addCategory/${this.newCategoryName}`, {}, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                this.newCategoryName = "";
                await this.fetchCategories();
                } catch (error) {
                console.error(error);
            }
        },
        async removeCategory(id: number) {
            if (confirm("Are you sure you want to delete this category?")) {
                try {
                    const token = localStorage.getItem("authToken");
                    await axios.get(this.serverIP + `/api/category/removeCategory/${id}`, {
                        headers: {
                            Authorization: `Bearer ${token}`,
                        },
                    });
                    await this.fetchCategories();
                } catch (error){
                    console.error(error);
                }
            }
        },
    },
}

</script>


<style scoped>
.form-container {
  max-height: 60vh; 
  display: flex;
  flex-direction: column;
  background-color: #f1f1f1;
  border-radius: 0.5rem;

}

.list {
  overflow-y: auto;
  flex-grow: 1; 
  margin-bottom: 1rem;
  padding: 0;
  list-style-type: none;
  border-radius: 0.5rem;
}

.list__item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 1rem;
  border-bottom: 1px solid #ccc;

}

form {
  display: flex;
}

input {
  flex-grow: 1;
  margin-right: 1rem;
}

button {
  cursor: pointer;
}
</style>