<template>
  <div>
    <div style="margin-bottom: 20px; font-size: 22px; font-weight: bold">评论 {{ evaluateCount }}</div>
    <div style="margin-bottom: 20px">
      <el-input type="textarea" placeholder="请输入评论" v-model="content"></el-input>
      <div style="text-align: right; margin-top: 5px"><el-button type="primary" @click="addEvaluate(null)">评论</el-button></div>
    </div>

    <div>
      <div v-for="item in evaluateList" :key="item.id" style="margin-bottom: 20px">
        <!-- 一级评论-->
        <div style="display: flex; margin-bottom: 20px">
          <img :src="item.avatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
          <div style="padding-left: 15px; flex: 1">
            <div style="margin-bottom: 10px; color: #666">{{ item.userName }}</div>
            <div style="margin-bottom: 5px">{{ item.content }}</div>
            <div style="color: #666; font-size: 13px; margin-bottom: 5px">
              <span>{{ item.time }}</span>
              <span @click="handleShowReply(item)" style="margin: 0 20px; cursor: pointer"
                    :class="{'evaluate-active' : item.showReply}"><i class="el-icon-s-comment"></i>回复</span>
              <span @click="delEvaluate(item.id)" v-if="item.userId === user.id" style="cursor: pointer"><i class="el-icon-delete"></i>删除</span>
            </div>
            <div v-if="item.showReply">
              <el-input type="textarea" placeholder="请输入回复" v-model="item.replyContent"></el-input>
              <div style="text-align: right; margin-top: 5px"><el-button type="primary" @click="addEvaluate(item)">回复</el-button></div>
            </div>
          </div>
        </div>
        <!-- 二级评论-->
        <div v-if="item.children" style="padding-left: 65px">
          <div v-for="sub in item.children" style="display: flex; margin-bottom: 20px">
            <img :src="sub.avatar" alt="" style="width: 50px; height: 50px; border-radius: 50%">
            <div style="padding-left: 15px; flex: 1">
              <div style="margin-bottom: 10px; color: #666">{{ sub.userName }} <span v-if="sub.parentUserName !== null">回复：{{ sub.parentUserName }}</span></div>
              <div style="margin-bottom: 5px">{{ sub.content }}</div>
              <div style="color: #666; font-size: 13px; margin-bottom: 5px">
                <span>{{ sub.time }}</span>
                <span @click="handleShowReply(sub)" style="margin: 0 20px; cursor: pointer"
                      :class="{'evaluate-active' : sub.showReply}"><i class="el-icon-s-comment"></i>回复</span>
                <span @click="delEvaluate(sub.id)" v-if="sub.userId === user.id" style="cursor: pointer"><i class="el-icon-delete"></i>删除</span>
              </div>
              <div v-if="sub.showReply">
                <el-input type="textarea" placeholder="请输入回复" v-model="sub.replyContent"></el-input>
                <div style="text-align: right; margin-top: 5px"><el-button type="primary" @click="addEvaluate(sub)">回复</el-button></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "EvaluateComponent",
  props: {
    fid: null,
    module: null
  },
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      evaluateCount: 0,
      content: '',
      evaluateList: []
    }
  },
  created() {
    this.loadEvaluate()
  },
  methods: {
    handleShowReply(evaluate) {
      this.$set(evaluate, 'showReply', !evaluate.showReply)
    },
    delEvaluate(evaluateId) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/evaluate/delete/' + evaluateId).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.loadEvaluate()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    loadEvaluate() {
      this.$request.get('/evaluate/selectTree/' + this.fid + '/' + this.module).then(res => {
        this.evaluateList = res.data || []
      })

      this.$request.get('/evaluate/selectCount/' + this.fid + '/' + this.module).then(res => {
        this.evaluateCount = res.data || 0
      })
    },
    addEvaluate(evaluate) {  //  evaluate 有值表示这是回复
      let data = { fid: this.fid, userId: this.user.id, module: this.module, content: this.content }
      if (evaluate) {
        data.content = evaluate.replyContent
        data.pid = evaluate.id
      }
      this.$request.post('/evaluate/add', data).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.content = ''
          this.loadEvaluate()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
  }
}
</script>

<style scoped>

</style>