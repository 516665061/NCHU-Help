<template>
  <div class="front-home">
    <Affix></Affix>
    <div class="main-content" style="position: relative">

      <el-button @click="handleAdd" style="position: absolute; top: 20px; right: -150px" size="medium" type="primary" icon="el-icon-plus" plain>发布帖子</el-button>

      <div style="display: flex; align-items: flex-start; grid-gap: 10px">
        <div style="width: 150px;" class="card">
          <div class="circle" :class="{'circle-active' : circle === item.name }" v-for="item in circles" :key="item.id" @click="loadCircle(item.name)">{{ item.name }}</div>
        </div>

        <div style="flex: 1" class="card">
          <div @click="$router.push('/postsDetail?id=' + item.id)" class="circle-box" v-for="item in tableData" :key="item.id">
            <div style="flex: 1; padding: 0 10px; width: 0">
              <div class="line1 postTitle" style="font-size: 16px; font-weight: bold; margin-bottom: 10px; cursor: pointer;">{{ item.title }}</div>
              <div class="line1" style="font-size: 13px; color: #666; margin-bottom: 10px">{{ item.descr }}</div>

              <div style="color: #666">
                <span><i class="el-icon-user"></i> {{ item.userName }}</span>
                <span style="margin-left: 20px"><i class="el-icon-reading"></i> {{ item.readCount }}</span>
                <span style="margin-left: 20px"><i class="el-icon-time"></i> {{ item.time }}</span>
              </div>

            </div>

            <div>
              <img :src="item.img" alt="" style="width: 80px; height: 80px; border-radius: 5px;">
            </div>

          </div>
        </div>
      </div>
      <div style="margin-top: 10px; text-align: center">
        <el-pagination
            :hide-on-single-page="true"
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog title="帖子" :visible.sync="fromVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="descr">
          <el-input type="textarea" v-model="form.descr" placeholder="简介"></el-input>
        </el-form-item>
        <el-form-item label="圈子" prop="circle">
          <el-select style="width: 100%" v-model="form.circle">
            <el-option v-for="item in circles" :key="item.id" :value="item.name" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图片" prop="img">
          <el-upload
              :action="$baseUrl + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleImgSuccess"
          >
            <el-button type="primary">上传图片</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Affix from "@/views/component/Affix.vue";
import E from "wangeditor";

export default {
  name: "Posts",
  components: {Affix},
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      circles: [],
      posts: [],
      circle: '全部',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      fromVisible: false,
      form: {},
      rules: {},
    }
  },
  created() {
    this.loadCircles()
    this.load(1)
  },
  methods: {
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
      this.setRichText('')
    },
    loadCircle(circle) {  // 分页查询
      this.circle = circle
      this.load(1)
    },
    setRichText(html) {
      this.$nextTick(() => {
        this.editor = new E(`#editor`)
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgHeaders = {
          token: this.user.token
        }
        this.editor.config.uploadImgParams = {
          type: 'img',
        }
        this.editor.create()  // 创建
        this.editor.txt.html(html)
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/posts/selectFrontPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          circle: this.circle === '全部' ? null : this.circle,
        }
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadCircles() {
      this.$request.get('/circles/selectAll').then(res => {
        this.circles = res.data || []
      })
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImgSuccess(response, file, fileList) {
      this.form.img = response.data
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.content = this.editor.txt.html()
          this.$request({
            url: this.form.id ? '/posts/update' : '/posts/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>

</style>