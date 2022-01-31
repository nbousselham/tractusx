<template>
  <div class="data-offers-panel">
    <v-row justify="end">
      <v-spacer></v-spacer>
      <v-col cols="auto">
        <v-tooltip bottom>
          <template v-slot:activator="{ on, attrs }">
            <v-btn
              id="add-data-offer-btn"
              class="mb-4 ml-2"
              dark
              large
              v-bind="attrs"
              v-on="on"
              @click.stop="showCreateOfferDialog = true"
              >ADD NEW DATA OFFER<v-icon
                color="#b3cb2d"
                class="ml-4 add-offer-icon"
                x-large
                right
                dark
              >
                mdi-plus-box
              </v-icon></v-btn
            >
          </template>
          <span>Add new data offer</span>
        </v-tooltip>
        <CreateDataOfferModal :isOpen.sync="showCreateOfferDialog" />
      </v-col>
    </v-row>
    <v-toolbar class="data-offer-toolbar" color="#F5F5F5" elevation="0">
      <h2 class="mb-4">Data offers</h2>
      <v-spacer></v-spacer>
      <v-subheader
        class="d-inline text--disabled mt-2"
        v-text="'Sort by'"
      ></v-subheader>
      <div>
        <v-select
          class="d-inline dataoffer-sortby black--text font-weight-bold"
          v-model="sortDataOffersBy"
          :items="sortDataOffersByItems"
          @change="sortDataOffers"
        ></v-select>
      </div>
    </v-toolbar>
    <h5 class="mb-4 ml-2 contract-agreement-info">
      Data offers represent data of your organization that other ecosystem
      members can discover and subscribe to.
    </h5>
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
import { SORT_BY } from "@/common/util/index";

export default Vue.extend({
  name: "DataOffers",
  components: {
    CxPanel,
    DataOffersList,
    CreateDataOfferModal,
  },
  data: () => ({
    showCreateOfferDialog: false,
    sortDataOffersBy: "newest",
    sortDataOffersByItems: ["newest", "oldest"],
  }),
  created() {
    this.$store.dispatch(GET_DATA_OFFERS);
    this.$store.dispatch(GET_USE_CASES);
    this.$store.dispatch(GET_ORG_ROLES);
  },
  computed: {
    dataOffers(): Array<iDataOffers> {
      return this.$store.getters[FETCH_DATA_OFFERS];
    },
    isDataOffersLoading(): boolean {
      return this.$store.getters[IS_OFFER_LOADING];
    },
  },
  methods: {
    sortDataOffers(sortByKey: string) {
      if (sortByKey === SORT_BY.NEWEST) {
        this.dataOffers.sort((a, b) => {
          const d1 = new Date(a.modifiedTimeStamp).valueOf();
          const d2 = new Date(b.modifiedTimeStamp).valueOf();
          return d2 - d1;
        });
      } else if (sortByKey === SORT_BY.OLDEST) {
        this.dataOffers.sort((a, b) => {
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
@import "~@/styles/variables";

#add-data-offer-btn {
  background-color: $brand-color-green;
  padding: 15px;
}
.data-offer-toolbar {
  & .v-toolbar__content {
    padding: 4px 8px;
  }
}
.add-offer-icon {
  background: white;
}
.v-select.dataoffer-sortby.v-text-field {
  & input {
    width: 10px;
  }
}
</style>
