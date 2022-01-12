import { GetterTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import {
  FETCH_DATA_OFFERS,
  IS_OFFER_LOADING,
  FETCH_USE_CASES,
  FETCH_ORG_ROLES,
  FETCH_ORGS_BY_ROLE,
  FETCH_SELECTED_ORG_ROLES,
  GET_DATA_OFFER_FILE,
  IS_NEW_OFFER_LOADING,
  IS_FILE_ERROR,
  IS_CREATE_OFFER_ERROR,
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
  [FETCH_SELECTED_ORG_ROLES](state) {
    return state.selectedOrgRoles;
  },
  [GET_DATA_OFFER_FILE](state) {
    return state.dataOfferFile;
  },
  [FETCH_ORGS_BY_ROLE](state) {
    return state.orgsByRole;
  },
  [IS_NEW_OFFER_LOADING](state) {
    return state.isNewDataOfferLoading;
  },
  [IS_FILE_ERROR](state) {
    return state.isFileError;
  },
  [IS_CREATE_OFFER_ERROR](state) {
    return state.isCreateOfferError;
  },
};
