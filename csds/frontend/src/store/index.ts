import Vue from "vue";
import Vuex from "vuex";
import DataOfferState from "@/store/modules/dataoffer";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    DataOfferState,
  },
});
