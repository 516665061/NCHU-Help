<template>
  <div class="manager-container">
    <!--  头部  -->
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" />
        <div class="title">NCHU校园帮</div>
      </div>

      <div class="manager-header-center">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{ path: '/admin/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <div class="manager-header-right">
        <el-dropdown placement="bottom">
          <div class="avatar">
            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <div>{{ user.name ||  '管理员' }}</div>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>
            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!--  主体  -->
    <div class="manager-main">
      <!--  侧边栏  -->
      <div class="manager-main-left">
        <el-menu router style="border: none" :default-active="$route.path" unique-opened="true">
          <el-menu-item index="/admin/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">系统首页</span>
          </el-menu-item>
          <el-submenu index="set">
            <template slot="title">
              <i class="el-icon-s-tools"></i><span>网站设置</span>
            </template>
            <el-menu-item index="/admin/slides">幻灯片管理</el-menu-item>
            <el-menu-item index="/admin/notice">公告信息</el-menu-item>
          </el-submenu>

          <el-submenu index="market">
            <template slot="title">
              <i class="el-icon-goods"></i><span>商城管理</span>
            </template>
            <el-menu-item index="/admin/goods">商品信息</el-menu-item>
            <el-menu-item index="/admin/orders">订单信息</el-menu-item>
            <el-menu-item index="/admin/evaluate">商品评价</el-menu-item>
            <el-menu-item index="/admin/category">模块分类</el-menu-item>

          </el-submenu>

          <el-submenu index="errand">
            <template slot="title">
              <i class="el-icon-eleme"></i><span>跑腿管理</span>
            </template>
            <el-menu-item index="/admin/tasks">跑腿订单</el-menu-item>
            <el-menu-item index="/admin/comment">评价管理</el-menu-item>
            <el-menu-item index="/admin/certification">骑手认证</el-menu-item>
          </el-submenu>

          <el-submenu index="post">
            <template slot="title">
              <i class="el-icon-bangzhu"></i><span>失物与社区</span>
            </template>
            <el-menu-item index="/admin/lost">失物招领</el-menu-item>
            <el-menu-item index="/admin/posts">社区帖子</el-menu-item>
            <el-menu-item index="/admin/circles">圈子信息</el-menu-item>
          </el-submenu>

          <el-submenu index="user">
            <template slot="title">
              <i class="el-icon-s-custom"></i><span>用户管理</span>
            </template>
            <el-menu-item index="/admin/adminInfo">管理员信息</el-menu-item>
            <el-menu-item index="/admin/auditorInfo">审核员信息</el-menu-item>
            <el-menu-item index="/admin/user">用户信息</el-menu-item>
            <el-menu-item index="/admin/records">收支明细</el-menu-item>
            <el-menu-item index="/admin/address">地址信息</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--  数据表格  -->
      <div class="manager-main-right">
        <router-view @update:user="updateUser" />
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "Manager",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    }
  },
  created() {
    if (!this.user.id) {
      this.$router.push('/login')
    }
  },
  methods: {
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    goToPerson() {
      if (this.user.role === 'ADMIN') {
        this.$router.push('/admin/adminPerson')
      }
    },
    logout() {
      localStorage.removeItem('xm-user')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>