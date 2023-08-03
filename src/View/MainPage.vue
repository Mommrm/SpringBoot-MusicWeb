<template>
  <div class="wrapper">
    <div class="mainPage">
      <div class="aside">
        <div class="aside-selectPage">
          <h2 @click="showSection('search')">搜索音乐</h2>
          <h2 @click="showSection('myMusic')">我的音乐</h2>
          <h2 @click="showSection('recommendations')">推荐音乐</h2>
          <h2 @click="showSection('searchRankings')">音乐排行榜</h2>
        </div>
      </div>
      <div class="content">
        <aside>
          <div v-show="currentSection === 'search'">
            <SearchMusic></SearchMusic>
          </div>

          <div v-show="currentSection === 'myMusic'">
            <MyMusic></MyMusic>
          </div>

          <div v-show="currentSection === 'recommendations'">
            <RMusic></RMusic>
          </div>

          <div v-show="currentSection === 'searchRankings'">
            <RankMusic></RankMusic>
          </div>
        </aside>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import SearchMusic from "../components/SearchMusic.vue";
import MyMusic from "../components/MyMusic.vue";
import RMusic from "../components/RMusic.vue";
import RankMusic from "../components/RankMusic.vue";

export default {
  computed: {
    ...mapState(["currentSection", "now_song"]),
  },

  components: {
    SearchMusic,
    MyMusic,
    RMusic,
    RankMusic,
  },
  data() {
    return {
      songId: "",
    };
  },
  methods: {
    showSection(section) {
      this.$store.commit("setCurrentSection", section);
    },
  },
};
</script>

<style scoped>
* {
  margin: 0;
  box-sizing: border-box;
}

.mainPage {
  display: flex;
  justify-content: center;
  align-content: center;
}

.aside {
  border-right-style: solid;
  border-right-width: 1px;
  border-right-color: grey;
  width: 10%;
  float: left;
  height: 100%;
}

.aside h2 {
  margin: 10px;
  text-align: center;
  cursor: pointer;
  color: black;
  font-weight: normal;
  font-size: medium;
}

.aside h2:hover {
  font-weight: bolder;
  font-size: large;
  transition: font-size 0.5s font-weight 0.5;
}

.content {
  width: 85%;
}
</style>
