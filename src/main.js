import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueRouter from 'vue-router'
import router from "./router/index"
import store from "../src/store/index"

Vue.use(VueRouter)

axios.defaults.baseURL = "http://121.41.165.32:8088"
Vue.prototype.$http = axios

Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  render: h => h(App),
  router: router,
  store
}).$mount('#app')
