<template>
  <div class="body">
    <div class="myMusic-body" v-if="userInfo.token">
      <div class="select_area">
        <div class="myPlaylist_box">
          <div class="myPlaylist_select">
            <div class="header">
              <h2>歌单</h2>
            </div>
            <div
              v-for="(playlist, playlist_index) in myPlaylist"
              :key="playlist_index"
              class="div_button"
              @click="to_index_playlist(playlist.playlistID)"
            >
              <div class="dropdown">
                <div class="dropbtn">
                  {{ playlist.playlistName }}
                </div>
                <div class="dropdown-content">
                  <div class="delete_button">
                    <p @click="openCheck(playlist.playlistID, playlist.playlistName)">
                      删除
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="other_function">
            <div class="div_button" @click="changeState">创建歌单</div>
            <div class="addArea" v-show="addState == 1">
              <input
                class="playlist_input"
                v-model="playlistName"
                placeholder="起个什么名字?"
                required
              />
              <div class="div_button" @click="addplaylist">创建</div>
            </div>
          </div>
        </div>
      </div>
      <div class="myPlaylist" v-if="userInfo.token && openPlaylist == true">
        <!-- 展示 歌曲 -->
        <div class="playlist-content">
          <div class="playlist-content-header">
            <span class="header-other"></span>
            <span class="header-singer">歌手</span>
            <span class="header-song">歌曲</span>
          </div>
          <div
            class="playlist-content-songs"
            v-for="(song, song_index) in MySongs"
            :key="song_index"
          >
            <!-- 偶数 -->
            <div class="songs-row-nomal" v-show="song_index % 2 != 1">
              <div class="songs-other">
                <div class="other-index">{{ song_index + 1 }}</div>
                <div class="songs-other-button" @click="playMusic(song_index)">播放</div>
                <div class="songs-other-button" @click="deleteMusic(song_index)">
                  删除
                </div>
              </div>
              <div class="songs-singer">{{ song.songSinger }}</div>
              <div class="songs-song">{{ song.songName }}</div>
            </div>
            <div class="songs-row-color" v-show="song_index % 2 != 0">
              <div class="songs-other">
                <div class="other-index">{{ song_index + 1 }}</div>
                <div class="songs-other-button" @click="playMusic(song_index)">播放</div>
                <div class="songs-other-button" @click="deleteMusic(song_index)">
                  删除
                </div>
              </div>
              <div class="songs-singer">{{ song.songSinger }}</div>
              <div class="songs-song">{{ song.songName }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="notLand">
      <h1>请<a href="#/myworkspace">登录</a>后再试</h1>
    </div>
    <!-- 提示窗口 -->
    <div class="tip-wrapper" v-if="window_tips">
      <div class="tip-content">{{ tip_content }}</div>
    </div>
    <div class="tip-wrapper" v-if="window_check">
      <div class="tip-content">
        {{ tip_content }}
        <div class="div_buttons">
          <button class="div_button" @click="deleteMyPlaylist()">确认</button>
          <button class="div_button" @click="closeCheck()">取消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
* {
  padding: 0px;
  margin: 0px;
  box-sizing: border-box;
}
/*整体 */
.myMusic-body {
  padding: 10px;
  display: flex;
  margin: 5px;
  border-radius: 25px;
}

/*没有登录时显示 */
.notLand {
  display: flex;
  align-content: center;
  justify-content: center;
}

/**用户歌单选择 */
.select_area {
  align-content: center;
  justify-content: center;
  width: 15%;
}

/**歌单内容部分 */
.myPlaylist {
  border: 1px solid grey;
  align-content: center;
  justify-content: center;
  width: 80%;
  padding: 0;
  margin: 0;
}

.playlist-content {
  display: block;
  width: 100%;
}
/**歌单内容表头 */
.playlist-content-header {
  display: flex;
  height: 8%;
  color: black;
  padding: 0px 5px 5px 5px;
  margin: 0px 0px 5px 0px;
  border: 2px solid grey;
}

.header-other {
  width: 20%;
}

.header-song {
  width: 50%;
}

.header-singer {
  width: 30%;
}

/**歌单显示每一首歌曲 */
.playlist-content-songs {
  overflow: auto;
  overflow: hidden;
  display: block;
  color: black;
}

/**具体每一行歌曲怎么展示 */
/**偶数 */
.songs-row-nomal {
  display: flex;
  height: 10%;
  width: 100%;
}
/**奇数 */
.songs-row-color {
  display: flex;
  height: 10%;
  width: 100%;
  color: balck;
  background-color: rgb(221, 221, 221);
}
.songs-row-nomal:hover,
.songs-row-color:hover {
  background-color: rgb(174, 174, 174);
}

/**other功能 */
.songs-other {
  display: flex;
  width: 20%;
}
/**歌曲序号 */
.other-index {
  width: 5%;
  padding: 4.5px 10px;
}
/**歌曲other操作按钮 */
.songs-other-button {
  width: 30%;
  padding: 4px;
  margin: 0px 5px;
  cursor: pointer;
}
.songs-other-button:hover {
  font-weight: bold;
  border-bottom-width: 1px;
  border-bottom-style: solid;
}

/**歌曲名字 */
.songs-song {
  width: 50%;
}
/**歌手 */
.songs-singer {
  width: 30%;
}

/**用户选择歌单标题字体 */
.header {
  font-size: larger;
  font-weight: bolder;
}

/*按钮通用样式 */
.div_button {
  cursor: pointer;
  color: black;
  margin: 10px;
  border: 2px solid grey;
  border-radius: 15px;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  box-shadow: 0 3px 9px 0 grey;
}

/**下拉删除 */
.dropdown {
  position: relative;
  display: inline-block;
}
/* 隐藏菜单内容 */
.dropdown-content {
  display: none;
  position: absolute;
  color: black;
  border: 1px solid grey;
  border-radius: 5px;
  min-width: 20%;
  background-color: #f9f9f9;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
}
/* 删除图标 */
.dropdown-content p {
  padding: 5px;
  display: block;
}

/* 悬浮在歌单按钮上 */
.dropdown:hover .dropdown-content {
  display: block;
}

.div_button:hover {
  transition: background-color ease 1s;
  border-color: white;
  color: white;
  background-color: grey;
}

.div_button:active {
  background-color: black;
  color: white;
  box-shadow: 0 3px 9px 0 white;
}

.img_button {
  width: 30px;
  height: auto;
}

/*歌单输入框 */
.playlist_input {
  border-radius: 15px;
  border: 3px solid black;
  padding: 15px;
  width: 100%;
}

.playlist_input:focus {
  width: 100%;
  border: 3px solid blue;
}

/**消息弹窗 */
.tip-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5); /* 半透明黑色背景 */
}

.tip-content {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
</style>

<script>
import { mapState } from "vuex";

export default {
  //定义变量
  data: function () {
    return {
      MySongs: [],
      playUrl: "",
      songId: "", //记录下当前播放
      songName: "",
      songSinger: "",
      playlistId: "", //选择的歌单Id
      myPlaylist: [], //获取到的歌单
      playlistName: "", //歌单名字
      addState: 0, //添加歌单状态 0时不会显示输入框(输入歌单名字) 1时显示
      userId: "",
      openPlaylist: false, //打开歌单开关
      window_tips: false, //提示窗口
      tip_content: "", //动态更改tip窗口的内容
      window_check: false, //打开确认窗口
    };
  },
  //监控
  watch: {
    playlistId(newplaylistId) {
      this.getData();
    },
    userId() {
      getUserPlaylist();
    },
  },
  computed: {
    ...mapState(["userInfo", "ExitLand", "now_song"]),
  },
  //开始就发送请求歌单数据
  created() {
    //如果记录到已经登录
    if (sessionStorage.getItem("token")) {
      this.$store.commit("setToken", sessionStorage.getItem("token"));
      this.$http({
        url: "/getUserInfo",
        method: "post",
        params: {
          token: sessionStorage.getItem("token"),
        },
      }).then((res) => {
        //将用户信息保存到vuex中
        this.$store.commit("setUserInfo", res.data);
        this.$store.commit("setPlaylistId", res.data.userId);
        this.$http({
          url: "/getMyPlaylist",
          method: "post",
          params: {
            userId: this.userInfo.userId,
          },
        }).then((res) => {
          this.myPlaylist = res.data;
        });
      });
    }
  },

  //方法定义
  methods: {
    //跳转到我的页面
    gotoMyPage() {
      this.$router.push("/message");
    },

    //设置播放地址
    playMusic(song_index) {
      const audio = new Audio(this.MySongs[song_index].songUrl);
      //当没有音乐资源时弹窗提示
      audio.addEventListener("error", () => {
        this.window_tips = true;
        this.tip_content = "没有音乐资源";
        setTimeout(() => {
          this.window_tips = false;
        }, 1000);
      });
      audio.addEventListener("loadeddata", () => {
        this.songId = this.MySongs[song_index].songId;
        //提交到vuex保存当前播放歌曲
        this.$store.commit("setNow_song", this.MySongs[song_index]);
        this.getSongLyrics(this.MySongs[song_index].songId);
      });
    },
    //获取歌词
    getSongLyrics(songId) {
      this.$http({
        url: "/getlyric",
        method: "post",
        params: {
          songId: songId,
        },
      }).then((res) => {
        if (res.data == "无歌词，请欣赏!" || res.data == null) {
          const temp = [];
          temp[0] = res.data;
          this.$store.commit("setlyric", temp);
        } else {
          const lyric = res.data.split("\n");
          // console.log(lyric);
          this.$store.commit("setlyric", lyric);
        }
      });
    },
    //删除歌曲
    deleteMusic(song_index) {
      this.$http({
        url: "/deleteMyMusic",
        method: "post",
        params: {
          songId: this.MySongs[song_index].songId,
          playlistId: this.playlistId,
        },
      }).then((res) => {
        if (res.data) {
          this.getData();
        } else {
        }
      });
      this.getData();
    },
    //获取歌单歌曲
    getData() {
      this.$http({
        url: "/getMySongData",
        method: "post",
        params: {
          playlistId: this.playlistId,
        },
      }).then((res) => {
        this.MySongs = res.data;
      });
    },

    //跳转对应歌单
    to_index_playlist(playlistId) {
      this.songId = "";
      this.playlistId = playlistId;
      this.openPlaylist = true;
      this.showContextMenu = false;
    },
    //改变添加状态
    changeState() {
      //弹出创建歌单填写框
      if (this.addState == 0) {
        this.addState = 1;
      } else if (this.addState == 1) {
        this.addState = 0;
      }
    },
    //添加歌单
    addplaylist() {
      if (this.playlistName != "") {
        this.$http({
          url: "/addPlaylist",
          method: "post",
          params: {
            playlistId: this.userInfo.userId, //后端根据userId进行拼接确定playlistId
            playlistName: this.playlistName,
            userId: this.userInfo.userId,
          },
        }).then((res) => {
          if (res.data) {
            this.RefreshPlaylist();
            this.addState = 0;
          } else {
            this.addState = 0;
          }
        });
      }
    },
    //刷新获取歌单
    RefreshPlaylist() {
      this.$http({
        url: "/getMyPlaylist",
        method: "post",
        params: {
          userId: this.userInfo.userId,
        },
      }).then((res) => {
        this.myPlaylist = res.data;
      });
    },
    //获取用户歌单
    getUserPlaylist() {
      if (this.userInfo.userId) {
        //获取歌单歌曲
        this.$http({
          url: "/getMySongData",
          method: "post",
          params: {
            playlistId: this.userInfo.playlistId,
          },
        }).then((res) => {
          this.MySongs = res.data;
        });
        this.$http({
          url: "/getMyPlaylist",
          method: "post",
          params: {
            userId: this.userInfo.userId,
          },
        }).then((res) => {
          this.myPlaylist = res.data;
        });
      } else {
        console.log("请登录后再试");
      }
    },
    //打开删除歌单确认
    openCheck(playlistID, playlistName) {
      this.tip_content = "是否删除: " + playlistName + "?";
      this.playlistId = playlistID;
      this.window_check = true;
    },
    //关闭删除歌单窗口
    closeCheck() {
      this.window_check = false;
    },
    //删除歌单请求
    deleteMyPlaylist() {
      this.$http({
        url: "/deletePlaylist",
        method: "post",
        params: {
          playlistId: this.playlistId,
          userId: this.userInfo.userId,
        },
      }).then((res) => {
        if (res.data != -1) {
          this.window_check = false;
          this.RefreshPlaylist();
        } else if (res.data == -1) {
          this.window_tips = true;
          this.window_check = false;
          this.tip_content = "默认歌单无法删除！";
          setTimeout(() => {
            this.window_tips = false;
          }, 1000);
        }
      });
    },
  },
};
</script>
