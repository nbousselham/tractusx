import { GetterTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import { FETCH_DATA_OFFERS } from "./getter-types";

export const getters: GetterTree<dataOfferState, Record<string, never>> = {
  [FETCH_DATA_OFFERS](state) {
    return state.dataOffersList;
  },
};
