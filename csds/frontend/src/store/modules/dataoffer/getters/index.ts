import { GetterTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import {
  FETCH_DATA_OFFERS,
  IS_OFFER_LOADING,
  FETCH_USE_CASES,
  FETCH_ORG_ROLES,
} from "./getter-types";

export const getters: GetterTree<dataOfferState, Record<string, never>> = {
  [FETCH_DATA_OFFERS](state) {
    return state.dataOffersList;
  },
  [IS_OFFER_LOADING](state) {
    return state.isDataOffersLoading;
  },
  [FETCH_USE_CASES](state) {
    return state.useCaseList;
  },
  [FETCH_ORG_ROLES](state) {
    return state.organizationRoles;
  },
};
