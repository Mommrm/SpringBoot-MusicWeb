<template>
  <div>
    <div v-if="userInfo.token">
      <div class="myPlaylist">
        <!--歌单内容 -->
        <div class="myMusic"><MyMusic></MyMusic></div>
      </div>
    </div>
    <div v-else>
      <div class="landArea">
        <LandPage></LandPage>
      </div>
    </div>
  </div>
</template>

<style scoped>
* {
  box-sizing: border-box;
}

.select_MyPlaylist {
  float: left;
}

.myInfo {
  float: right;
}
</style>

<script>
import { mapState } from "vuex";
import LandPage from "./LandPage.vue";
import MyMusic from "@/components/MyMusic.vue";

export default {
  computed: {
    ...mapState(["userInfo"]),
  },

  components: {
    LandPage,
    MyMusic,
  },

  created() {},

  data: function () {
    return {};
  },

  methods: {},
};
</script>
