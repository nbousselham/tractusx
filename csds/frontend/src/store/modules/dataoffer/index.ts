import { Module } from "vuex";
import { getters } from "./getters";
import { actions } from "./actions";
import { mutations } from "./mutations";
import { dataOfferState } from "./iDataOfferStates";

const DataOfferState: Module<dataOfferState, Record<string, never>> = {
  state: {
    dataOffersList: [],
    isDataOffersLoading: false,
    useCaseList: [],
    organizationRoles: [],
    orgsByRole: [],
    selectedOrgRoles: [],
    dataOfferFile: {},
    isNewDataOfferLoading: false,
    isFileError: false,
    isCreateOfferError: false,
  },
  actions,
  getters,
  mutations,
};

export default DataOfferState;
