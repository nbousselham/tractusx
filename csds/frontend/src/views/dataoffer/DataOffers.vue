<template>
  <div class="data-offers-panel">
    <h2 class="mb-4 ml-2">Data offers</h2>
    <template v-if="dataOffers && !isDataOffersLoading">
      <DataOffersList :dataOffers="dataOffers" />
    </template>
    <template v-else-if="isDataOffersLoading">
      <v-skeleton-loader
        class="mb-3"
        type="table-tbody"
        transition="scale-transition"
        :loading="isDataOffersLoading"
        max-height="390"
      >
      </v-skeleton-loader>
    </template>
    <CxPanel v-else>
      <template v-slot:panel-title>
        <span>No data offers.</span>
      </template>
    </CxPanel>
    <template>
      <v-btn
        id="add-data-offer-btn"
        class="ml-2"
        dark
        large
        @click.stop="showCreateOfferDialog = true"
        >ADD NEW DATA OFFER</v-btn
      >
      <CreateDataOfferModal :isOpen.sync="showCreateOfferDialog" />
    </template>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import CxPanel from "@/components/CxPanel.vue";
import {
  GET_DATA_OFFERS,
  GET_USE_CASES,
  GET_ORG_ROLES,
} from "@/store/modules/dataoffer/actions/action-types";
import {
  FETCH_DATA_OFFERS,
  IS_OFFER_LOADING,
} from "@/store/modules/dataoffer/getters/getter-types";
import DataOffersList from "../dataoffer/DataOffersList.vue";
import { iDataOffers } from "@/common/interfaces/dataOffers/IDataOffers";
import CreateDataOfferModal from "@/views/createdataoffer/CreateDataOfferModal.vue";

export default Vue.extend({
  name: "DataOffers",
  components: {
    CxPanel,
    DataOffersList,
    CreateDataOfferModal,
  },
  data: () => ({
    showCreateOfferDialog: false,
  }),
  created() {
    this.$store.dispatch(GET_DATA_OFFERS);
    this.$store.dispatch(GET_USE_CASES);
    this.$store.dispatch(GET_ORG_ROLES);
  },
  computed: {
    dataOffers(): iDataOffers {
      return this.$store.getters[FETCH_DATA_OFFERS];
    },
    isDataOffersLoading(): boolean {
      return this.$store.getters[IS_OFFER_LOADING];
    },
  },
});
</script>
<style lang="scss">
@import "~@/styles/variables";

#add-data-offer-btn {
  background-color: $brand-color-green;
  padding: 15px;
}
</style>
