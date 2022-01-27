import { Module } from "vuex";
import { getters } from "./getters";
import { actions } from "./actions";
import { mutations } from "./mutations";
import { contractAgreementsState } from "./iContractAgreementStates";

const ContractAgreementsState: Module<
  contractAgreementsState,
  Record<string, never>
> = {
  state: {
    contractAgreementsList: [],
    isContractAgreementsLoading: false,
  },
  actions,
  getters,
  mutations,
};

export default ContractAgreementsState;
