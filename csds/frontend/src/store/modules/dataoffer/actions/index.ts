import { ActionTree } from "vuex";
import HttpStatusCode from '@/common/util/HttpStatusCode';
import ResponseDataCode from '@/common/util/ResponseDataCode';
import DataOfferService from "@/common/services/dataoffer/DataOfferService";
import { dataOfferState } from "../iDataOfferStates";
import { GET_DATA_OFFERS } from "./action-types";
import { SET_DATA_OFFERS } from "../mutations/mutation-types";
// eslint-disable-next-line
export const actions: ActionTree<dataOfferState, {}> = {
  [GET_DATA_OFFERS]({ commit }) {
    DataOfferService.getDataOfferList()
      .then((res) => {
        const { data } = res;
        const dataOffersList = data.data;
        const filteredDataOffersList = JSON.parse(
          JSON.stringify(dataOffersList)
        );
        commit(SET_DATA_OFFERS, filteredDataOffersList);
        console.log("Data Offers");
        console.log(filteredDataOffersList);
      })
      .catch((error) => {
        console.log(error);
      });
  },
};
