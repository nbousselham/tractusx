<template>
  <v-app>
    <v-app-bar color="white" elevation="1" app>
      <v-app-bar-title class="ml-8">
        <img src="@/styles/assets/images/catenax-logo.svg" />
      </v-app-bar-title>

      <v-spacer></v-spacer>

      <UserAvatar />
    </v-app-bar>
    <v-main>
      <v-container fill-height fluid>
        <v-layout>
          <router-view></router-view>
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script lang="ts">
import Vue from "vue";
import UserAvatar from "@/components/UserAvatar.vue";

export default Vue.extend({
  name: "Dashboard",
  components: {
    UserAvatar,
  },
});
</script>

<style lang="scss">
@import "~@/styles/variables";

.v-main {
  background: $grey1;
}
</style>
