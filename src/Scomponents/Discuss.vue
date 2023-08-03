<template>
  <div>
    <div class="discussion" v-if="songId">
      <div class="discussion-area">
        <div class="comment-area">
          <textarea type="text" placeholder="快来说点什么吧!" v-model="comment" />
        </div>
        <div class="funtion-area">
          <div class="div_button" @click="submitComment()">评论</div>
        </div>
        <hr />
        <div
          class="user-reply"
          v-for="(comment, comment_index) in comments"
          :key="comment_index"
        >
          <div class="reply-userInfo">
            <div class="reply-userName" @click="gotoMyPage">{{ comment.userName }}:</div>
          </div>
          <div class="reply-content">
            <div class="comment-user">{{ comment.comment }}</div>
            <div class="comment-likes" @click="updataLikes(comment_index)">
              Likes:{{ comment.commentLikes }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";

export default {
  props: ["songId"],
  mounted() {
    //获取歌曲评论
    this.$http({
      url: "/getComments",
      method: "post",
      params: {
        songId: this.songId,
      },
    }).then((res) => {
      if (res.data != null) {
        this.comments = res.data;
      } else {
        console.log("获取评论失败!");
      }
    });
  },

  watch: {
    songId(new_songId) {
      //获取歌曲评论
      this.$http({
        url: "/getComments",
        method: "post",
        params: {
          songId: this.songId,
        },
      }).then((res) => {
        if (res.data != null) {
          this.comments = res.data;
        } else {
          console.log("获取评论失败!");
        }
      });
    },
  },
  computed: {
    ...mapState(["now_song", "userInfo", "addedCommentIndex"]),
  },

  data: function () {
    return {
      comments: [],
      comment: "",
      userId: "",
    };
  },
  methods: {
    //添加或取消点赞
    updataLikes(comment_index) {
      /**逻辑:记录此次点赞的评论index a,b为当前用户所选的commmentindex
       * comment_index: 用户选择操作的评论index(7.17bug:切换页面再赞时可以多次赞)
       * 记录已经增加了Likes的commentId 遍历后如果有那就- 没有就+*/
      const length = this.addedCommentIndex.length;
      let isEqual = false;
      for (let i = 0; i < length; i++) {
        //选择已经赞的评论  减少
        if (comment_index == this.addedCommentIndex[i]) {
          this.comments[comment_index].commentLikes--;
          this.$store.commit("deleteCommentIndex", i);
          isEqual = true;
          break;
        }
      }
      //只有一开始的时候 增加
      if (length == 0) {
        this.comments[comment_index].commentLikes++;
        this.$store.commit("addedCommentIndex", comment_index);
      }
      //有赞过 但选择的评论此时未赞
      else if (this.addedCommentIndex.length != 0 && !isEqual) {
        this.comments[comment_index].commentLikes++;
        this.$store.commit("addedCommentIndex", comment_index);
      }
      //提交评论状态
      this.$http({
        url: "/updataCommentLikes",
        method: "post",
        data: {
          commentLikes: this.comments[comment_index].commentLikes,
          userId: this.comments[comment_index].userId,
          commentId: this.comments[comment_index].commentId,
        },
      }).then((res) => {});
    },

    //提交评论内容
    submitComment() {
      if (this.songId != "") {
        this.$http({
          url: "/submitComment",
          method: "post",
          data: {
            comment: this.comment,
            userId: this.userInfo.userId,
            songId: this.songId,
            userName: this.userInfo.userName,
          },
        }).then((res) => {
          if (res.data == 1) {
            this.RefreshComment();
            console.log("插入成功!");
          } else if (res.data == -1) {
            console.log("插入失败!");
          }
        });
      }
    },

    //刷新评论
    RefreshComment() {
      //获取歌曲评论
      this.$http({
        url: "/getComments",
        method: "post",
        params: {
          songId: this.songId,
        },
      }).then((res) => {
        if (res.data != null) {
          this.comments = res.data;
        } else {
          console.log("获取评论失败!");
        }
      });
    },

    //跳转到用户页面
    gotoMyPage() {},
  },
};
</script>

<style scoped>
/*用户评论 */
.user-reply {
  display: flex;
}

/*评论区域 */
.comment-area {
  padding: 5px;
}
/*评论框 */
.comment-area textarea {
  padding: 5px;
  border-radius: 10px;
  width: 100%;
  height: 75px;
}
/*评论区的用户信息 */
.reply-userInfo {
  width: 20%;
}
/*评论区的评论内容 */
.reply-content {
  width: 80%;
}
/*评论内容 */
.reply-userName:hover,
.reply-userName:active {
  cursor: pointer;
  color: blue;
}

.user-reply {
  border: 2px solid grey;
  border-radius: 15px;
  padding: 5px;
}

.reply-content {
  display: flex;
}

.comment-user {
  width: 90%;
}

.comment-likes {
  cursor: pointer;
  padding: 5px;
  border: 2px solid grey;
  border-radius: 10px;
}

.comment-likes:hover {
  background-color: rgb(72, 69, 69);
  color: white;
  transition: background-color 1s;
}

.comment-likes:active {
  background-color: black;
  color: white;
}
</style>
