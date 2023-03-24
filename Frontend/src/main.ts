import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n';
import en from './locales/en.json';
import nb from './locales/nb.json';
import nn from './locales/nn.json';
import ja from './locales/ja.json';

import App from './App.vue'
import router from './router'

import './assets/main.css'

const app = createApp(App)

const i18n = createI18n({
    locale: navigator.language.substring(0, 2) || 'en',
    fallbacklocale: 'en',
    messages: {
        en,
        nb,
        nn,
        ja
    }
});

app.use(i18n)


app.use(createPinia())
app.use(router)

app.mount('#app')
