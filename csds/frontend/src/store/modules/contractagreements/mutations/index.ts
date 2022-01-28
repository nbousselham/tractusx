import { MutationTree } from "vuex";
import { contractAgreementsState } from "../iContractAgreementStates";
import {
  SET_CONTRACT_AGREEMENTS,
  SET_CONTRACT_AGREEMENTS_LOADING,
} from "./mutation-types";

export const mutations: MutationTree<contractAgreementsState> = {
  [SET_CONTRACT_AGREEMENTS](state, payload: Array<any>) {
    state.contractAgreementsList = payload;
  },
  [SET_CONTRACT_AGREEMENTS_LOADING](state, value: boolean) {
    state.isContractAgreementsLoading = value;
  },
};
