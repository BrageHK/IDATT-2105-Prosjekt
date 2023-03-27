<template>
    <div class="form-container">
        <h2 class="form-container__title">{{ $t('userAccounts') }}</h2>
        <table class="user-table">
            <thead>
                <tr>
                    <th>{{ $t('role') }}</th>
                    <th>{{ $t('name') }}</th>
                    <th>{{ $t('email') }}</th>
                    <th>{{ $t('phone') }}</th>
                    <th>{{ $t('address') }}</th>
                    <th>{{ $t('delete') }}</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in userAccounts" :key="user.id">
                    <td :data-label="$t('role')">{{ user.role }}</td>
                    <td :data-label="$t('name')">{{ user.firstName }} {{ user.lastName }}</td>
                    <td :data-label="$t('email')">{{ user.email }}</td>
                    <td :data-label="$t('phone')">{{ user.phoneNumber }}</td>
                    <td :data-label="$t('address')">{{ user.address }}</td>
                    <td :data-label="$t('delete')"><button @click="deleteUser(user.id)">{{ $t('delete') }}</button></td>
                </tr>
            </tbody>
        </table>
    </div>
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
                alert(error.response.data);
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
                    alert(error.response.data);
                });
            }
        },
    }
}
</script>

<style scoped>
.form-container {
  max-height: 60vh;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  background-color: #f1f1f1;
  border-radius: 0.5rem;
  padding: 1rem;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  border-radius: 0.5rem;
}

.user-table th, .user-table td {
  border: 1px solid #ccc;
  padding: 0.5rem;
  text-align: left;
  background-color: #fff;
}

.user-table th {
  background-color: #fff;
}




button {
  cursor: pointer;
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

@media screen and (max-width: 768px) {
  .user-table {
    border: 0;
  }

  .user-table thead {
    clip: rect(0 0 0 0);
    height: 1px;
    margin: -1px;
    overflow: hidden;
    padding: 0;
    position: absolute;
    width: 1px;
  }

  .user-table tr {
    border-bottom: 1px solid #ccc;
    display: block;
    margin-bottom: 0.5rem;
  }

  .user-table td {
    border: none;
    border-bottom: 1px solid #ccc;
    display: block;
    font-size: 0.8em;
    text-align: right;
  }

  .user-table td:before {
    content: attr(data-label);
    float: left;
    font-weight: bold;
    text-transform: uppercase;
  }

  .user-table td:last-child {
    border-bottom: 0;
  }
}
</style>