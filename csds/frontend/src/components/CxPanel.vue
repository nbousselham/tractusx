<template>
  <div class="panel">
    <v-card elevation="0" class="mx-auto" height="156px">
      <v-card-title class="panel-header black--text" v-if="hasTitleSlot">
        <slot name="panel-title"></slot>
      </v-card-title>
      <v-card-text v-if="hasContentSlot">
        <slot name="panel-content"></slot>
      </v-card-text>
      <v-card-actions class="panel-action" v-if="hasActionSlot">
        <slot name="panel-action"></slot>
      </v-card-actions>
    </v-card>
  </div>
</template>
<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  name: "CxPanel",
  computed: {
    hasTitleSlot(): boolean {
      return !!this.$slots["panel-title"];
    },
    hasContentSlot(): boolean {
      return !!this.$slots["panel-content"];
    },
    hasActionSlot(): boolean {
      return !!this.$slots["panel-action"];
    },
  },
});
</script>
<style lang="scss">
@import "~@/styles/variables";

.panel-header {
  max-height: 36px;
  padding: 5px 20px !important;
  background: $grey4;

  & span {
    font-size: 14px;
    font-weight: bold;
  }
}
.panel-action {
  margin: 30px 12px;
}
</style>
