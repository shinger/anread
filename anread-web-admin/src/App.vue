<template>
  <div class="layout">
    <el-container v-if="state.showMenu" class="container">
      <el-aside class="aside">
        <div class="head">
          <div>
            <img src="/anyue-logo.png" alt="logo" />
            <span style="font-size: 14px">Anyue Admin</span>
          </div>
        </div>
        <div class="line" />
        <el-menu
          background-color="#222832"
          text-color="#fff"
          :router="true"
          :default-active="state.currentPath"
        >
          <el-menu-item index="/"
            ><el-icon><Odometer /></el-icon>首页</el-menu-item
          >
          <el-menu-item index="/add"
            ><el-icon><Plus /></el-icon>添加书本</el-menu-item
          >
          <el-menu-item index="/category"
            ><el-icon><TakeawayBox /></el-icon>分类管理</el-menu-item
          >
          <el-menu-item index="/book"
            ><el-icon><Management /></el-icon>书库管理</el-menu-item
          >
          <el-menu-item index="/system_config"
            ><el-icon><FontIcon /></el-icon>系统配置</el-menu-item
          >
          <!-- <el-menu-item index="/hot"
            ><el-icon><StarFilled /></el-icon>热销商品配置</el-menu-item
          >
          <el-menu-item index="/new"
            ><el-icon><Sell /></el-icon>新品上线配置</el-menu-item
          >
          <el-menu-item index="/recommend"
            ><el-icon><ShoppingCart /></el-icon>为你推荐配置</el-menu-item
          >
          <el-menu-item index="/guest"
            ><el-icon><User /></el-icon>会员管理</el-menu-item
          >
          <el-menu-item index="/order"
            ><el-icon><List /></el-icon>订单管理</el-menu-item
          > -->
          <el-menu-item index="/account"
            ><el-icon><Lock /></el-icon>修改密码</el-menu-item
          >
        </el-menu>
        <div class="user-box" v-if="userInfo">
          <div class="avatar">
            <img :src="userInfo.avatar" alt="" />
          </div>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            viewBox="0 0 24 24"
            width="32"
            height="32"
            @click="signOut"
          >
            <g fill="none">
              <path
                d="M8.502 11.5a1.002 1.002 0 1 1 0 2.004a1.002 1.002 0 0 1 0-2.005zM12 4.353V11.004h7.442L17.72 9.28a.75.75 0 0 1-.073-.977l.073-.084a.75.75 0 0 1 .976-.072l.084.072l2.997 2.998a.75.75 0 0 1 .073.976l-.073.084l-2.996 3.003a.75.75 0 0 1-1.134-.975l.072-.084l1.713-1.717h-7.431L12 19.25a.75.75 0 0 1-.88.738l-8.5-1.501a.75.75 0 0 1-.62-.739V5.75a.75.75 0 0 1 .628-.74l8.5-1.396a.75.75 0 0 1 .872.74zm-1.5.883l-7 1.15v10.732l7 1.236V5.237zM13 18.5h.765l.102-.007a.75.75 0 0 0 .648-.744l-.007-4.25H13v5zM13.002 10L13 8.725V5h.745a.75.75 0 0 1 .743.647l.007.101l.007 4.252h-1.5z"
                fill="currentColor"
              ></path>
            </g>
          </svg>
        </div>
      </el-aside>
      <el-container class="content">
        <Header />
        <div class="main">
          <router-view />
        </div>
        <!-- <Footer /> -->
      </el-container>
    </el-container>
    <el-container v-else class="container">
      <router-view />
    </el-container>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { useRouter } from "vue-router";
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import FontIcon from "@/components/FontIcon.vue";
import { localGet, pathMap } from "@/utils";
import userAPI from "@/requests/user-api";
import { localRemove } from "@/utils";

const noMenu = ["/login"];
const router = useRouter();
const state = reactive({
  showMenu: true,
  currentPath: "/",
});
const userInfo = ref(null);

onMounted(() => {
  const pathname = window.location.hash.split("/")[1] || "";
  if (!["login"].includes(pathname)) {
    getUserInfo();
  }
});

// 获取用户信息
const getUserInfo = async () => {
  userAPI.getUserInfo().then((res) => {
    console.log(res);
    userInfo.value = res;
  });
};

const signOut = () => {
  console.log("sign out");
  localRemove("token");
  router.push({ path: "/login" });
};

router.afterEach((to, from) => {
  state.showMenu = !noMenu.includes(to.path);
});

router.beforeEach((to, from, next) => {
  if (to.path == "/login") {
    // 如果路径是 /login 则正常执行
    next();
  } else {
    // 如果不是 /login，判断是否有 token
    if (!localGet("token")) {
      // 如果没有，则跳至登录页面
      next({ path: "/login" });
    } else {
      // 否则继续执行
      // getUserInfo();
      next();
    }
  }
  state.currentPath = to.path;
  document.title = pathMap[to.name];
});
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #ffffff;
}
.container {
  height: 100vh;
}
.aside {
  width: 200px !important;
  background-color: #222832;
}
.head {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 50px;
}
.head > div {
  display: flex;
  align-items: center;
}

.head img {
  width: 36px;
  height: 36px;
}
.head span {
  font-size: 20px;
  color: #ffffff;
  margin-inline: 8px;
}
.line {
  border-top: 1px solid hsla(0, 0%, 100%, 0.05);
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}
.content {
  display: flex;
  flex-direction: column;
  max-height: 100vh;
  overflow: hidden;
}
.main {
  height: calc(100vh - 50px);
  overflow: auto;
  padding: 10px;
}

.user-box {
  color: #fff;
  position: absolute;
  bottom: 10px;
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
  width: 200px;
}

.user-box .avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  overflow: hidden;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>

<style>
body {
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
.el-menu {
  border-right: none !important;
}
.el-submenu {
  border-top: 1px solid hsla(0, 0%, 100%, 0.05);
  border-bottom: 1px solid rgba(0, 0, 0, 0.2);
}
.el-submenu:first-child {
  border-top: none;
}
.el-submenu [class^="el-icon-"] {
  vertical-align: -1px !important;
}
a {
  color: #409eff;
  text-decoration: none;
}
.el-pagination {
  text-align: center;
  margin-top: 20px;
}
.el-popper__arrow {
  display: none;
}
</style>
