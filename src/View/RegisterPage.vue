<template>
  <div class="body">
    <div class="register" v-if="isRegister == -1 || isRegister == 0">
      <div class="table">
        <div class="register_area">
          <div class="header">欢迎注册</div>
          <div class="goto_land_area">
            <div class="text">已经有账号了?<a href="#/myworkspace/land">去登录</a></div>
          </div>
          <form>
            <input type="text" v-model="userName" placeholder="用户名" required />
            <input type="text" v-model="account" placeholder="账号" required />
            <input type="password" v-model="password1" placeholder="密码" required />
            <input
              type="password"
              v-model="password2"
              placeholder="请再次输入密码"
              required
            />
            <input class="register_button" type="submit" value="注册" @click="register" />
          </form>
        </div>
      </div>
    </div>

    <div class="land" v-else-if="isRegister == 1">
      <h1>登录</h1>
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

.table {
  border-radius: 15px;
  width: 700px;
  height: 550px;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 0px 5px black;
  cursor: pointer;
}

.header {
  font: 900 40px "";
  text-align: center;
  letter-spacing: 5px;
}

.goto_land_area {
  margin: 20px;
  letter-spacing: 2px;
}

.table input {
  border-radius: 15px;
  width: 90%;
  margin: 0px 0px 20px 20px;
  outline: none;
  border: 2px solid grey;
  padding: 15px;
}

.table input:focus {
  border: 2px solid #355bf0;
}

.register_button {
  color: white;
  border-radius: 15px;
  background-color: #4e6ef2;
  box-shadow: 0 3px 9px 0 grey;
}

.register_button:hover {
  background-color: #355bf0;
}

.register_button:active {
  background-color: #355bf0;
  box-shadow: 0 0 white;
}
</style>

<script>
export default {
  created() {
    setInterval(() => {
      this.setRigisterState();
    }, 5000);
  },

  data: function () {
    return {
      isRegister: -1,
      userName: "",
      account: "",
      password1: "",
      password2: "",
    };
  },
  methods: {
    register() {
      if (!(this.password1 === this.password2)) {
        console.log("注册失败");
        this.isRegister = false;
      } else {
        this.$http({
          url: "/Register",
          method: "post",
          data: {
            userName: this.userName,
            account: this.account,
            password: this.password1,
          },
        }).then((res) => {
          if (res.data) {
            this.isRegister = 1;
            console.log("注册成功!");
          } else {
            this.isRegister = 0;
            console.log("注册失败!");
          }
        });
      }
    },

    setRigisterState() {
      this.isRegister = -1;
    },
  },
};
</script>
