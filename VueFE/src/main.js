import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/store'
import './styles/variables.css'
import './styles/scrollbar.css'
import './styles/dropdown.css'

const app = createApp(App)

app.use(store)
app.use(router)

app.config.productionTip = true

app.mount('#app')

