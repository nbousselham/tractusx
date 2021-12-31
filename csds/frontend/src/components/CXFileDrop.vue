<template>
  <div>
    <v-card id="drop-zone" elevation="0" class="mb-2">
      <v-card-text>
        <vue-dropzone
          id="dropzone-wrapper"
          :options="dropzoneOptions"
          :useCustomSlot="true"
        >
          <div class="dropzone-custom-content">
            <p>
              <v-icon class="pt-5">$vuetify.icons.fileUploadIcon</v-icon>
            </p>
            <p class="black--text fs-16 pt-2 mb-1">Drag and drop here</p>
            <p class="black--text fs-16 mb-1">Or</p>
            <p class="browse-files fs-16">Browse files</p>
          </div>
        </vue-dropzone>
      </v-card-text>
    </v-card>
    <p class="pl-1 text--disabled">Accepted file types: CSV, JSON</p>
  </div>
</template>
<script lang="ts">
import Vue from "vue";
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
  }),
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
