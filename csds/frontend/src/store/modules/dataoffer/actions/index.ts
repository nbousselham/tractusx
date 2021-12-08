import { ActionTree } from "vuex";
import DataOfferService from "@/common/services/dataoffer/DataOfferService";
import { dataOfferState } from "../iDataOfferStates";
import { GET_DATA_OFFERS } from "./action-types";
import {
  SET_DATA_OFFERS,
  SET_DATA_OFFERS_LOADING,
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
        console.log("Data Offers");
        console.log(filteredDataOffersList);
      })
      .catch((error) => {
        commit(SET_DATA_OFFERS_LOADING, true);
        throw error;
      });
  },
};
