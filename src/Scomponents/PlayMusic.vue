<template>
  <div>
    <!-- 添加播放-----播放区域 -->
    <div class="playMusic" v-if="this.playUrl">
      <audio
        ref="audioPlayer"
        :src="this.playUrl"
        @timeupdate="updateProgress"
        autoplay
      ></audio>
      <!-- 播放显示窗口 -->
      <div class="play-windows">
        <div class="song">
          <div class="songInfo">
            <div class="audio-songName">{{ songName }}</div>
            <div class="audio-songSinger">{{ songSinger }}</div>
            <div class="play-controls">
              <div class="play-button" @click="toggleAudio">
                {{ audioPaused ? "播放" : "暂停" }}
              </div>
            </div>
            <!-- 歌词lyric -->
            <div class="song-lyric">
              <div class="lyric-text">{{ this.lyric }}</div>
            </div>
          </div>
        </div>
        <!-- 进度条 -->
        <div class="progress-bar" ref="progressBar" @click="toAimProgress">
          <div
            class="progress"
            :style="{ width: (pro_currentTime / pro_duration) * 100 + '%' }"
          ></div>
        </div>
        <div class="song-Time">{{ currentTime }} / {{ duration }}</div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  computed: {
    ...mapState(["now_song"]),
  },

  created() {
    setInterval(() => {
      this.updataNow_song();
    }, 500);
  },
  watch: {
    currentTime(new_currentTime) {},
    playUrl(new_playUrl) {
      this.lyric_index = 0;
    },
  },
  data: function () {
    return {
      pro_currentTime: 0, //pro歌词进度条的记录时间戳
      pro_duration: "",
      currentTime: "", //显示时间 格式化过 是字符型
      duration: "",
      playUrl: "",
      songName: "",
      songSinger: "",
      audioPaused: false,
      currentLyric: "",
      lyric: "",
      lyric_index: 0, //记录歌词数组下标
    };
  },
  methods: {
    //@timeupdate触发 粗略计算是一秒执行4次 每次间隔0.25毫秒
    updateProgress() {
      const audio = this.$refs.audioPlayer;
      this.pro_currentTime = audio.currentTime;
      this.pro_duration = audio.duration;
      this.duration = this.formattedTime(audio.duration);
      this.currentTime = this.formattedTime(audio.currentTime);

      //判断是否为第一句歌词是否为空或不存在 是则显示~~~~~ 并跳转下一条歌词
      if (
        this.now_song.lyric[this.lyric_index] == "" ||
        this.now_song.lyric[this.lyric_index] == null
      ) {
        this.lyric = "~~~~~~";
        this.lyric_index++;
      } else {
        //不为空或不存在 则把时间和歌词提取出来
        const matches = this.now_song.lyric[this.lyric_index].match(
          /\[(\d{2}):(\d{2})\.\d{0,3}\](.+)/
        );
        //判断提取出来的字符串数组是否存在
        if (matches == null) {
          this.lyric = "~~~~~~";
          this.lyric_index++;
        } else {
          // 不为空 处理字符串 得到歌词时间time 歌词lyric
          const time = `${matches[1]}:${matches[2]}`;
          const lyric = matches[3].trim();
          // console.log("time: " + time + " currentTIme: " + this.currentTime);
          // console.log("lyric: " + lyric);
          if (time == this.currentTime || time == "00:00") {
            this.lyric = lyric;
            this.lyric_index++;
          }
        }
      }
    },
    //把秒格式化为分
    formattedTime(time) {
      const minutes = Math.floor(time / 60);
      const seconds = Math.floor(time % 60);
      return `${minutes > 10 ? "" : "0"}${minutes}:${seconds < 10 ? "0" : ""}${seconds}`; //
    },
    toggleAudio() {
      const audioPlayer = this.$refs.audioPlayer;
      //开始显示的是暂停按钮 false 点击后停止播放 显示为播放 true
      if (this.audioPaused) {
        audioPlayer.play();
        this.audioPaused = false;
      } else {
        audioPlayer.pause();
        this.audioPaused = true;
      }
    },
    //更新当前播放的歌曲信息
    updataNow_song() {
      this.playUrl = this.now_song.songUrl;
      this.songName = this.now_song.songName;
      this.songSinger = this.now_song.songSinger;
    },
    //跳转到用户点击的播放进度
    toAimProgress(event) {
      //获取进度条和播放器的对象
      const audio = this.$refs.audioPlayer;
      const progressBar = this.$refs.progressBar;
      //获取点击的width 和 进度条总width 求得点击的位置占歌曲的百分比
      const progressBarWidth = progressBar.offsetWidth;
      const clickPosition = event.offsetX;
      const progressPercentage = clickPosition / progressBarWidth;
      //利用百分比求得跳转的时间戳
      audio.currentTime = audio.duration * progressPercentage;
      this.pro_currentTime = audio.duration * progressPercentage;
      this.currentTime = this.formattedTime(this.pro_currentTime);
      //更新歌词 找出跳转的大概时间歌词下标
      let to_index;
      if (progressPercentage < 0.01) {
        this.lyric_index = 0;
      } else {
        to_index = parseInt(this.now_song.lyric.length * progressPercentage);
      }
      //循环判断跳转的时间戳在哪个歌词时间端内 找到后歌词赋予左边时间戳的歌词
      //图示： |  .    |    |   |   （|为歌词时间戳 .为用户选择的跳转时间戳 ||之间是左|的歌词时间段
      try {
        while (true) {
          if (
            this.pro_currentTime > this.getFloatTime(to_index)[0] &&
            this.pro_currentTime < this.getFloatTime(to_index + 1)[0]
          ) {
            this.lyric_index = to_index + 1;
            this.lyric = this.getFloatTime(this.lyric_index - 1)[1];
            break;
          } else if (
            this.pro_currentTime > this.getFloatTime(to_index - 1)[0] &&
            this.pro_currentTime < this.getFloatTime(to_index)[0]
          ) {
            this.lyric_index = to_index;
            this.lyric = this.getFloatTime(this.lyric_index - 1)[1];
            break;
          } else if (this.pro_currentTime > this.getFloatTime(to_index + 1)[0]) {
            if (to_index > this.now_song.lyric.length) {
              to_index = this.now_song.lyric.length - 1;
              break;
            }
            to_index++;
            continue;
          } else if (this.pro_currentTime < this.getFloatTime(to_index - 1)[0]) {
            if (to_index <= 0) {
              to_index = 0;
              break;
            }
            to_index--;
          }
        }
      } catch (e) {
        //无歌词异常 不需要处理
      }
    },
    //从歌词数组中提取时间和歌词
    getFloatTime(to_index) {
      const tempMap = [];
      let matches = this.now_song.lyric[to_index].match(
        /\[(\d{2}):(\d{2})\.\d{0,3}\](.+)/
      );
      if (matches != null) {
        tempMap[0] = parseFloat(`${matches[1]}` * 60) + parseFloat(`${matches[2]}`);
        tempMap[1] = matches[3].trim();
        return tempMap;
      }
    },
  },
};
</script>

<style scoped>
/*播放视窗 */
.playMusic {
  float: bottom;
}

/*播放歌曲时候的歌曲文字信息 */
.songInfo {
  display: flex;
  padding: 5px;
}

/*歌手文字信息 */
.audio-songSinger {
  padding: 0px 5px;
  cursor: pointer;
}

.audio-songSinger:active {
  cursor: pointer;
  font-weight: bolder;
}

.play-button {
  cursor: pointer;
  padding: 3px;
  border: 1px solid grey;
  border-radius: 8px;
  box-shadow: 5px grey;
}

.play-button:active {
  box-shadow: 0px;
  background-color: white;
}

/*进度 */
.progress {
  border: 1px solid #0291d8;
  border-radius: 30px;
  height: 100%;
  background-color: #00aaff;
}

/*进度条 */
.progress-bar {
  cursor: pointer;
  border: 1px solid black;
  border-radius: 5px;
  width: 100%;
  height: 10px;
  background-color: rgba(255, 255, 255, 0.1);
}

/**歌词盒子 */
.song-lyric {
  margin: 0px 20px;
}
/* 歌词文字 */
.lyric-text {
  font-size: x-large;
}
</style>
