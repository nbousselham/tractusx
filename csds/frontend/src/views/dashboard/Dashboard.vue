<template>
  <v-row no-gutters>
    <v-col cols="12">
      <section class="ma-10">
        <DataOffers />
        <ContractAgreementsList />
      </section>
    </v-col>
  </v-row>
</template>

<script lang="ts">
import Vue from "vue";
import DataOffers from "@/views/dataoffer/DataOffers.vue";
import ContractAgreementsList from "@/views/contractagreements/ContractAgreementsList.vue";

export default Vue.extend({
  name: "Dashboard",
  components: {
    DataOffers,
    ContractAgreementsList,
  },
});
</script>
