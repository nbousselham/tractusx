<template>
  <v-row no-gutters>
    <v-col cols="12">
      <section class="ma-10">
        <DataOffers />
        <ContractAgreements />
      </section>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import Vue from "vue";
import DataOffers from "@/views/dataoffer/DataOffers.vue";
import ContractAgreements from "@/views/contractagreements/ContractAgreements.vue";

export default Vue.extend({
  name: "Dashboard",
  components: {
    DataOffers,
    ContractAgreements,
  },
});
</script>
