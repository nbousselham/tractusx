<template>
  <div>
    <v-card id="drop-zone" elevation="0" class="mb-2">
      <v-card-text>
        <vue-dropzone
          ref="dropzoneWrapper"
          id="dropzone-wrapper"
          :options="dropzoneOptions"
          :useCustomSlot="true"
          @vdropzone-success="afterSuccess"
          @vdropzone-error="afterError"
        >
          <div class="dropzone-custom-content">
            <p>
              <v-icon class="pt-5">$vuetify.icons.fileUploadIcon</v-icon>
            </p>
            <p class="black--text font-weight-bold fs-16 pt-2 mb-1">
              Drag and drop here
            </p>
            <p class="black--text font-weight-bold fs-16 mb-1">Or</p>
            <p class="browse-files font-weight-bold fs-16">Browse files</p>
          </div>
        </vue-dropzone>
      </v-card-text>
    </v-card>
    <div
      v-if="isError"
      class="v-messages theme--light error--text"
      role="alert"
    >
      <div class="v-messages__wrapper">
        <div class="v-messages__message">{{ fileErrorMessage }}</div>
      </div>
    </div>
    <p class="pl-1 text--disabled">Accepted file types: CSV, JSON</p>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
import {
  SET_DATA_OFFER_FILE,
  SET_FILE_ERROR,
} from "@/store/modules/dataoffer/mutations/mutation-types";
// import vue2Dropzone from "vue2-dropzone";
import "vue2-dropzone/dist/vue2Dropzone.min.css";
// eslint-disable-next-line @typescript-eslint/no-var-requires
const vue2Dropzone = require("vue2-dropzone");

export default Vue.extend({
  name: "CXFileDrop",
  components: {
    vueDropzone: vue2Dropzone,
  },
  data: () => ({
    dropzoneOptions: {
      url: "https://httpbin.org/post",
      uploadMultiple: false,
      acceptedFiles: [".json", ".csv"].join(","),
      disablePreviews: true,
    },
    fileErrorMessage: "You can't upload files of this type.",
    isError: false,
  }),
  methods: {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    afterSuccess(file: any) {
      this.isError = false;
      const uploadedFile = file ? file : undefined;
      this.$store.commit(SET_DATA_OFFER_FILE, uploadedFile);
      this.$store.commit(SET_FILE_ERROR, false);
    },
    afterError(file: any) {
      this.isError = true;
      this.removeFiles();
      this.$store.commit(SET_FILE_ERROR, true);
    },
    removeFiles() {
      const dropzoneWrapper: any = this.$refs.dropzoneWrapper;
      dropzoneWrapper.removeAllFiles();
    },
  },
});
</script>
<style lang="scss">
@import "~@/styles/variables";

#drop-zone {
  width: 518px;
  background: $grey1;
  border: 4px dashed $brand-color-green;
  box-sizing: border-box;
  border-radius: 4px;

  & .dropzone .dz-preview {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    height: 0;
    margin: 0;
  }

  & .vue-dropzone > .dz-preview .dz-details {
    display: flex;
    align-items: center;
    flex-direction: column;
    background: $grey1;
    color: black;
    font-size: 18px;
  }
  & .dropzone .dz-preview .dz-details .dz-size {
    display: none;
  }

  & .dropzone-custom-content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
  }

  & .browse-files {
    color: $brand-color-orange;
  }

  & #dropzone-wrapper {
    width: 100%;
    background: $grey1;
    border: none;
  }
}
.fs-16 {
  font-size: 16px;
}
</style>
