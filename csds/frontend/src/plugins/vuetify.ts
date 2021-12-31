import Vue from "vue";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";
import "@mdi/font/css/materialdesignicons.css";
import FileUploadIcon from "@/components/FileUploadIcon.vue";

Vue.use(Vuetify);

const opts = {
  icons: {
    values: {
      fileUploadIcon: {
        component: FileUploadIcon,
      },
    },
  },
};

export default new Vuetify(opts);
