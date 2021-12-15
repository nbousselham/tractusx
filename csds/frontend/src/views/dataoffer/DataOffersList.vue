<template>
  <v-data-table
    :headers="headers"
    :items="filteredDataOffers"
    hide-default-header
    :items-per-page="5"
    class="data-offer-list elevation-0 mb-3"
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
            <p class="mb-0" v-if="item.title">
              <span>{{ item.title }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0" v-if="item.fileName">
              <span class="text--disabled">File: </span>
              <span>{{ item.fileName }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0" v-if="item.accessControlUseCase">
              <span class="text--disabled">Use cases: </span>
              <span>{{ item.accessControlUseCase }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0" v-if="item.byOrganizationRole">
              <span class="text--disabled">Access control: </span>
              <span>Company role ({{ item.byOrganizationRole }})</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0" v-if="item.usageControl">
              <span class="text--disabled">Usage control: </span>
              <span>{{ item.usageControl }} usage</span>
            </p>
          </td>
          <td width="25%" class="d-block d-sm-table-cell">
            <div
              class="d-flex"
              :class="{ 'mt-5': isSmallScreen }"
              v-if="item === selectedItem"
            >
              <v-btn
                class="ml-3 font-weight-bold"
                color="#CCCCCC"
                small
                elevation="0"
                @click="editOffer(item)"
                >EDIT</v-btn
              >
              <v-btn
                class="ml-3 font-weight-bold"
                color="#CCCCCC"
                small
                elevation="0"
                @click="duplicateOffer(item)"
                >DUPLICATE</v-btn
              >
              <v-btn
                class="ml-3 font-weight-bold"
                color="#CCCCCC"
                small
                elevation="0"
                @click="deleteOffer(item)"
                >DELETE</v-btn
              >
            </div>
          </td>
        </tr>
      </tbody>
    </template>
  </v-data-table>
</template>
<script lang="ts">
import Vue from "vue";
import {
  iFilteredDataOffers,
  iDataOffers,
} from "@/common/interfaces/dataOffers/IDataOffers";
import { DATA_OFFER_TABLE_HEADERS } from "@/common/util/DataOfferUtil";

export default Vue.extend({
  name: "DataOffersList",
  data: () => ({
    headers: DATA_OFFER_TABLE_HEADERS,
    selectedItem: [],
  }),
  props: {
    dataOffers: {
      type: Array as () => Array<iDataOffers>,
      default: () => [],
    },
  },
  computed: {
    isSmallScreen(): boolean {
      return this.$vuetify.breakpoint.mdAndDown;
    },
    filteredDataOffers() {
      const dataOffersArr: iFilteredDataOffers[] = [];
      let filteredObj: iFilteredDataOffers;
      this.dataOffers.forEach((dataOffer: iDataOffers) => {
        filteredObj = {
          title: "",
          fileName: "",
          accessControlUseCase: "",
          byOrganizationRole: "",
          usageControl: "",
          id: "",
        };
        filteredObj["title"] = dataOffer.title || "";
        filteredObj["fileName"] = dataOffer.fileName || "";
        filteredObj["accessControlUseCase"] = dataOffer.accessControlUseCase
          ? dataOffer.accessControlUseCase.join(",")
          : "";
        filteredObj["byOrganizationRole"] = dataOffer.byOrganizationRole
          ? dataOffer.byOrganizationRole.join(",")
          : "";
        filteredObj["usageControl"] = dataOffer.accessControlUseCaseType || "";
        filteredObj["id"] = dataOffer["_id"] || "";
        dataOffersArr.push(filteredObj);
      });
      return dataOffersArr;
    },
  },
  mounted() {
    console.log(this.filteredDataOffers);
  },
  methods: {
    selectItem(item: never[]) {
      this.selectedItem = item;
    },
    unSelectItem() {
      this.selectedItem = [];
    },
    editOffer(dataOffer: iDataOffers) {
      console.log(dataOffer);
    },
    duplicateOffer(dataOffer: iDataOffers) {
      console.log(dataOffer);
    },
    deleteOffer(dataOffer: iDataOffers) {
      console.log(dataOffer);
    },
  },
});
</script>

<style lang="scss">
@import "~@/styles/variables";

.v-data-table.data-offer-list {
  & > .v-data-table__wrapper > table > tbody > tr:hover {
    background: white !important;
    box-shadow: $cx-elevation;
  }
  & > .v-data-table__wrapper {
    background-color: $grey1 !important;
  }
  & > .v-data-table__wrapper > table > tbody > tr {
    background: white;
  }
  & > div > table {
    border-spacing: 0 0.4rem !important;
    padding: 0 2px 0 8px;
  }
  & td {
    height: 56px !important;
    border-bottom: none !important;
  }
  & .v-data-footer {
    border-top: none !important;
    box-shadow: inset 8px 0 0 0 $grey1;
    background-clip: content-box;
  }
}
</style>
