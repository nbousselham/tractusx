import { MutationTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import { SET_DATA_OFFERS } from "./mutation-types";

export const mutations: MutationTree<dataOfferState> = {
  [SET_DATA_OFFERS](state, payload) {
    state.dataOffersList = payload;
  },
};
