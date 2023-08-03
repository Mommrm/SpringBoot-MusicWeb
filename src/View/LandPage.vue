<template>
  <div class="body">
    <div class="login" v-if="isLand == 1 || isLand == 0">
      <div class="box">
        <p class="table">欢迎登录</p>
        <div class="error_message" v-show="showMessage">
          登录失败,请检查账号密码是否错误!
        </div>
        <div class="error_message" v-show="showError">登录失败,未知异常!</div>
        <br />
        <input type="text" placeholder="账号" v-model="userInfo.account" />
        <input type="password" placeholder="密码" v-model="userInfo.password" />
        <div class="logic">
          <div class="other">
            <div class="text">
              没有账号?<a href="#/myworkspace/register" @click="Register(2)">创建一个</a>
            </div>
            <div class="text">忘记密码?<a href="#/myworkspace/retrieve">找回密码</a></div>
          </div>
          <div class="land">
            <button @click="Land">登录</button>
          </div>
        </div>
      </div>
    </div>
    <div class="register" v-else-if="2">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped>
* {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}

.body {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 80vh;
}

.login {
  border-radius: 15px;
  width: 500px;
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 0px 5px black;
  cursor: pointer;
}
/* 欢迎登录字体 */
.table {
  font: 900 40px "";
  text-align: center;
  letter-spacing: 5px;
}

.box {
  overflow: hidden;
}

.error_message {
  color: red;
}

.box input {
  font: bolder;
  border-radius: 15px;
  width: 100%;
  margin-bottom: 20px;
  outline: none;
  border: 2px solid grey;
  padding: 15px;
}

.box input:focus {
  border: 2px solid #355bf0;
}
/**登录信息输入部分 */
.logic {
  display: flex;
}
/*输入框下面的文字跳转 */
.other {
  font-size: medium;
  font-weight: bold;
  padding: 30px 5px;
  letter-spacing: 4px;
  width: 50%;
}

/**文字跳转样式 */
.text {
  padding-bottom: 20px;
}
/**登录按钮 */
.land {
  padding: 0px 40px;
  width: 50%;
}
.land button {
  width: 150px;
  margin-top: 25px;
  padding: 25px 0px;
  border-radius: 15px;
  background: #4e6ef2;
  color: white;
  text-align: center;
  font-size: larger;
  font-weight: bolder;
  box-shadow: 0 3px 9px 0 grey;
}

.land button:hover {
  background-color: #355bf0;
}

.land button:active {
  background-color: #355bf0;
  box-shadow: 0 0 white;
}
</style>

<script>
import { mapGetters } from "vuex";
import { mapMutations } from "vuex";
import { mapState } from "vuex";

export default {
  computed: {
    ...mapState(["userInfo", "setToken", "setUserInfo", "setPlaylistId"]),

    ...mapGetters(["account", "password", "token", "userId", "userName"]),
  },
  created() {
    //如果保存了用户登录token就发送请求获取用户信息
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
      });
    }
  },

  data: function () {
    return {
      //1:未按下登录按钮 0按下了登录按钮 2为跳转注册页面
      isLand: 1,
      //是否显示登录错误 默认不显示 登录失败显示赋予true
      showMessage: false,
      showError: false,
    };
  },

  methods: {
    Land() {
      this.isLand = 0;
      this.$http({
        url: "/Land",
        method: "post",
        data: {
          account: this.userInfo.account,
          password: this.userInfo.password,
        },
      })
        .then((res) => {
          console.log("LandPage");
          if (res.data) {
            this.$store.commit("setUserInfo", res.data);
            this.$store.commit("setToken", res.data.token);
            this.$store.commit("setPlaylistId", res.data.userId);
            //添加session记录用户token 且获取用户信息
            this.addUserSession(res.data.token);
            this.showMessage = false;
            console.log("登录成功");
          } else {
            console.log("登录失败");
            this.showError = false;
            this.showMessage = true;
          }
        })
        .catch((e) => {
          console.log("未知异常");
          this.showMessage = false;
          this.showError = true;
        });
    },

    Register(land_state) {
      this.isLand = land_state;
    },

    addUserSession(usertoken) {
      sessionStorage.setItem("token", usertoken);
    },
  },
};
</script>
