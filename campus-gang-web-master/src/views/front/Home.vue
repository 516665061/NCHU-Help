<template>
  <div class="front-home">
    <div style="position: fixed; top: 100px;">
      <div class="el-affix" style="height: 180px; width: 150px;">
        <div class="el-affix--fixed" style="height: 180px; width: 150px; top: 200px; z-index: 100;">
          <div class="el-affix-container">
            <a href="http://www.nchu.edu.cn" style="">
              <div class="el-affix-items">
                <div class="el-affix-item">
                  <img src="@/assets/imgs/logo.png" style="height: 20px; width: 20px;">
                  <div class="left-hot" style="color: red;">南昌航空大学</div>
                </div>
              </div>
            </a>
            <el-popover
                placement="right"
                width="300"
                trigger="hover">
              <div style="width: 300px; height: 300px">
                <div style="text-align: center; margin-top: 20px;">微信扫描二维码了解</div>
                <div style="display: flex; align-items: center;justify-content: center">
                  <img src="@/assets/imgs/wechatbinary.jpg" alt="" style="width: 250px; height: 250px; border: 1px solid rgb(204, 204, 204); border-radius: 10px;">
                </div>
              </div>
              <div class="el-affix-items" slot="reference">
                <div class="el-affix-item">
                  <img src="@/assets/imgs/wechat.png" style="height: 20px; width: 20px;">
                  <div class="left-hot">添加微信咨询</div>
                </div>
              </div>
            </el-popover>
            <el-popover
                placement="right"
                width="300"
                trigger="hover"
                >
              <div style="width: 300px; height: 300px">
                <div style="text-align: center; margin-top: 20px;">QQ扫描二维码了解</div>
                <div style="display: flex; align-items: center;justify-content: center">
                  <img src="@/assets/imgs/qqbinary.jpg" alt="" style="width: 250px; height: 250px; border: 1px solid rgb(204, 204, 204); border-radius: 10px;">
                </div>
              </div>
              <div class="el-affix-items" slot="reference">
                <div class="el-affix-item">
                  <img src="@/assets/imgs/qq.png" style="height: 20px; width: 20px;">
                  <div class="left-hot">添加QQ咨询</div>
                </div>
              </div>
            </el-popover>
          </div>
        </div>
      </div>
    </div>
    <div class="main-content">
      <el-carousel indicator-position="outside" height="400px">
        <el-carousel-item v-for="(item,index) in slides" :key="index">
          <img :src="item.image" @click="openUrl(item.url)"/>
        </el-carousel-item>
      </el-carousel>

      <div class="service_title">NCHU校内的互助平台</div>
      <div class="service_sub_title">在校生实名服务，安全稳定有保障</div>
      <div class="solution-list">
        <div class="solution-item">
          <div class="image">
            <img src="@/assets/imgs/tower.png" alt="">
          </div>
          <div class="headline">校园市场</div>
          <div class="content">
            聚合多个校园站点，专注解决校园综合性创业解方案，打造本地智慧校园
          </div>
        </div>
        <div class="solution-item">
          <div class="image">
            <img src="@/assets/imgs/order.png" alt="">
          </div>
          <div class="headline">餐饮行业</div>
          <div class="content">
            多角色管理，多终端支持功能齐全，深入同城外卖连锁商超等多样场景
          </div>
        </div>
        <div class="solution-item">
          <div class="image">
            <img src="@/assets/imgs/store.png" alt="">
          </div>
          <div class="headline">代购服务</div>
          <div class="content">
            “吃、住、行、游、娱”一站式生活服务平台，各场景解决方案，连锁商超等多样场景
          </div>
        </div>
        <div class="solution-item">
          <div class="image">
            <img src="@/assets/imgs/distribution-car.png" alt="">
          </div>
          <div class="headline">跑腿配送</div>
          <div class="content">
            适用于多个场景：校园配送本地配送、即时配送、聚合配送等，高效率，低成本
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {

  data() {
    return {
      slides: [],
    }
  },
  created() {
    this.load()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    load() {
      this.$request.get('/slides/selectAll').then(res => {
        if (res.code === '200'){
          this.slides = res.data
        }
      })
    },
    openUrl(targetUrl) {
      window.open(targetUrl,'_blank');
      console.log(targetUrl);
    },
    hilarity() {
      this.$notify({
        title: "提示",
        message: "时间已到",
        duration: 0,
      });
    }
  }
}
</script>

<style scoped>
.el-affix--fixed {
  position: fixed;
}

.el-affix-container {
  width: 150px;
  background-color: white;
  height: 180px;
  border-radius: 5px;
  box-shadow: rgba(7, 17, 27, 0.1) 0px 4px 8px 0px;
}

.el-affix-items {
  height: 60px;
  line-height: 60px;
  text-align: center;
  padding: 0px 10px;
}

.el-affix-item {
  display: flex;
  align-items: center;
}

.left-hot {
  margin-left: 10px;
  border-bottom: 1px solid #eae4e4;
  cursor: pointer;
  color: #6d7278;
}

.left-hot:hover {
  color: red !important;
  font-weight: 550;
}

.el-carousel__item img {
  width: 100%;
  height: 100%
}

/deep/ .el-statistic .head {
  margin-bottom: 4px;
  color: #606266;
  font-size: 23px;
}

/deep/ .el-statistic .con .number {
  font-size: 30px;
}
</style>