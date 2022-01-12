import { ActionTree } from "vuex";
import DataOfferService from "@/common/services/dataoffer/DataOfferService";
import { dataOfferState } from "../iDataOfferStates";
import {
  GET_DATA_OFFERS,
  GET_USE_CASES,
  GET_ORG_ROLES,
  GET_ORGS_BY_ROLE,
  ADD_DATA_OFFER,
} from "./action-types";
import {
  SET_DATA_OFFERS,
  SET_DATA_OFFERS_LOADING,
  SET_USE_CASES,
  SET_ORG_ROLES,
  SET_ORGS_BY_ROLE,
  IS_NEW_DATA_OFFER_LOADING,
  SET_CREATE_OFFER_ERROR,
} from "../mutations/mutation-types";

export const actions: ActionTree<dataOfferState, Record<string, never>> = {
  [GET_DATA_OFFERS]({ commit }) {
    commit(SET_DATA_OFFERS_LOADING, true);
    return DataOfferService.getDataOfferList()
      .then((res) => {
        const { data } = res;
        const dataOffersList = data.data;
        const filteredDataOffersList = JSON.parse(
          JSON.stringify(dataOffersList)
        );
        commit(SET_DATA_OFFERS, filteredDataOffersList);
        commit(SET_DATA_OFFERS_LOADING, false);
      })
      .catch((error) => {
        commit(SET_DATA_OFFERS_LOADING, false);
        throw error;
      });
  },
  [GET_USE_CASES]({ commit }) {
    return DataOfferService.getAllUseCases().then((res) => {
      const { data } = res;
      const useCaseList = data.data;
      commit(SET_USE_CASES, useCaseList);
    });
  },
  [GET_ORG_ROLES]({ commit }) {
    return DataOfferService.getOrgRoles().then((res) => {
      const { data } = res;
      const orgRoles = data.data;
      const withoutRoleObj = {
        id: 10,
        role: "Without Role",
      };
      orgRoles.push(withoutRoleObj);
      commit(SET_ORG_ROLES, orgRoles);
    });
  },
  [GET_ORGS_BY_ROLE]({ commit }, role) {
    return DataOfferService.getOrganizationsByRole(role).then((res) => {
      const { data } = res;
      const orgsByRole = data.data;
      commit(SET_ORGS_BY_ROLE, orgsByRole);
    });
  },
  [ADD_DATA_OFFER]({ commit, dispatch }, offer) {
    commit(IS_NEW_DATA_OFFER_LOADING, true);
    const formDataObj = new FormData();
    formDataObj.append("file", offer.file);
    formDataObj.append("offerRequest", JSON.stringify(offer.offerRequest));
    return DataOfferService.createDataOffer(formDataObj).then((res) => {
        const { data } = res;
        const createOfferResData = data.data;
        if (
          createOfferResData &&
          createOfferResData !== null &&
          createOfferResData.status !== "FAILED"
        ) {
          commit(SET_CREATE_OFFER_ERROR, false);
        } else {
          commit(SET_CREATE_OFFER_ERROR, true);
        }
        commit(IS_NEW_DATA_OFFER_LOADING, false);
        dispatch(GET_DATA_OFFERS);
      })
      .catch((error) => {
        commit(IS_NEW_DATA_OFFER_LOADING, false);
        commit(SET_CREATE_OFFER_ERROR, true);
        throw error;
      });
  },
};
