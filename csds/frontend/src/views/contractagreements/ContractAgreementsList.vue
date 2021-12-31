<template>
  <div class="contract-agreement-panel mt-10">
    <h2 class="mb-4 ml-2">Contract agreements</h2>
    <h5 class="mb-4 ml-2 contract-agreement-info">
      Contract agreements are automatic processes. You can only discover latest
      added contracts and their details.
    </h5>
    <CxPanel class="ml-2">
      <template v-slot:panel-title>
        <span>No contract agreements.</span>
      </template>
    </CxPanel>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import CxPanel from "@/components/CxPanel.vue";

export default Vue.extend({
  name: "ContractAgreementsList",
  components: {
    CxPanel,
  },
});
</script>
<style lang="scss">
.contract-agreement-info {
  opacity: 0.4;
  font-size: 14px;
}
</style>
