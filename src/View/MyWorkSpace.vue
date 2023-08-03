<template>
  <div>
    <div v-show="isLand">
      <LandArea></LandArea>
    </div>
  </div>
</template>

<script>
import LandArea from "./LandArea.vue";

export default {
  created() {},
  components: {
    LandArea,
  },
  data() {
    return {
      isLand: true,
    };
  },
  methods: {},
};
</script>
