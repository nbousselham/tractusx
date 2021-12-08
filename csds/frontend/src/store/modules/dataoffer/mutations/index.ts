import { MutationTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import { SET_DATA_OFFERS, SET_DATA_OFFERS_LOADING } from "./mutation-types";
import { iDataOffers } from "@/common/interfaces/dataOffers/IDataOffers";

export const mutations: MutationTree<dataOfferState> = {
  [SET_DATA_OFFERS](state, payload: Array<iDataOffers>) {
    state.dataOffersList = payload;
  },
  [SET_DATA_OFFERS_LOADING](state, value: boolean) {
    state.isDataOffersLoading = value;
  },
};
