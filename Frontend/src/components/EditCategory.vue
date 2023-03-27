<template>
    <h1>{{ $t("editCategories") }}</h1>
    <ul>
        <li v-for="category in categories" :key="category.id">{{ $t(category.name) }}
            <button @click="removeCategory(category.id)">{{ $t("delete") }}</button>
        </li>
    </ul>
    <form @submit.prevent="addCategory">
        <input v-model="newCategoryName" placeholder="Enter new category name" />
        <button type="submit">{{ $t("addCategory") }}</button>
    </form>
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
</style>