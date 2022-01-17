import Vue from "vue";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";
import "@mdi/font/css/materialdesignicons.css";
import FileUploadIcon from "@/components/icons/FileUploadIcon.vue";
import InfoIcon from "@/components/icons/InfoIcon.vue";
import EditIcon from "@/components/icons/EditIcon.vue";
import DuplicateIcon from "@/components/icons/DuplicateIcon.vue";
import DeleteIcon from "@/components/icons/DeleteIcon.vue";

Vue.use(Vuetify);

const opts = {
  icons: {
    values: {
      fileUploadIcon: {
        component: FileUploadIcon,
      },
      infoIcon: {
        component: InfoIcon,
      },
      editIcon: {
        component: EditIcon,
      },
      duplicateIcon: {
        component: DuplicateIcon,
      },
      deleteIcon: {
        component: DeleteIcon,
      },
    },
  },
};

export default new Vuetify(opts);
