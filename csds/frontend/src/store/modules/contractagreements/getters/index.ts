import { GetterTree } from "vuex";
import { contractAgreementsState } from "../iContractAgreementStates";
import {
  FETCH_CONTRACT_AGREEMENTS,
  IS_CONTRACT_AGREEMENTS_LOADING,
} from "./getter-types";

export const getters: GetterTree<
  contractAgreementsState,
  Record<string, never>
> = {
  [FETCH_CONTRACT_AGREEMENTS](state) {
    return state.contractAgreementsList;
  },
  [IS_CONTRACT_AGREEMENTS_LOADING](state) {
    return state.isContractAgreementsLoading;
  },
};
