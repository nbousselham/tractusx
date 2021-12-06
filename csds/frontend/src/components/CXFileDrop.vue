<template>
  <v-card
    id="drop-zone"
    @drop.prevent="onDrop($event)"
    @dragover.prevent="dragover = true"
    @dragenter.prevent="dragover = true"
    @dragleave.prevent="dragover = false"
    :class="{ 'grey lighten-2': dragover }"
    elevation="0"
    class="mb-2"
  >
    <v-card-text>
      <v-row class="d-flex flex-column" dense align="center" justify="center">
        <template v-if="!isFileDropped">
          <v-icon :class="[dragover ? 'mt-2, mb-6' : 'mt-5']" size="60"
            >mdi-cloud-upload</v-icon
          >
          <p>Drag and drop here</p>
          <p>Or</p>
          <p>Browse files</p>
        </template>
        <template v-else>
          <p>{{ uploadedFiles }}</p>
        </template>
      </v-row>
    </v-card-text>
    <v-card-actions></v-card-actions>
  </v-card>
</template>
<script lang="ts">
import Vue from "vue";
export default Vue.extend({
  name: "CXFileDrop",
  data: () => ({
    dragover: false,
    uploadedFiles: [],
    isFileDropped: false,
  }),
  props: {
    multiple: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    onDrop(e: {
      dataTransfer: {
        files: {
          length: number;
          forEach: (arg0: (element: never) => number) => void;
        };
      };
    }) {
      this.dragover = false;

      if (this.uploadedFiles.length > 0) this.uploadedFiles = [];

      if (!this.multiple && e.dataTransfer.files.length > 1) {
        alert("Only one file can be uploaded at a time..");
      } else {
        this.isFileDropped = true;
        console.log(e);
        e.dataTransfer.files.forEach((element: never) =>
          this.uploadedFiles.push(element)
        );
      }
    },
  },
});
</script>
<style lang="scss">
@import "~@/styles/variables";

#drop-zone {
  width: 490px;
  height: 240px;
  background: $grey1;
  border: 4px dashed $brand-color-green;
  box-sizing: border-box;
  border-radius: 4px;
}
</style>
