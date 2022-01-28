import { ActionTree } from "vuex";
import ContractAgreementsService from "@/common/services/contractagreements/ContractAgreementsService";
import { contractAgreementsState } from "../iContractAgreementStates";
import { GET_CONTRACT_AGREEMENTS } from "./action-types";
import {
  SET_CONTRACT_AGREEMENTS,
  SET_CONTRACT_AGREEMENTS_LOADING,
} from "../mutations/mutation-types";

export const actions: ActionTree<
  contractAgreementsState,
  Record<string, never>
> = {
  [GET_CONTRACT_AGREEMENTS]({ commit }) {
    commit(SET_CONTRACT_AGREEMENTS_LOADING, true);
    return ContractAgreementsService.getContractAgreements()
      .then((res) => {
        const { data } = res;
        const contractAgreementsList = data.data;
        const filteredContractAgreementsList = JSON.parse(
          JSON.stringify(contractAgreementsList)
        );
        commit(SET_CONTRACT_AGREEMENTS, filteredContractAgreementsList);
        commit(SET_CONTRACT_AGREEMENTS_LOADING, false);
      })
      .catch((error) => {
        commit(SET_CONTRACT_AGREEMENTS_LOADING, false);
        throw error;
      });
  },
};
