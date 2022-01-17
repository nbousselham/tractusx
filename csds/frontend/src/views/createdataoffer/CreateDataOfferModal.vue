<template>
  <v-dialog
    v-model="isOpen"
    class="create-offer-dialog"
    scrollable
    persistent
    width="960"
  >
    <v-card>
      <v-card-title>
        <h5 class="dialog-title">Add new data offer</h5>
        <nav>
          <ul>
            <li>
              <a href="#fileTitle" id="activeTab" class="fileTitle"
                >Title/File</a
              >
            </li>
            <li>
              <a href="#accessControl" class="accessControl">Access Control</a>
            </li>
            <li>
              <a href="#usageControl" class="usageControl">Usage Control</a>
            </li>
            <li><a href="#description" class="description">Description</a></li>
          </ul>
        </nav>
      </v-card-title>

      <v-divider></v-divider>

      <v-card-text
        @scroll.passive="handleScroll"
        class="pa-0 create-offer-modal-content"
        ref="createOfferModalContent"
      >
        <v-form
          ref="createOfferForm"
          v-model="createOfferForm"
          class="mx-2"
          lazy-validation
          enctype="multipart/form-data"
        >
          <v-container>
            <section id="fileTitle" class="createOfferSection">
              <v-row justify="center" no-gutters>
                <div class="d-flex col-12">
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="2">
                    <v-subheader class="dataoffer-subheader black--text"
                      >Title</v-subheader
                    >
                  </v-col>
                  <v-col cols="12" md="8">
                    <v-text-field
                      v-model="dataOfferTitle"
                      class="data-offer-title"
                      hide-details="auto"
                      solo
                      :rules="[(v) => !!v || 'Title is required']"
                      required
                      label="Title of the data offer"
                    ></v-text-field>
                  </v-col>
                  <v-spacer></v-spacer>
                </div>
                <div class="d-flex col-12">
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="2">
                    <v-subheader class="dataoffer-subheader black--text"
                      >Select file</v-subheader
                    >
                  </v-col>
                  <v-col cols="12" md="8">
                    <CxFileDrop ref="fileUpload" />
                  </v-col>
                  <v-spacer></v-spacer>
                </div>
              </v-row>
            </section>
            <section id="accessControl" class="createOfferSection">
              <v-row justify="center" no-gutters>
                <div class="accessControlByUseCase d-flex col-12">
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="4">
                    <v-subheader class="dataoffer-subheader mt-3 black--text"
                      >Access control by use case</v-subheader
                    >
                    <p class="px-4 text--disabled">
                      Select Unlimited Access or Limited Access and its
                      subcategories.
                    </p>
                  </v-col>
                  <v-col cols="12" md="8">
                    <v-radio-group
                      v-model="accessControlByUseCase"
                      row
                      mandatory
                    >
                      <v-radio
                        label="Unlimited access"
                        value="Unlimited"
                        color="#b3cb2d"
                      ></v-radio>
                      <v-radio
                        label="Limited access"
                        value="Limited"
                        color="#b3cb2d"
                      ></v-radio>
                    </v-radio-group>
                    <v-row
                      :class="{
                        'greyout-box': accessControlByUseCase === 'Unlimited',
                      }"
                    >
                      <v-spacer></v-spacer>
                      <v-col cols="12" md="9">
                        <div class="limitedUseCases">
                          <v-treeview
                            v-model="selectedUseCases"
                            :items="filteredItems"
                            selected-color="#b3cb2d"
                            selectable
                            return-object
                            open-all
                            :rules="[
                              () =>
                                selectedUseCases.length < 0 ||
                                'At least 1 use case should be selected',
                            ]"
                          ></v-treeview>
                          <div
                            v-if="isAccessControlByUseCaseError"
                            class="v-messages theme--light error--text"
                            role="alert"
                          >
                            <div class="v-messages__wrapper">
                              <div class="v-messages__message">
                                At least 1 use case should be selected
                              </div>
                            </div>
                          </div>
                        </div>
                      </v-col>
                    </v-row>
                  </v-col>
                </div>
                <div class="accessControlByRole d-flex col-12">
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="4">
                    <v-subheader class="dataoffer-subheader mt-3 black--text"
                      >Access control by role</v-subheader
                    >
                    <p class="px-4 text--disabled">
                      Grant acces by company role or company name. Just drag
                      items and from left to right to add access. It doesn’t
                      matter it is a role or a company name.
                    </p>
                    <p class="px-4">
                      <img src="@/styles/assets/images/drag-drop.svg" />
                      <span class="ml-1 text--disabled"
                        >Drag and drop items</span
                      >
                    </p>
                  </v-col>
                  <v-col cols="12" md="8">
                    <v-radio-group v-model="accessControlByRole" row mandatory>
                      <v-radio
                        label="Unlimited access"
                        value="Unlimited"
                        color="#b3cb2d"
                      ></v-radio>
                      <v-radio
                        label="Limited access"
                        value="Limited"
                        color="#b3cb2d"
                      ></v-radio>
                    </v-radio-group>
                    <v-row
                      :class="{
                        'greyout-box': accessControlByRole === 'Unlimited',
                      }"
                    >
                      <v-spacer></v-spacer>
                      <v-col cols="12" md="12">
                        <div class="limitedRole">
                          <v-card
                            class="rounded-0 elevation-0"
                            color="#f5f5f5"
                            height="120"
                            min-height="120"
                          >
                            <v-card-text>
                              <p class="mb-0">
                                Company role
                                <span class="link ml-2" @click="addAllRoles()"
                                  >Add all</span
                                >
                              </p>
                              <v-chip-group
                                column
                                class="role-chip-group"
                                active-class="role-chip-group primary--text"
                              >
                                <draggable
                                  :list="orgRoles"
                                  :emptyInsertThreshold="200"
                                  class="draggable-list"
                                  group="org-roles"
                                >
                                  <transition-group>
                                    <v-chip
                                      v-for="orgRole in orgRoles"
                                      :key="orgRole.id"
                                      draggable
                                    >
                                      {{ orgRole.role }}
                                    </v-chip>
                                  </transition-group>
                                </draggable>
                              </v-chip-group>
                            </v-card-text>
                          </v-card>
                          <v-card
                            class="rounded-0"
                            :class="{
                              'selected-roles-card':
                                !isAccessControlByRoleError,
                            }"
                            red-border
                            height="120"
                            min-height="120"
                            outlined
                          >
                            <v-card-text>
                              <v-chip-group
                                column
                                class="role-chip-group"
                                active-class="primary--text"
                              >
                                <draggable
                                  :emptyInsertThreshold="200"
                                  :list="selectedOrgRoles"
                                  class="draggable-list"
                                  group="org-roles"
                                >
                                  <transition-group>
                                    <v-chip
                                      v-for="selectedOrgRole in selectedOrgRoles"
                                      :key="selectedOrgRole.id"
                                      color="#b3cb2d"
                                      text-color="white"
                                      draggable
                                    >
                                      {{ selectedOrgRole.role }}
                                    </v-chip>
                                  </transition-group>
                                </draggable>
                              </v-chip-group>
                            </v-card-text>
                            <v-card-actions>
                              <v-spacer></v-spacer>
                              <span class="link" @click="removeAllRoles()"
                                >Remove all</span
                              >
                            </v-card-actions>
                          </v-card>
                        </div>
                        <div
                          v-if="isAccessControlByRoleError"
                          class="mt-1 v-messages theme--light error--text"
                          role="alert"
                        >
                          <div class="v-messages__wrapper">
                            <div class="v-messages__message">
                              At least 1 role should be selected
                            </div>
                          </div>
                        </div>
                      </v-col>
                    </v-row>
                  </v-col>
                </div>
              </v-row>
            </section>
            <section id="usageControl" class="createOfferSection">
              <v-row justify="center" no-gutters>
                <div class="usageControl-wrapper d-flex col-12">
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="3">
                    <v-subheader class="dataoffer-subheader mt-2 black--text"
                      >Usage control</v-subheader
                    >
                  </v-col>
                  <v-col cols="12" md="9">
                    <v-radio-group v-model="usageControl" row mandatory>
                      <v-radio
                        label="Unlimited usage timeframe"
                        value="Unlimited"
                        color="#b3cb2d"
                      ></v-radio>
                      <v-radio
                        label="Limited usage timeframe"
                        value="Limited"
                        color="#b3cb2d"
                      ></v-radio>
                    </v-radio-group>
                    <v-row>
                      <v-col cols="12" md="4">
                        <v-checkbox
                          v-model="usageLoggingChk"
                          label="Usage logging"
                          color="#b3cb2d"
                        ></v-checkbox>
                      </v-col>
                      <v-spacer></v-spacer>
                      <v-col
                        cols="12"
                        md="8"
                        :class="{
                          'greyout-box': usageControl === 'Unlimited',
                        }"
                      >
                        <p class="black--text mb-0 ml-9">Usage timeframe</p>
                        <v-row>
                          <v-col class="ml-9" cols="12" sm="6" md="5">
                            <v-menu
                              ref="startDateMenu"
                              v-model="startDateMenu"
                              :close-on-content-click="false"
                              transition="scale-transition"
                              offset-y
                              min-width="auto"
                            >
                              <template v-slot:activator="{ on, attrs }">
                                <v-text-field
                                  prepend-inner-icon="mdi-calendar"
                                  readonly
                                  :value="formattedStartDate"
                                  v-bind="attrs"
                                  v-on="on"
                                  required
                                ></v-text-field>
                              </template>
                              <v-date-picker
                                locale="en-in"
                                :min="date"
                                v-model="startDateVal"
                                no-title
                                scrollable
                                color="#b3cb2d"
                                header-color="#b3cb2d"
                                @input="startDateMenu = false"
                              >
                              </v-date-picker>
                            </v-menu>
                          </v-col>
                          <span class="date-range"> - </span>
                          <v-col cols="12" sm="6" md="5">
                            <v-menu
                              ref="endDateMenu"
                              v-model="endDateMenu"
                              :close-on-content-click="false"
                              transition="scale-transition"
                              offset-y
                              min-width="auto"
                            >
                              <template v-slot:activator="{ on, attrs }">
                                <v-text-field
                                  prepend-inner-icon="mdi-calendar"
                                  readonly
                                  :value="formattedEndDate"
                                  v-bind="attrs"
                                  v-on="on"
                                  :rules="
                                    usageControl === 'Limited'
                                      ? [
                                          () =>
                                            !!formattedEndDate ||
                                            'End date is required',
                                        ]
                                      : []
                                  "
                                ></v-text-field>
                              </template>
                              <v-date-picker
                                locale="en-in"
                                :min="startDateVal"
                                v-model="endDateVal"
                                no-title
                                scrollable
                                color="#b3cb2d"
                                header-color="#b3cb2d"
                                @input="endDateMenu = false"
                              >
                              </v-date-picker>
                            </v-menu>
                          </v-col>
                        </v-row>
                        <v-checkbox
                          class="ml-8"
                          v-model="deleteAfterTimeFrame"
                          :rules="
                            usageControl === 'Limited' ? [
                              (v) => !!v || 'This is required field',
                            ] : []
                          "
                          label="Must delete after the selected timeframe"
                          color="#b3cb2d"
                        ></v-checkbox>
                      </v-col>
                    </v-row>
                  </v-col>
                </div>
              </v-row>
            </section>
            <section id="description" class="createOfferSection">
              <v-row justify="center" no-gutters>
                <div class="description-wrapper d-flex col-12">
                  <v-spacer></v-spacer>
                  <v-col cols="12" md="2">
                    <v-subheader class="dataoffer-subheader black--text"
                      >Description</v-subheader
                    >
                  </v-col>
                  <v-col cols="12" md="8">
                    <v-textarea
                      class="dataoffer-description"
                      v-model="dataOfferDescription"
                      background-color="#F5F5F5"
                      solo
                      color="black"
                      label="Description of the data offer"
                    ></v-textarea>
                  </v-col>
                </div>
              </v-row>
            </section>
          </v-container>
        </v-form>
      </v-card-text>

      <v-divider></v-divider>

      <v-card-actions style="height: 70px">
        <v-spacer></v-spacer>
        <v-btn text width="200" @click.native="close"> CANCEL </v-btn>
        <v-btn
          :disabled="isCreateOfferFormError"
          :loading="isNewOfferLoading"
          width="200"
          id="addOfferBtn"
          @click.native="addDataOffer"
        >
          ADD DATA OFFER
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script lang="ts">
/* eslint-disable @typescript-eslint/no-explicit-any */
import Vue from "vue";
import draggable from "vuedraggable";
import CxFileDrop from "@/components/CXFileDrop.vue";
import {
  FETCH_USE_CASES,
  FETCH_ORG_ROLES,
  FETCH_SELECTED_ORG_ROLES,
  GET_DATA_OFFER_FILE,
  IS_NEW_OFFER_LOADING,
  IS_FILE_ERROR,
  IS_CREATE_OFFER_ERROR,
} from "@/store/modules/dataoffer/getters/getter-types";
import { ADD_DATA_OFFER } from "@/store/modules/dataoffer/actions/action-types";
import {
  ADD_ALL_ROLES,
  REMOVE_ALL_ROLES,
} from "@/store/modules/dataoffer/mutations/mutation-types";
import {
  iUseCase,
  iOrgRoles,
} from "@/common/interfaces/dataOffers/IDataOffers";
import { DATE_TODAY } from "@/common/util/DataOfferUtil";
import moment from "moment";
interface iFilteredItems {
  id: number;
  name: string;
  children: Array<iUseCase>;
}
interface iUsageControl {
  type: string;
  startDate?: string;
  endDate?: string;
  deleteDate?: string;
}
export default Vue.extend({
  name: "CreateDataOfferModal",
  components: { CxFileDrop, draggable },
  data: () => ({
    createOfferForm: true,
    dataOfferTitle: "",
    dataOfferDescription: "",
    accessControlByUseCase: null,
    accessControlByRole: null,
    usageControl: null,
    selectedUseCases: [],
    usageLoggingChk: false,
    date: DATE_TODAY,
    startDateMenu: false,
    endDateMenu: false,
    startDateVal: null,
    endDateVal: null,
    deleteAfterTimeFrame: false,
  }),
  props: {
    isOpen: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    useCases(): Array<iUseCase> {
      return this.$store.getters[FETCH_USE_CASES];
    },
    orgRoles(): Array<iOrgRoles> {
      return this.$store.getters[FETCH_ORG_ROLES];
    },
    selectedOrgRoles(): Array<iOrgRoles> {
      return this.$store.getters[FETCH_SELECTED_ORG_ROLES];
    },
    startDateDisp() {
      return this.startDateVal;
    },
    endDateDisp() {
      return this.endDateVal;
    },
    formattedStartDate() {
      return this.startDateVal
        ? moment(this.startDateVal).format("DD/MM/YYYY")
        : "";
    },
    formattedEndDate() {
      return this.endDateVal
        ? moment(this.endDateVal).format("DD/MM/YYYY")
        : "";
    },
    filteredItems(): iFilteredItems[] {
      const filteredArr: iFilteredItems[] = [];
      let filteredObj = {
        id: 100,
        name: "Select/Deselect all",
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        children: this.useCases,
      };
      filteredArr.push(filteredObj);
      return filteredArr;
    },
    dataOfferFile(): File {
      return this.$store.getters[GET_DATA_OFFER_FILE];
    },
    isNewOfferLoading(): boolean {
      return this.$store.getters[IS_NEW_OFFER_LOADING];
    },
    isFileError(): boolean {
      return this.$store.getters[IS_FILE_ERROR];
    },
    isTitleError(): boolean {
      return this.isFileError || !this.dataOfferTitle;
    },
    isAccessControlByUseCaseError(): boolean {
      if (
        this.accessControlByUseCase === "Limited" &&
        this.selectedUseCases.length <= 0
      )
        return true;
      return false;
    },
    isAccessControlByRoleError(): boolean {
      if (
        this.accessControlByRole === "Limited" &&
        this.selectedOrgRoles.length <= 0
      )
        return true;
      return false;
    },
    isUsageControlError(): boolean {
      if (this.usageControl === "Limited")
        return !this.deleteAfterTimeFrame || !this.formattedEndDate;
      return false;
    },
    isCreateOfferError(): boolean {
      return this.$store.getters[IS_CREATE_OFFER_ERROR];
    },
    isCreateOfferFormError(): boolean {
      return (
        !this.createOfferForm ||
        this.isTitleError ||
        this.isAccessControlByUseCaseError ||
        this.isAccessControlByRoleError ||
        this.isUsageControlError
      );
    },
  },
  methods: {
    handleScroll(event: { srcElement: { scrollTop: any } }) {
      let accessControlTab = document.querySelector(
        "#accessControl"
      ) as HTMLElement;
      let usageControlTab = document.querySelector(
        "#usageControl"
      ) as HTMLElement;
      let descriptionTab = document.querySelector(
        "#description"
      ) as HTMLElement;
      var verticalWindowOffset = event.srcElement.scrollTop;
      if (
        accessControlTab.offsetTop - 86 <= verticalWindowOffset &&
        usageControlTab.offsetTop - 86 > verticalWindowOffset
      ) {
        document.querySelector(".usageControl")?.removeAttribute("id");
        document.querySelector(".description")?.removeAttribute("id");
        document.querySelector(".fileTitle")?.removeAttribute("id");
        document
          .querySelector(".accessControl")
          ?.setAttribute("id", "activeTab");
      } else if (
        usageControlTab.offsetTop - 86 <= verticalWindowOffset &&
        descriptionTab.offsetTop - 86 > verticalWindowOffset
      ) {
        document.querySelector(".accessControl")?.removeAttribute("id");
        document.querySelector(".description")?.removeAttribute("id");
        document.querySelector(".fileTitle")?.removeAttribute("id");
        document
          .querySelector(".usageControl")
          ?.setAttribute("id", "activeTab");
      } else if (descriptionTab.offsetTop - 86 <= verticalWindowOffset) {
        document.querySelector(".accessControl")?.removeAttribute("id");
        document.querySelector(".usageControl")?.removeAttribute("id");
        document.querySelector(".fileTitle")?.removeAttribute("id");
        document.querySelector(".description")?.setAttribute("id", "activeTab");
      } else {
        document.querySelector(".accessControl")?.removeAttribute("id");
        document.querySelector(".usageControl")?.removeAttribute("id");
        document.querySelector(".description")?.removeAttribute("id");
        document.querySelector(".fileTitle")?.setAttribute("id", "activeTab");
      }
    },
    addAllRoles() {
      this.$store.commit(ADD_ALL_ROLES);
    },
    removeAllRoles() {
      this.$store.commit(REMOVE_ALL_ROLES);
    },
    resetForm() {
      const createOfferForm: any = this.$refs.createOfferForm;
      createOfferForm.resetValidation();
      createOfferForm.reset();
      this.selectedUseCases = [];
      this.removeAllRoles();
      const fileUpload: any = this.$refs.fileUpload;
      fileUpload.removeFiles();
      this.$emit("update:isOpen", false);
    },
    close() {
      this.resetForm();
    },
    addDataOffer() {
      const self = this as any;
      const createOfferForm: any = this.$refs.createOfferForm;
      if (createOfferForm.validate()) {
        const createOfferPayload = {
          file: self.dataOfferFile,
          offerRequest: {
            title: "",
            description: "",
            accessControlUseCaseType: "",
            accessControlUseCase: [],
            accessControlByRoleType: "",
            byOrganizationRole: [],
            byOrganization: [],
            usageControlType: "",
            usageControl: [] as iUsageControl[],
            contractEndsinDays: 0,
          },
        };
        createOfferPayload.offerRequest["title"] = self.dataOfferTitle;
        createOfferPayload.offerRequest["description"] =
          self.dataOfferDescription;
        createOfferPayload.offerRequest["accessControlUseCaseType"] =
          self.accessControlByUseCase;
        const accessControlUseCase = self.selectedUseCases.map(
          (useCase: { name: string }) => useCase.name
        );
        createOfferPayload.offerRequest["accessControlUseCase"] =
          self.accessControlByUseCase === "Limited" ? accessControlUseCase : [];
        createOfferPayload.offerRequest["accessControlByRoleType"] =
          self.accessControlByRole;
        const byOrganizationRole = self.selectedOrgRoles.map(
          (selectedOrgRole: { role: string }) => selectedOrgRole.role
        );
        createOfferPayload.offerRequest["byOrganizationRole"] =
          self.accessControlByRole === "Limited" ? byOrganizationRole : [];
        createOfferPayload.offerRequest["usageControlType"] = self.usageControl;
        const usageControlArr: iUsageControl[] = [];
        const usageTimeframeStart = moment(self.startDateVal)
          .utc()
          .toISOString();
        const usageTimeframeEnd = moment(self.endDateVal).utc().toISOString();
        if (self.usageLoggingChk) {
          usageControlArr.push({ type: "USAGE_LOGGING" });
        }
        if (self.usageControl === "Unlimited") {
          usageControlArr.push({ type: "PROVIDE_ACCESS" });
        } else {
          if (self.deleteAfterTimeFrame) {
            usageControlArr.push({
              type: "USAGE_UNTIL_DELETION",
              startDate: usageTimeframeStart,
              endDate: usageTimeframeEnd,
              deleteDate: usageTimeframeStart,
            });
          }
        }
        createOfferPayload.offerRequest["usageControl"] = usageControlArr;
        const contractLimitDurationInDays = moment(usageTimeframeEnd).diff(
          moment(usageTimeframeStart),
          "days"
        );
        createOfferPayload.offerRequest["contractEndsinDays"] =
          contractLimitDurationInDays;
        this.$store.dispatch(ADD_DATA_OFFER, createOfferPayload).then(() => {
          if (this.isCreateOfferError) {
            const successNotificationObj = {
              type: "error",
              duration: 3000,
              theme: "toasted-primary",
              action: {
                text: "Cancel",
                onClick: (
                  e: Event,
                  toastObject: { goAway: (arg0: number) => void }
                ) => {
                  toastObject.goAway(0);
                },
              },
            };
            this.$toasted.show(
              "Failed to create data offer",
              successNotificationObj
            );
            return;
          } else {
            const failureNotificationObj = {
              type: "success",
              duration: 3000,
              theme: "toasted-primary",
              action: {
                text: "Cancel",
                onClick: (
                  e: Event,
                  toastObject: { goAway: (arg0: number) => void }
                ) => {
                  toastObject.goAway(0);
                },
              },
            };
            this.$toasted.show(
              "Data offer created successfully",
              failureNotificationObj
            );
            this.resetForm();
          }
        });
      } else {
        return;
      }
    },
  },
});
</script>
<style lang="scss">
@import "~@/styles/variables";

nav {
  display: flex;
  justify-content: space-between;
  & ul {
    display: flex;
    align-items: center;
    justify-content: right;
    margin-right: 20px;
    & li {
      list-style: none;
      margin: 0 15px;
      & a {
        text-decoration: none;
        color: $grey8 !important;
        letter-spacing: 0.3px;
        font-size: 15px;
        padding: 0 5px;
        transition: 0.5s;
        &:hover {
          color: black !important;
        }
      }
    }
  }
}
section.createOfferSection {
  display: flex;
  min-height: 90vh;
  align-items: flex-start;
  justify-content: center;
  padding: 20px 50px;
}
.dialog-title {
  font-size: 18px;
}
#activeTab {
  border-bottom: 4px solid $brand-color-green;
  color: black !important;
  transition: 0.2s;
  padding-bottom: 12px;
}
.create-offer-modal-content {
  height: 70vh;
}
.data-offer-title {
  & .v-input__slot {
    background: $grey1 !important;
    box-shadow: none !important;
    & label {
      color: $grey9;
    }
  }
}
.link {
  text-decoration: underline;
  cursor: pointer;
}
.role-chip-group {
  min-height: 50px;
}
.selected-roles-card {
  border: 1px solid $brand-color-green !important;
}
.date-range {
  position: relative;
  top: 37px;
}
.dataoffer-description {
  & .v-input__slot {
    box-shadow: none !important;
  }
}
.greyout-box {
  background: $grey1;
  pointer-events: none;
  opacity: 0.5;
  --moz-opacity: 0.5;
  z-index: 99;
  filter: alpha(opacity=50);
}
.v-subheader.dataoffer-subheader {
  font-weight: 500;
  font-size: 16px;
  letter-spacing: 0.3px;
}
#addOfferBtn {
  background-color: $brand-color-orange;
  color: white;
}
.red-border {
  border: 1px solid red;
}
</style>
