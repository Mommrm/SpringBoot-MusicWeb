<template>
  <div>
    <div class="search-box">
      <span class="search-input">
        <input
          type="text"
          @keydown.enter="SearchNow"
          v-model="SearchMessage"
          placeholder="听点什么?"
        /> </span
      ><span class="search-button">
        <button @click="SearchNow">搜索</button>
      </span>
    </div>
    <!-- 搜索内容 -->
    <div class="search_show">
      <div class="search-content">
        <div class="search-content-header">
          <span class="header-other"></span>
          <span class="header-singer">歌手</span>
          <span class="header-song">歌曲</span>
        </div>
        <div class="search-content-box">
          <div
            class="search-content-songs"
            v-for="(song, song_index) in songs"
            :key="song_index"
          >
            <!-- 单数是有背景颜色 -->
            <div class="songs-row-color" v-show="song_index % 2 != 1">
              <div class="songs-other">
                <div class="other-index">{{ song_index + 1 }}</div>
                <div class="songs-other-button" @click="playSong(song_index)">播放</div>
                <div class="songs-other-button" @click="AddToPlaylist(song_index)">
                  添加到
                </div>
              </div>
              <div class="songs-singer">{{ song.songSinger }}</div>
              <div class="songs-song">{{ song.songName }}</div>
            </div>
            <!-- 单数是无背景颜色 -->
            <div class="songs-row-nomal" v-show="song_index % 2 != 0">
              <div class="songs-other">
                <div class="other-index">{{ song_index + 1 }}</div>
                <div class="songs-other-button" @click="playSong(song_index)">播放</div>
                <div class="songs-other-button" @click="AddToPlaylist(song_index)">
                  添加到
                </div>
              </div>
              <div class="songs-singer">{{ song.songSinger }}</div>
              <div class="songs-song">{{ song.songName }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 弹窗内容 -->
    <div class="playlist-wrapper" v-if="showModal">
      <div class="playlist-content" v-if="userInfo.token">
        <div class="select-playlist-header">
          <div class="select-playlist-text">选择歌单</div>
          <button class="select-playlist-button" @click="AddToPlaylist">关闭</button>
        </div>
        <!-- 创建歌单 -->
        <div class="create-playlist">
          <div
            class="select_myplaylist"
            @click="
              (openCreatePlaylist) => {
                if (this.OpenOrNot) {
                  this.OpenOrNot = false;
                } else {
                  this.OpenOrNot = true;
                }
              }
            "
          >
            创建歌单
          </div>
          <div class="select-playlist-input" v-show="OpenOrNot">
            <input
              type="text"
              v-model="PlaylistName"
              @keydown.enter="createPlaylist"
              placeholder="创建的歌单名"
            />
          </div>
        </div>
        <div
          v-for="(playlist, playlist_index) in myPlaylist"
          :key="playlist_index"
          class="select_myplaylist"
          @click="AddMyLove(playlist_index)"
        >
          {{ playlist.playlistName }}
        </div>
      </div>
      <div v-else class="playlist-content">请<a href="#/myworkspace">登录</a>后再试</div>
    </div>
    <div class="tip-wrapper" v-if="window_tips">
      <div class="tip-content">{{ this.tip_content }}</div>
    </div>
    <div class="playMusic" v-if="this.playUrl">
      <audio :src="this.playUrl" controls autoplay></audio>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  computed: {
    ...mapState(["userInfo", "songs"]),
  },

  created() {
    //开局先获取我的歌单
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
  data: function () {
    return {
      song_index: -1,
      AddSong_Check: -1,
      SearchMessage: "",
      LastSearchMessage: "",
      myPlaylist: [], //我的歌单
      userId: "",
      userName: "",
      playUrl: "",
      showModal: false, //控制歌单弹窗开关
      window_tips: false,
      tip_content: "",
      PlaylistName: "", //用户输入创建的歌单名
      OpenOrNot: false, //是否打开歌单输入框
    };
  },
  methods: {
    /**打开添加歌单窗口 */
    AddToPlaylist(song_index) {
      this.song_index = song_index;
      if (this.showModal) {
        this.showModal = false;
      } else {
        this.showModal = true;
      }
    },

    /**把要播放的歌曲传给vuex管理 */
    playSong(song_index) {
      const audio = new Audio(this.songs[song_index].songUrl);
      //当没有音乐资源时弹窗提示
      audio.addEventListener("error", () => {
        this.window_tips = true;
        this.tip_content = "没有音乐资源";
        setTimeout(() => {
          this.window_tips = false;
        }, 1000);
      });
      audio.addEventListener("loadeddata", () => {
        this.$store.commit("setNow_song", this.songs[song_index]);
        this.getSongLyrics(this.songs[song_index].songId);
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
        if (res.data == "" || res.data == null) {
          const temp = [];
          temp[0] = res.data;
          this.$store.commit("setlyric", temp);
        } else {
          const lyric = res.data.split("\n");
          this.$store.commit("setlyric", lyric);
        }
      });
    },

    /**搜索 */
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
    },

    //添加到歌单
    AddMyLove(playlist_index) {
      this.$http({
        url: "/insertMusic",
        method: "post",
        data: {
          songName: this.songs[this.song_index].songName,
          songSinger: this.songs[this.song_index].songSinger,
          songUrl: this.songs[this.song_index].songUrl,
          songId: this.songs[this.song_index].songId,
          userId: this.userInfo.userId,
          userName: this.userInfo.userName,
          playlistId: this.myPlaylist[playlist_index].playlistID,
        },
      }).then((res) => {
        if (res.data == 1) {
          console.log("添加成功");
          this.showModal = false;
        } else if (res.data == 0) {
          console.log("添加失败");
        }
      });
    },
    createPlaylist() {
      this.$http({
        url: "/addPlaylist",
        method: "post",
        params: {
          playlistId: this.userInfo.userId,
          playlistName: this.PlaylistName,
          userId: this.userInfo.userId,
        },
      }).then((res) => {
        if (res.data) {
          console.log("创建成功!");
        } else {
          console.log("创建失败!");
        }
      });
    },
  },
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

/**搜索盒子 */
.search-box {
  display: flex;
  padding: 5px;
  width: 100%;
  height: 10%;
  justify-content: center;
}
/* 搜索输入框 */
.search-input {
  width: 80%;
}

.search-input input {
  width: 15%;
  padding: 10px;
  border-radius: 10px;
  transition: width ease 1s;
}

.search-input input:hover {
  width: 100%;
}

/* 搜索确认按钮 */
.search-button button {
  background-color: transparent;
  cursor: pointer;
  padding: 9px;
  margin: 0px 10px;
  border-radius: 10px;
  border: 1px solid grey;
}

.search-button button:hover {
  border-width: 2px;
}
.search-button button:active {
  border-width: 2px;
  font-weight: bold;
}

/*搜索结果盒子模型 */
.search_show {
  border: 1px solid grey;
  align-content: center;
  justify-content: center;
  width: 100%;
  padding: 0;
  margin: 0;
}

/**歌单内容表头 */
.search-content-header {
  display: flex;
  height: 8%;
  color: black;
  padding: 0px 5px 5px 5px;
  margin: 0px 0px 5px 0px;
  border: 2px solid grey;
}
/**其他按钮 */
.header-other {
  width: 20%;
}
/**歌名 */
.header-song {
  width: 50%;
}
/**歌手 */
.header-singer {
  width: 30%;
}

/**搜索内容盒子 */
.search-content-box {
  height: 30rem;
  overflow: hidden;
  overflow: auto;
}

/**歌单显示每一首歌曲 */
.search-content-songs {
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
  padding: 4.5px 15px;
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

/**歌单选择框样式 */
.select_myplaylist {
  cursor: pointer;
  border-radius: 5px;
  border: 2px solid;
  padding: 5px 10px;
  margin: 5px;
}

.select_myplaylist:hover {
  background-color: azure;
}

/*选择歌单 */
.playlist-wrapper {
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

.playlist-content {
  width: 20%;
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
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
/* 选择添加歌单 */
.select-playlist-header {
  justify-content: center;
  display: flex;
  padding: 5px;
  margin: 5px;
}

.select-playlist-text {
  font-size: x-large;
}
/* 关闭按钮 */
.select-playlist-button {
  margin: 0px 10px;
  border-radius: 50%;
  box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);
}
/* 歌单名字输入框 */
.select-playlist-input input {
  margin: 0;
  padding: 10px 0px;
  border: 1px solid grey;
  width: 95%;
}
</style>
