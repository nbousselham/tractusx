<template>
  <v-data-table
    :headers="headers"
    :items="contractAgreements"
    :items-per-page="5"
    class="contract-agreements-list elevation-0 mb-3"
  >
    <template v-slot:body="{ items }">
      <tbody>
        <tr
          v-for="item in items"
          :key="item.id"
          @mouseover="selectItem(item)"
          @mouseleave="unSelectItem()"
        >
          <td class="d-block d-sm-table-cell">
            <p class="mb-0 black--text font-weight-bold">
              <span>{{ item.contractName ? item.contractName : "" }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span v-if="item.status === CONTRACT_AGREEMENT_STATUS.ACCEPTED">
                <v-icon small color="black"> mdi-check-circle </v-icon></span
              >
              <span v-if="item.status === CONTRACT_AGREEMENT_STATUS.SUSPENDED">
                <v-icon small color="#ffa600" dark>
                  mdi-alert-circle
                </v-icon></span
              >
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{
                item.dateEstablished ? item.dateEstablished : ""
              }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{ item.dataConsumer ? item.dataConsumer : "" }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{
                item.accessLimitedByUsecase ? item.accessLimitedByUsecase : ""
              }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{
                item.accessLimitedByCompanyRole
                  ? item.accessLimitedByCompanyRole
                  : ""
              }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{ item.usageControl ? item.usageControl : "" }}</span>
            </p>
          </td>
          <td width="15%" class="d-block d-sm-table-cell">
            <div
              class="d-flex"
              :class="{ 'mt-5': isSmallScreen }"
              v-if="item === selectedItem"
            >
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    @click="viewContractAgreementDetails(item)"
                  >
                    <v-icon large dark>$vuetify.icons.infoIcon</v-icon>
                  </v-btn>
                </template>
                <span>See contract agreement details</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    v-if="item.status === CONTRACT_AGREEMENT_STATUS.SUSPENDED"
                    @click="acceptContractAgreement(item)"
                  >
                    <v-icon color="#b3cb2d" large dark
                      >$vuetify.icons.playIcon</v-icon
                    >
                  </v-btn>
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    v-if="item.status === CONTRACT_AGREEMENT_STATUS.ACCEPTED"
                    disabled
                    @click="acceptContractAgreement(item)"
                  >
                    <v-icon large dark>$vuetify.icons.playIconGrayed</v-icon>
                  </v-btn>
                </template>
                <span>Accept contract agreement</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    v-if="item.status === CONTRACT_AGREEMENT_STATUS.ACCEPTED"
                    @click="suspendContractAgreement(item)"
                  >
                    <v-icon color="#b3cb2d" large dark
                      >$vuetify.icons.pauseIcon</v-icon
                    >
                  </v-btn>
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    v-if="item.status === CONTRACT_AGREEMENT_STATUS.SUSPENDED"
                    disabled
                    @click="acceptContractAgreement(item)"
                  >
                    <v-icon large dark>$vuetify.icons.pauseIconGrayed</v-icon>
                  </v-btn>
                </template>
                <span>Suspend contract agreement</span>
              </v-tooltip>
            </div>
          </td>
        </tr>
      </tbody>
    </template>
  </v-data-table>
</template>
<script lang="ts">
import Vue from "vue";
import { CONTRACT_AGREEMENTS_TABLE_HEADERS } from "@/common/util/ContractAgreementsUtil";
import {
  iContractAgreements,
  CONTRACT_AGREEMENT_STATUS,
} from "@/common/interfaces/contractAgreements/IContractAgreements";

export default Vue.extend({
  name: "ContractAgreementsList",
  data: () => ({
    headers: CONTRACT_AGREEMENTS_TABLE_HEADERS,
    selectedItem: [],
    CONTRACT_AGREEMENT_STATUS,
  }),
  props: {
    contractAgreements: {
      type: Array as () => Array<iContractAgreements>,
      default: () => [],
    },
  },
  computed: {
    isSmallScreen(): boolean {
      return this.$vuetify.breakpoint.mdAndDown;
    },
  },
  methods: {
    selectItem(item: never[]) {
      this.selectedItem = item;
    },
    unSelectItem() {
      this.selectedItem = [];
    },
  },
});
</script>

<style lang="scss">
@import "~@/styles/variables";

.v-data-table.contract-agreements-list {
  & > .v-data-table__wrapper > table > thead > tr:last-child > th {
    border-bottom: none !important;
    & > span {
      color: $grey9;
    }
  }
  & > .v-data-table__wrapper > table > tbody > tr:hover {
    background: $white !important;
    box-shadow: $cx-elevation;
  }
  & > .v-data-table__wrapper {
    background-color: $grey1 !important;
  }
  & > .v-data-table__wrapper > table > tbody > tr {
    background: $white;
  }
  & > div > table {
    border-spacing: 0 0.4rem !important;
    padding: 0 2px 0 8px;
  }
  & td {
    height: 56px !important;
    border-bottom: none !important;
    & span.v-icon {
      width: 32px !important;
    }
  }
  & .v-data-footer {
    border-top: none !important;
    box-shadow: inset 8px 0 0 0 $grey1;
    background-clip: content-box;
  }
}
</style>
