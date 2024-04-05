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
                <div style="flex: 1; color: #409EFF; font-size: 18px; font-weight: 550">最新帖子</div>
                <div style="flex: 1; text-align: right; color: #666666; cursor: pointer" @click="$router.push('/posts')">查看更多</div>
              </div>
              <div style="margin-top: 10px">
                <div v-for="item in newPostData" style="margin-bottom: 10px; display: flex; cursor: pointer;">
                  <div class="line1 postTitle" style="flex: 1 1 0%;">{{ item.title}}</div>
                  <div class="line1" style="margin-left: 10px; color: rgb(102, 102, 102); font-size: 13px">{{ item.time}}</div>
                </div>
              </div>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="card">
              <div style="display: flex; padding: 10px;">
                <div style="flex: 1; color: #409EFF; font-size: 18px; font-weight: 550">最热帖子</div>
                <div style="flex: 1; text-align: right; color: #666666; cursor: pointer" @click="$router.push('/posts')">查看更多</div>
              </div>
              <div style="margin-top: 10px">
                <div v-for="item in postData" style="margin-bottom: 10px; display: flex; cursor: pointer;">
                  <div class="line1 postTitle" style="flex: 1 1 0%;">{{ item.title}}</div>
                  <div class="line1" style="margin-left: 10px; color: rgb(102, 102, 102); font-size: 13px">{{ item.time}}</div>
              </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div style="margin-top: 20px;">
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
                        <el-button type="info" size="mini" @click="viewEditor(item.content)">查看详情</el-button>
                        <el-button type="primary" size="mini" @click="contact(item.userId)">联系失主</el-button>
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
                <div style="flex: 1; text-align: right; color: #666666; cursor: pointer" @click="$router.push('/lost?activeName=second')">查看更多</div>
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
                        <el-button type="info" size="mini" @click="viewEditor(item.content)">查看详情</el-button>
                        <el-button type="primary" size="mini" @click="contact(item.userId)">联系失主</el-button>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
        <div v-html="viewData" class="w-e-text w-e-text-container"></div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import router from "@/router";
import Affix from "@/views/component/Affix.vue";

export default {
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      slides: [],
      lostData: [],
      postData: [],
      foundData: [],
      newPostData: [],
      viewVisible: false,
      viewData: null,
    }
  },
  components: {Affix},
  created() {
    this.load()
    this.loadLostData()
    this.loadPostData()
    this.loadFoundData()
    this.loadHotPostData()
  },
  // methods：本页面所有的点击事件或者其他函数定义区
  methods: {
    router() {
      return router
    },
    contact(userId) {
      if (this.user.id === userId) {
        this.$message.warning('自己不能给自己留言！！！')
        return
      }
      this.$request.post('/chatGroup/add', { chatUserId: userId, userId: this.user.id }).then(res => {
        this.$router.push('/chat')
      })
    },
    viewEditor(content) {
      this.viewData = content
      this.viewVisible = true
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
    loadPostData() {
      this.$request.get('/posts/selectNewFive',{
        params: {

        }
      }).then(res => {
        if (res.code === '200') {
          this.newPostData = res.data
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    loadHotPostData() {
      this.$request.get('/posts/selectHotFive',{
        params: {

        }
      }).then(res => {
        if (res.code === '200') {
          this.postData = res.data
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