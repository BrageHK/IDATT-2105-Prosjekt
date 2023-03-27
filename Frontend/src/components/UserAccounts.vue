<template>
    <ul>
        <li v-for="user in userAccounts" :key="user.id">
            {{ user.role }} - {{ user.firstName }} {{ user.lastName }} - {{ user.email }} - {{ user.phoneNumber }} - {{ user.address }}
            <button @click="deleteUser(user.id)">{{ $t('delete') }}</button>
        </li>
    </ul>
</template>

<script lang="ts">

import { getIp } from '@/globalState';
import axios from 'axios';

interface UserAccount {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    role: string;
    phoneNumber: number;
    address: string;
}

export default {
    name: 'UserAccounts',
    setup() {
        const { serverIP } = getIp();
        return {
            serverIP,
        };
    },
    data() {
        return {
            userAccounts: [] as UserAccount[],
        };
    },
    mounted() {
        this.fetchUserAccounts();
    },
    methods: {
        async fetchUserAccounts(){
            const token = localStorage.getItem("authToken")
            axios
            .get(this.serverIP + '/api/user/getAllUsers', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .then((response) => {
                console.log(response.data[0])
                this.userAccounts = response.data;
            })
            .catch((error) => {
                console.error(error);
            });
        },
        async deleteUser(id : number) {
            if (confirm('Are you sure you want to delete this user?')) {
                const token = localStorage.getItem('authToken');
                await axios.delete(this.serverIP + `/api/user/deleteUser/${id}`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                })
                .then(() => {
                    this.userAccounts = this.userAccounts.filter(
                        (user) => user.id !== id
                    );
                })
                .catch((error) => {
                    console.error(error);
                });
            }
        },
    }
}
</script>

<style scoped>
</style>