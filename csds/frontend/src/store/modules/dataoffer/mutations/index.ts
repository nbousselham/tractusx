import { MutationTree } from "vuex";
import { dataOfferState } from "../iDataOfferStates";
import {
  SET_DATA_OFFERS,
  SET_DATA_OFFERS_LOADING,
  SET_USE_CASES,
  SET_ORG_ROLES,
  SET_ORGS_BY_ROLE,
  ADD_ALL_ROLES,
  REMOVE_ALL_ROLES,
  SET_DATA_OFFER_FILE,
  IS_NEW_DATA_OFFER_LOADING,
  SET_FILE_ERROR,
  SET_CREATE_OFFER_ERROR,
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
  [SET_DATA_OFFER_FILE](state, payload) {
    state.dataOfferFile = payload;
  },
  [ADD_ALL_ROLES](state) {
    const orgRoles = state.organizationRoles;
    orgRoles.forEach((orgRole) => {
      state.selectedOrgRoles.push(orgRole);
    });
    state.organizationRoles = [];
  },
  [REMOVE_ALL_ROLES](state) {
    const selectedOrgRoles = state.selectedOrgRoles;
    selectedOrgRoles.forEach((selectedOrgRole) => {
      state.organizationRoles.push(selectedOrgRole);
    });
    state.selectedOrgRoles = [];
  },
  [SET_ORGS_BY_ROLE](state, payload) {
    state.orgsByRole = payload;
  },
  [IS_NEW_DATA_OFFER_LOADING](state, value: boolean) {
    state.isNewDataOfferLoading = value;
  },
  [SET_FILE_ERROR](state, value: boolean) {
    state.isFileError = value;
  },
  [SET_CREATE_OFFER_ERROR](state, value: boolean) {
    state.isCreateOfferError = value;
  },
};
