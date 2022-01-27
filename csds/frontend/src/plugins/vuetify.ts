import Vue from "vue";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";
import "@mdi/font/css/materialdesignicons.css";
import { cxIcons } from "./icons";

Vue.use(Vuetify);

const opts = {
  icons: {
    values: cxIcons,
  },
};

export default new Vuetify(opts);
