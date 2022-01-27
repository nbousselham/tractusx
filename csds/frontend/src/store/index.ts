import Vue from "vue";
import Vuex from "vuex";
import DataOfferState from "@/store/modules/dataoffer";
import ContractAgreementsState from "@/store/modules/contractagreements";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    DataOfferState,
    ContractAgreementsState,
  },
});
