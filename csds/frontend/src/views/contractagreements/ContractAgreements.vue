<template>
  <div class="contract-agreement-panel mt-10">
    <v-toolbar
      class="contract-agreements-toolbar"
      color="#F5F5F5"
      elevation="0"
    >
      <h2 class="d-flex align-start flex-column mb-4 ml-2">
        Contract agreements
      </h2>
      <v-spacer></v-spacer>
      <v-subheader
        class="d-inline text--disabled mt-2"
        v-text="'Sort by'"
      ></v-subheader>
      <div>
        <v-select
          class="d-inline contract-sortby black--text font-weight-bold"
          v-model="sortContractsBy"
          :items="sortContractsByItems"
          @change="sortContracts"
        ></v-select>
      </div>
    </v-toolbar>
    <h5 class="mb-4 ml-4 contract-agreement-info">
      Contract agreements represent the subscriptions of your organizations's
      data offers by other ecosystem members. Your organization provides data to
      these consumers.
    </h5>
    <template v-if="contractAgreements && !isContractAgreementsLoading">
      <ContractAgreementsList :contractAgreements="contractAgreements" />
    </template>
    <template v-else-if="isContractAgreementsLoading">
      <v-skeleton-loader
        class="mb-3"
        type="table-tbody"
        transition="scale-transition"
        :loading="isContractAgreementsLoading"
        max-height="390"
      >
      </v-skeleton-loader>
    </template>
    <CxPanel v-else class="ml-2">
      <template v-slot:panel-title>
        <span>No contract agreements.</span>
      </template>
    </CxPanel>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import CxPanel from "@/components/CxPanel.vue";
import ContractAgreementsList from "@/views/contractagreements/ContractAgreementsList.vue";
import { GET_CONTRACT_AGREEMENTS } from "@/store/modules/contractagreements/actions/action-types";
import {
  FETCH_CONTRACT_AGREEMENTS,
  IS_CONTRACT_AGREEMENTS_LOADING,
} from "@/store/modules/contractagreements/getters/getter-types";
import { iContractAgreements } from "@/common/interfaces/contractAgreements/IContractAgreements";
import { SORT_BY } from "@/common/util/index";

export default Vue.extend({
  name: "ContractAgreements",
  components: {
    CxPanel,
    ContractAgreementsList,
  },
  data: () => ({
    sortContractsBy: "newest",
    sortContractsByItems: ["newest", "oldest"],
  }),
  created() {
    this.$store.dispatch(GET_CONTRACT_AGREEMENTS);
  },
  computed: {
    contractAgreements(): Array<iContractAgreements> {
      return this.$store.getters[FETCH_CONTRACT_AGREEMENTS];
    },
    isContractAgreementsLoading(): boolean {
      return this.$store.getters[IS_CONTRACT_AGREEMENTS_LOADING];
    },
  },
  methods: {
    sortContracts(sortByKey: string) {
      if (sortByKey === SORT_BY.NEWEST) {
        this.contractAgreements.sort((a, b) => {
          const d1 = new Date(a.modifiedTimeStamp).valueOf();
          const d2 = new Date(b.modifiedTimeStamp).valueOf();
          return d2 - d1;
        });
      } else if (sortByKey === SORT_BY.OLDEST) {
        this.contractAgreements.sort((a, b) => {
          const d1 = new Date(a.modifiedTimeStamp).valueOf();
          const d2 = new Date(b.modifiedTimeStamp).valueOf();
          return d1 - d2;
        });
      }
    },
  },
});
</script>
<style lang="scss">
.contract-agreement-info {
  opacity: 0.4;
  font-size: 14px;
}
.contract-agreements-toolbar {
  & .v-toolbar__content {
    padding: 4px 8px;
  }
}
.v-select.contract-sortby.v-text-field {
  & input {
    width: 10px;
  }
}
</style>
