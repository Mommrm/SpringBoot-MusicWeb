<template>
  <div>
    <div class="body">
      <div class="main">
        <!--状态头-->
        <div class="nav">
          <router-link to="/mainpage"><p>主页</p></router-link>
          <router-link to="/myworkspace"><p>个人空间</p></router-link>
          <router-link to="/message"><p>消息中心</p></router-link>
          <input
            class="search_input"
            type="text"
            v-model="SearchMessage"
            @keydown.enter="SearchNow"
            :placeholder="recommend_text"
          />
          <div class="mylogo">
            <span><h1>MoMusic</h1></span>
            <span><img id="Logo" src="../resource/image/Logo.png" alt="LOGO" /></span>
          </div>
        </div>
        <hr />
        <div>
          <!--显示页面-->
          <router-view></router-view>
        </div>
      </div>
      <div class="play-box" v-if="this.now_song.songUrl">
        <div class="play_area">
          <PlayMusic></PlayMusic>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PlayMusic from "@/Scomponents/PlayMusic.vue";
import { mapState } from "vuex";

export default {
  components: {
    PlayMusic,
  },

  computed: {
    ...mapState(["songs", "currentSection", "now_song"]),
  },
  data() {
    return {
      SearchMessage: "",
      LastSearchMessage: "",
      recommend_text: "听点什么?",
    };
  },
  methods: {
    SearchNow() {
      if (this.SearchMessage !== "" && this.LastSearchMessage !== this.SearchMessage) {
        this.LastSearchMessage = this.SearchMessage;
        this.$http({
          url: "/search",
          method: "post",
          data: {
            songName: this.SearchMessage,
          },
        }).then((res) => {
          this.$store.commit("setSongs", res.data);
        });
      }
      this.$store.commit("setCurrentSection", "search");
      this.$router.push("/mainpage");
    },
  },
};
</script>

<style>
* {
  padding: 0;
  margin: 0;
  position: relative;
  box-sizing: border-box;
}

#Logo {
  width: 50px;
  height: 50px;
  border: 1px solid gray;
  border-radius: 50%;
  margin: 0px 20px;
}

.main {
  height: 50rem;
  margin: 20px;
  border-radius: 30px;
  border: 1px solid grey;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}

/**LOGO */
.mylogo {
  display: flex;
  color: black;
  float: right;
}

.mylogo h1 {
  color: transparent;
  -webkit-text-stroke-width: 2px;
  -moz-text-stroke-width: 2px;
  -webkit-text-stroke-color: #333;
  -moz-text-stroke-color: #333;
}

/**导航栏 */
.nav {
  width: 100%;
  margin: 10px;
  padding: 10px;
  overflow: hidden;
  display: inline-block;
}
/**导航文字 */
.nav p {
  float: left;
  display: block;
  color: black;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
}
.nav p:hover {
  font-weight: bold;
  font-size: large;
}

.search_input {
  padding: 10px;
  border: 2px solid grey;
  border-radius: 15px;
  width: auto;
  width: 20%;
}
/*歌曲播放区域 */
.play-box {
  display: flex;
  justify-content: center;
}

.play_area {
  position: fixed;
  bottom: 0;
  width: 90%;
  padding: 5px;
  margin: 10px 0px 0px 0px;
  color: black;
  border: 2px solid grey;
  border-radius: 20px;
  box-shadow: 8px 8px 8px rgba(0, 0, 0, 0.3);
}
</style>
