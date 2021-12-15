<template>
  <v-row no-gutters>
    <v-col cols="12">
      <section class="ma-10">
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
            <template v-slot:panel-action>
              <v-dialog v-model="dialog" width="500">
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    id="add-data-offer-btn"
                    dark
                    large
                    v-bind="attrs"
                    v-on="on"
                    >ADD NEW DATA OFFER</v-btn
                  >
                </template>

                <v-card>
                  <v-card-title class="dialog-header font-weight-bold">
                    Create a new data offer
                  </v-card-title>
                  <div class="text-center">
                    <v-card-text wrap class="pa-7">
                      Create new Data offer content
                    </v-card-text>
                  </div>
                  <v-divider></v-divider>
                  <div class="text-center">
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="black" text @click="dialog = false">
                        CANCEL
                      </v-btn>
                      <v-btn
                        id="add-data-offer-btn"
                        dark
                        large
                        @click="dialog = false"
                        >ADD NEW DATA OFFER</v-btn
                      >
                    </v-card-actions>
                  </div>
                </v-card>
              </v-dialog>
            </template>
          </CxPanel>
          <v-btn id="add-data-offer-btn" class="ml-2" dark large
            >ADD NEW DATA OFFER</v-btn
          >
        </div>
        <div class="contract-agreement-panel mt-10">
          <h2 class="mb-4">Contract agreements</h2>
          <h5 class="mb-4 contract-agreement-info">
            Contract agreements are automatic processes. You can only discover
            the latest added contracts and their details.
          </h5>
          <CxPanel>
            <template v-slot:panel-title>
              <span>No contract agreements.</span>
            </template>
          </CxPanel>
        </div>
      </section>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import Vue from "vue";
import CxPanel from "@/components/CxPanel.vue";
import { GET_DATA_OFFERS } from "@/store/modules/dataoffer/actions/action-types";
import {
  FETCH_DATA_OFFERS,
  IS_OFFER_LOADING,
} from "@/store/modules/dataoffer/getters/getter-types";
import DataOffersList from "../dataoffer/DataOffersList.vue";
import { iDataOffers } from "@/common/interfaces/dataOffers/IDataOffers";

export default Vue.extend({
  name: "Dashboard",
  components: {
    CxPanel,
    DataOffersList,
  },
  data: () => ({
    dialog: false,
  }),
  created() {
    this.$store.dispatch(GET_DATA_OFFERS);
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
<style lang="scss" scoped>
@import "~@/styles/variables";

#add-data-offer-btn {
  background-color: $brand-color-green;
  padding: 15px;
}
.contract-agreement-info {
  opacity: 0.4;
  font-size: 14px;
}
.dialog-header {
  max-height: 36px;
  padding: 5px 20px !important;
  background: $brand-color-green;
  font-size: 14px !important;

  & div {
    font-size: 14px;
    font-weight: bold;
  }
}
</style>
