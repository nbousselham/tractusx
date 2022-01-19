<template>
  <v-data-table
    :headers="headers"
    :items="dataOffers"
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
            <p class="mb-0">
              <span>{{ item.title ? item.title : "" }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{ item.fileName ? item.fileName : "" }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{
                item.accessControlUseCaseType === AccessControlType.UNLIMITED
                  ? item.accessControlUseCaseType
                  : item.accessControlUseCase
                  ? item.accessControlUseCase.join(", ")
                  : ""
              }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{
                item.accessControlByRoleType === AccessControlType.UNLIMITED
                  ? item.accessControlByRoleType
                  : item.byOrganizationRole
                  ? item.byOrganizationRole.join(", ")
                  : ""
              }}</span>
            </p>
          </td>
          <td class="d-block d-sm-table-cell">
            <p class="mb-0">
              <span>{{
                item.usageControlType ? item.usageControlType : ""
              }}</span>
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
                    @click="viewOfferDetails(item)"
                  >
                    <v-icon large dark>$vuetify.icons.infoIcon</v-icon>
                  </v-btn>
                </template>
                <span>See data offer details</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    @click="editDataOffer(item)"
                  >
                    <v-icon large dark>$vuetify.icons.editIcon</v-icon>
                  </v-btn>
                </template>
                <span>Edit data offer</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    @click="duplicateDataOffer(item)"
                  >
                    <v-icon large dark>$vuetify.icons.duplicateIcon</v-icon>
                  </v-btn>
                </template>
                <span>Duplicate data offer</span>
              </v-tooltip>
              <v-tooltip bottom>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    class="mr-2"
                    @click="deleteDataOffer(item)"
                  >
                    <v-icon large dark>$vuetify.icons.deleteIcon</v-icon>
                  </v-btn>
                </template>
                <span>Delete data offer</span>
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
import { iDataOffers } from "@/common/interfaces/dataOffers/IDataOffers";
import {
  DATA_OFFER_TABLE_HEADERS,
  AccessControlType,
} from "@/common/util/DataOfferUtil";

export default Vue.extend({
  name: "DataOffersList",
  data: () => ({
    headers: DATA_OFFER_TABLE_HEADERS,
    selectedItem: [],
    AccessControlType,
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

.v-data-table.data-offer-list {
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
