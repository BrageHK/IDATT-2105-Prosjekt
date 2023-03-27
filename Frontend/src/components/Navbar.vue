<template>
	<div class="wrapper">
	  <nav>
		<RouterLink to="/"><img :src="logoPath" alt="My Logo" class="logo" /></RouterLink>
		<div class="nav-links">
		  <div v-if="isAuthenticated"><router-link class="nav-link" to="/create-listing">{{ $t('createListing') }}</router-link></div>
		  <div v-if="isAuthenticated"><router-link class="nav-link" to="/user">{{ $t('myPage') }}</router-link></div>
		  <div v-if="isAuthenticated && isAdmin"><router-link class="nav-link" to="/admin">{{ $t('admin') }}</router-link></div>
		  <div v-if="!isAuthenticated"><router-link class="nav-link" to="/login">{{ $t('signIn') }}</router-link></div>
		  <div v-if="isAuthenticated" class="nav-link" @click="logout">{{ $t('signOut') }}</div>
		  <div>
			<select @change="changeLanguage" v-model="selectedLanguage">
			  <option value="en">English</option>
			  <option value="nb">Bokmål</option>
			  <option value="nn">NyNorsk</option>
			  <option value="ja">日本</option>
			</select>
		  </div>
		</div>
		
		<button class="hamburger hamburger-menu" @click="toggleNavLinks">
		  <span></span>
		  <span></span>
		  <span></span>
		</button>
	  </nav>
	</div>
  </template>

<script lang="ts">
	import logoPath from '@/assets/images/logo.svg';
	import router from '@/router';
	import { ref } from 'vue';
	import { getIp } from '@/globalState';
	import axios from 'axios';

	export default {
		name: 'Navbar',
		data() {
			return {
				selectedLanguage: this.$i18n.locale,
				isAuthenticated: ref(!!localStorage.getItem('authToken')),
				logoPath: logoPath,
				isAdmin: ref(false),
				navLinksVisible: ref(false),
			};
		},
		setup() {
			const { serverIP } = getIp();
			return {
				serverIP,
			};
		},
		methods: {
    		changeLanguage() {
      			this.$i18n.locale = this.selectedLanguage;
    		},
			logout() {
				localStorage.removeItem('authToken');
				window.location.reload();
				router.push('/');
			},
			toggleNavLinks() {
      			this.navLinksVisible = !this.navLinksVisible;
      			if (this.navLinksVisible) {
        			document.querySelector('.nav-links')?.classList.add('mobile');
      			} else {
        			document.querySelector('.nav-links')?.classList.remove('mobile');
      			}
    		},
			
  		},
		async mounted() {
			try
				{	
					console.log("checkAdmin");
					const token = localStorage.getItem('authToken');
					const response = await axios.get(this.serverIP + "/api/user/getUser/isAdmin", {
						headers: {
							"Authorization": `Bearer ${token}`,
						},
					}
					).then((response) => {
						this.isAdmin = response.data ;
					});
				} catch (error) {
					console.log(error);
				}
		}
	};

	
</script>

<style scoped>
  .logo {
    display: block;
    margin: 0 2rem 0 0;
    height: 75px;
    width: auto;
  }

  nav {
    display: flex;
    align-items: center;
    justify-content: space-between;
    position: fixed;
    height: 100px;
    background-color: #fff;
    top: 0;
    left: 0;
    right: 0;
    z-index: 9999;
    border-bottom: 1px solid #0718c4;
    padding: 0 1rem;
  }

  .nav-links {
    display: flex;
    align-items: center;
  }

  nav .nav-link {
    display: inline-block;
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    margin: 0 1rem;
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
    text-decoration: none;
    background-color: #fff;
    transition: all 0.2s ease;
  }

  nav .nav-link:hover,
  nav .nav-link .router-link-active,
  nav .nav-link.router-link-exact-active {
    background-color: #0718c4;
    color: #fff;
    text-decoration: none;
  }

  nav .nav-link .router-link {
    color: #333;
    text-decoration: none;
  }

  .logo:hover {
    border: 4px solid #0718c4;
    border-radius: 1rem;
  }

  .hamburger {
    display: none;
    cursor: pointer;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
	width: 30px;
    height: 22px;
  }

  .hamburger-menu {
  background-color: transparent;
  border: none;
  padding: 0;
  font-size: inherit;
  margin-top: 0;
  cursor: pointer;
  transition: none;
}

.hamburger-menu:hover {
  background-color: transparent;
  border: none;
}

  .hamburger span {
    display: block;
    width: 100%;
    height: 4px;
    background-color: #333;
    border-radius: 2px;
  }

  @media (max-width: 768px) {
    nav .nav-links {
      display: none;
    }

    nav .nav-link {
      display: block;
      margin: 0.5rem 0;
      text-align: center;
    }

    .nav-links.mobile {
      display: block;
      position: absolute;
      top: 100px;
      left: 0;
      right: 0;
      background-color: #fff;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
      padding: 1rem;
    }

    .hamburger {
      display: flex;
    }
  }
</style>