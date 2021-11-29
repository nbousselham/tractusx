<template>
  <v-list class="pa-0">
    <v-list-item class="list-item-height">
      <v-list-item-avatar>
        <v-btn icon x-large>
          <v-avatar class="user-avatar" size="49">
            <span class="white--text text-h7">{{ user.initials }}</span>
          </v-avatar>
        </v-btn>
      </v-list-item-avatar>

      <v-list-item-content class="d-block">
        <v-list-item-title>{{ user.fullName }}</v-list-item-title>
        <v-list-item-subtitle class="text--disabled">{{
          user.org
        }}</v-list-item-subtitle>
      </v-list-item-content>

      <v-list-item-action class="ma-0">
        <v-menu
          transition="slide-y-transition"
          :close-on-content-click="false"
          bottom
          rounded
          offset-y
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn icon v-bind="attrs" v-on="on">
              <v-icon>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-item>
              <v-list-item
                v-for="userMenuItem in userMenuItems"
                :key="userMenuItem.id"
                @click="() => {}"
              >
                <v-list-item-title>
                  <v-icon>{{ userMenuItem.icon }}</v-icon>
                  {{ userMenuItem.title }}</v-list-item-title
                >
              </v-list-item>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-list-item-action>
    </v-list-item>
  </v-list>
</template>
<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  name: "UserAvatar",
  data: () => ({
    user: {
      initials: "PH",
      fullName: "Pankaj Humnabadkar",
      email: "pankaj.humnabadkar@t-systems.com",
      org: "T-Systems",
    },
    userMenuItems: [
      {
        id: 1,
        title: "Logout",
        icon: "mdi-logout",
      },
    ],
  }),
});
</script>
<style lang="scss" scoped>
@import "~@/styles/variables";

.list-item-height {
  max-height: 48px;
}
.user-avatar {
  background: $brand-color-green;
}
</style>
