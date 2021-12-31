import { MutationTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import {
  SET_DATA_OFFERS,
  SET_DATA_OFFERS_LOADING,
  SET_USE_CASES,
  SET_ORG_ROLES,
} from "./mutation-types";
import {
  iDataOffers,
  iUseCase,
  iOrgRoles,
} from "@/common/interfaces/dataOffers/IDataOffers";

export const mutations: MutationTree<dataOfferState> = {
  [SET_DATA_OFFERS](state, payload: Array<iDataOffers>) {
    state.dataOffersList = payload;
  },
  [SET_DATA_OFFERS_LOADING](state, value: boolean) {
    state.isDataOffersLoading = value;
  },
  [SET_USE_CASES](state, payload: Array<iUseCase>) {
    state.useCaseList = payload;
  },
  [SET_ORG_ROLES](state, payload: Array<iOrgRoles>) {
    state.organizationRoles = payload;
  },
};
