import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import vuetify from "@/plugins/vuetify";
import Vuelidate from "vuelidate";
import Toasted from "vue-toasted";
import "@/styles/styles.scss";

Vue.use(Vuelidate);
Vue.use(Toasted);
Vue.config.productionTip = false;

new Vue({
  router,
  store,
  vuetify,
  render: (h) => h(App),
}).$mount("#app");
