<template>
  <main class="login-wrapper">
    <div class="title">
      <h2>安阅书屋 | 登录</h2>
    </div>
    <div class="select-tab">
      <div
        class="tab"
        @click="activeTab = 0"
        :class="{ active: activeTab == 0 }"
      >
        登录
      </div>
      <div
        class="tab"
        @click="activeTab = 1"
        :class="{ active: activeTab == 1 }"
      >
        注册
      </div>
    </div>
    <div class="form-wrapper" v-if="activeTab == 0">
      <div class="form-item">
        <span class="form-label">邮箱</span>
        <div class="form-widget">
          <input type="text" v-model.trim="loginForm.email" />
        </div>
      </div>
      <div class="form-item">
        <span class="form-label">密码</span>
        <div class="form-widget">
          <input type="password" v-model.trim="loginForm.password" />
        </div>
      </div>
      <div class="form-item">
        <div class="form-widget">
          <button
            class="form-button"
            style="margin-left: 80px"
            @click="submitLogin"
          >
            登录
          </button>
        </div>
      </div>
    </div>
    <div class="form-wrapper" v-else>
      <div class="form-item">
        <span class="form-label">用户名</span>
        <div class="form-widget">
          <input type="text" v-model.trim="registerForm.username" />
        </div>
      </div>
      <div class="form-item">
        <span class="form-label">邮箱</span>
        <div class="form-widget">
          <input type="text" v-model.trim="registerForm.email" />
        </div>
      </div>
      <div class="form-item">
        <span class="form-label">验证码</span>
        <div class="form-widget">
          <input type="text" />
          <button class="form-button">发送验证码</button>
        </div>
      </div>
      <div class="form-item">
        <span class="form-label">密码</span>
        <div class="form-widget">
          <input type="password" v-model.trim="registerForm.password" />
        </div>
      </div>
      <div class="form-item">
        <div class="form-widget">
          <button
            class="form-button"
            style="margin-left: 80px"
            @click="submitRegister"
          >
            注册
          </button>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, reactive, onBeforeMount } from "vue";
import { useRouter } from "vue-router";
import request from "@/utils/request.js";
import { useToast } from "vue-toast-notification";
import { useLoginStore } from "@/stores/login";



const loginStore = useLoginStore();
const toast = useToast();
const router = useRouter();
const activeTab = ref(0);

onBeforeMount(() => {
    loginStore.updateLogin();
    if (loginStore.isLogin.value) {
      router.push("/");
    }
});

const registerForm = reactive({
  username: "",
  email: "",
  password: "",
});
const loginForm = reactive({
  email: "",
  password: "",
});

const registerFormValid = () => {
  if (!registerForm.username.match("^[\u4e00-\u9fa5A-Za-z0-9]{2,15}$")) {
    toast.error("用户名为2-15个字符");
    return false;
  }
  if (
    !registerForm.email.match(
      "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$"
    )
  ) {
    toast.error("邮箱格式错误");
    return false;
  }
  if (!registerForm.password.match("^[A-Za-z0-9]{6,15}$")) {
    toast.error("密码应为6-15个英文或数字");
    return false;
  }
  return true;
};

const submitRegister = () => {
  if (!registerFormValid()) {
    return;
  }
  request.userRegister(registerForm).then((result) => {
    localStorage.setItem("token", result);
    loginStore.updateLogin();
    toast.success("注册成功");
    router.push("/");
  });
};

const submitLogin = () => {
  if (loginForm.email == "" || loginForm.password == "") {
    toast.error("邮箱和密码不能为空");
    return;
  }
  request.userLogin(loginForm).then((result) => {
    console.log(result);
    localStorage.setItem("token", result);
    loginStore.updateLogin();
    toast.success("登录成功");
    router.push("/");
    // location.reload();
  });
};
</script>

<style lang="less" scoped>
.login-wrapper {
  width: 100%;
  padding-inline: var(--bg-padding);
  display: flex;
  flex-direction: column;
  justify-content: center;
  .title {
    width: 100%;
    height: 120px;
  }
}
.select-tab {
  height: 96px;
  display: flex;
  justify-content: center;
  align-items: flex-end;
  border-bottom: 2px solid var(--color-border);
  .tab {
    margin-inline: 32px;
    font-size: 24px;
    cursor: pointer;
  }
  .tab.active {
    border-bottom: 3px solid #f5870a;
  }
}
.form-wrapper {
  display: flex;
  flex-direction: column;
  .form-item {
    height: 40px;
    display: flex;
    justify-content: center;
    margin-block: 10px;
    .form-label {
      width: 60px;
      line-height: 36px;
      font-size: 16px;
    }
    .form-widget {
      margin-left: 20px;
      width: 300px;
      height: 40px;
      display: flex;
      // justify-content: center;
      // margin-block: 10px;
      input {
        font-size: 16px;
        height: 100%;
        width: 100%;
        padding-inline: 16px;
        border: 1px solid #ccc;
      }
      .form-button {
        width: 150px;
        font-size: 15px;
        border: 1px solid #ccc;
        cursor: pointer;
      }
    }
  }
}
</style>
