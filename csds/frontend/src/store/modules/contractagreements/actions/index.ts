import { ActionTree } from "vuex";
import ContractAgreementsService from "@/common/services/contractagreements/ContractAgreementsService";
import { contractAgreementsState } from "../iContractAgreementStates";
import { GET_CONTRACT_AGREEMENTS } from "./action-types";
import {
  SET_CONTRACT_AGREEMENTS,
  SET_CONTRACT_AGREEMENTS_LOADING,
} from "../mutations/mutation-types";
import { iContractAgreements } from "@/common/interfaces/contractAgreements/IContractAgreements";

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
        const filteredContractAgreements: Array<iContractAgreements> =
          contractAgreementsList.sort(
            (a: iContractAgreements, b: iContractAgreements) => {
              const d1 = new Date(a.modifiedTimeStamp).valueOf();
              const d2 = new Date(b.modifiedTimeStamp).valueOf();
              return d2 - d1;
            }
          );
        commit(SET_CONTRACT_AGREEMENTS, filteredContractAgreements);
        commit(SET_CONTRACT_AGREEMENTS_LOADING, false);
      })
      .catch((error) => {
        commit(SET_CONTRACT_AGREEMENTS_LOADING, false);
        throw error;
      });
  },
};
