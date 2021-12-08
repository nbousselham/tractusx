<template>
  <v-data-table
    :headers="headers"
    :items="filteredDataOffers"
    hide-default-header
    :items-per-page="5"
    class="data-offer-list elevation-3 mb-3"
  >
    <template v-slot:body="{ items }">
      <tbody>
        <tr
          v-for="item in items"
          :key="item.id"
          @mouseover="selectItem(item)"
          @mouseleave="unSelectItem()"
        >
          <td class="d-block d-sm-table-cell">{{ item.title }}</td>
          <td class="d-block d-sm-table-cell" v-if="item.fileName">
            <span class="text--disabled">File: </span>
            <span>{{ item.fileName }}</span>
          </td>
          <td class="d-block d-sm-table-cell" v-if="item.accessControlUseCase">
            <span class="text--disabled">Use cases: </span>
            <span>{{ item.accessControlUseCase }}</span>
          </td>
          <td class="d-block d-sm-table-cell" v-if="item.byOrganizationRole">
            <span class="text--disabled">Access control: </span>
            <span>Company role ({{ item.byOrganizationRole }})</span>
          </td>
          <td class="d-block d-sm-table-cell" v-if="item.usageControl">
            <span class="text--disabled">Usage control: </span>
            <span>{{ item.usageControl }} access</span>
          </td>
          <td class="d-block d-sm-table-cell">
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
.v-data-table.data-offer-list {
  & .v-data-footer {
    border-top: none;
  }
  & td {
    border-bottom: 3px solid rgba(0, 0, 0, 0.12) !important;
    height: 56px !important;
  }
}
</style>
