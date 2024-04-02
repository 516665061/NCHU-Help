<template>
  <div class="front-home">
    <Affix></Affix>
    <div class="main-content">
      <el-carousel indicator-position="outside" height="400px">
        <el-carousel-item v-for="(item,index) in slides" :key="index">
          <img :src="item.image" @click="openUrl(item.url)"/>
        </el-carousel-item>
      </el-carousel>

      <div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="card">
              <div style="display: flex; padding: 10px;">
                <div style="flex: 1; color: #409EFF; font-size: 18px; font-weight: 550">最新发布失物</div>
                <div style="flex: 1; text-align: right; color: #666666; cursor: pointer" @click="$router.push('/lost')">查看更多</div>
              </div>
              <div style="margin-top: 10px">
                <el-row :gutter="10">
                  <el-col :span="12" v-for="item in lostData" style="margin-top: 10px">
                    <div class="transition card">
                      <div style="display: flex;">
                        <img :src="item.img" alt="" style="height: 75px; width: 75px; border: 2px solid #eeeeee">
                        <div style="flex: 1 1 0; margin-left: 5px; width: 0;">
                          <div class="overflowShow" style="font-weight: 550; color: #409EFF">{{ item.name }}</div>
                          <div class="overflowShow" style="margin-top: 10px; color: #666666">发布人：{{ item.userName }}</div>
                          <div class="overflowShow" style="margin-top: 5px; color: #666666">时间：{{ item.time }}</div>
                        </div>
                      </div>
                      <div style="margin-top: 20px; text-align: center">
                        <el-button type="info" size="mini">查看详情</el-button>
                        <el-button type="primary" size="mini">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="card">
              <div style="display: flex; padding: 10px;">
                <div style="flex: 1; color: #409EFF; font-size: 18px; font-weight: 550">最新发布招领</div>
                <div style="flex: 1; text-align: right; color: #666666">查看更多</div>
              </div>
              <div style="margin-top: 10px">
                <el-row :gutter="10">
                  <el-col :span="12" v-for="item in foundData" style="margin-top: 10px">
                    <div class="transition card">
                      <div style="display: flex;">
                        <img :src="item.img" alt="" style="height: 75px; width: 75px; border: 2px solid #eeeeee">
                        <div style="flex: 1 1 0; margin-left: 5px; width: 0;">
                          <div class="overflowShow" style="font-weight: 550; color: #409EFF">{{ item.name }}</div>
                          <div class="overflowShow" style="margin-top: 10px; color: #666666">发布人：{{ item.userName }}</div>
                          <div class="overflowShow" style="margin-top: 5px; color: #666666">时间：{{ item.time }}</div>
                        </div>
                      </div>
                      <div style="margin-top: 20px; text-align: center">
                        <el-button type="info" size="mini">查看详情</el-button>
                        <el-button type="primary" size="mini">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script>

import router from "@/router";
import Affix from "@/views/component/Affix.vue";

export default {
  data() {
    return {
      slides: [],
      lostData: [],
      foundData: [],
    }
  },
  components: {
    Affix,
  },
  created() {
    this.load()
    this.loadLostData()
    this.loadFoundData()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    router() {
      return router
    },
    loadLostData() {
      this.$request.get('/lost/selectNewFour',{
        params: {
          status: '丢失中'
        }
      }).then(res => {
        if (res.code === '200') {
          this.lostData = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    loadFoundData() {
      this.$request.get('/lost/selectNewFour',{
        params: {
          status: '未找到失主'
        }
      }).then(res => {
        if (res.code === '200') {
          this.foundData = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
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
  }
}
</script>

<style scoped>
.el-carousel__item img {
  width: 100%;
  height: 100%
}
</style>