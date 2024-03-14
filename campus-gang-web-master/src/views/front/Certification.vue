<template>
  <div class="main-content">
    <el-card style="margin: 30px auto">
      <el-form label-position="right" label-width="80px" :model="form" :rules="rules" ref="formRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" plaeholdre="请输入姓名" v-if="form.status != '通过'"></el-input>
          <el-input v-model="form.name" :disabled="true" v-if="form.status === '通过'"></el-input>
        </el-form-item>
        <el-form-item label="本人照片" prop="avatar" required>
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :on-success="handleAvatarSuccess1"
              v-if="form.status != '通过'"
          >
            <img v-if="form.avatar" :src="form.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
            <img v-if="form.avatar && form.status === '通过'" :src="form.avatar" class="avatar" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
        </el-form-item>
        <el-form-item label="学号" prop="phone">
          <el-input v-model="form.studentNo" placeholder="请输入学号" v-if="form.status != '通过'"></el-input>
          <el-input v-model="form.studentNo" :disabled="true" v-if="form.status === '通过'"></el-input>
        </el-form-item>
        <el-form-item label="示例">
          <template>
            <el-image :src="require('@/assets/imgs/cer.jpg')" style="width: 160px; height: 120px; border-radius: 5px" :preview-src-list="[require('@/assets/imgs/cer.jpg')]"></el-image>
          </template>
        </el-form-item>
        <el-form-item label="证件照片" prop="card" required>
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :on-success="handleAvatarSuccess2"
              v-if="form.status != '通过'"
          >
            <img v-if="form.card" :src="form.card" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
          <img v-if="form.card && form.status === '通过'" :src="form.card" class="avatar" />
        </el-form-item>
        <el-form-item label="审核状态" prop="status">
          <template>
            <el-tag type="default" v-if="!form.status" >待申请</el-tag>
            <el-tag type="warning" v-if="form.status === '待审核'">待审核</el-tag>
            <el-tag type="success" v-if="form.status === '通过'">通过</el-tag>
            <el-tag type="error" v-if="form.status === '拒绝'">拒绝</el-tag>
          </template>
        </el-form-item>
        <el-form-item label="审核理由" prop="reason" v-if="form.reason">
          <span>{{form.reason}}</span>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="submit" v-if="!form.status || form.status === '拒绝'">提交认证</el-button>
          <el-button type="primary" @click="save" v-if="form.status === '通过'">保 存</el-button>
        </div>
      </el-form>
      </el-card>
  </div>
</template>

<script>
export default {
  name: "Certification",
  data () {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      form: {},
      rules: {
        name: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' },
        ],
        phone: [
          { required: true, message: '请输入电话号码', trigger: 'blur' },
        ],
        studentNo: [
          { required: true, message: '请输入学号', trigger: 'blur' },
        ],
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    save() {
      this.$request.put('/certification/update', this.form).then(res => {
        if (res.code === '200'){
          this.$message.success('保存成功')
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      }).catch()
    },
    submit() {
      this.$refs.formRef.validate().then(r => {
        this.form.userId = this.user.id
        this.$request.post('/certification/add', this.form).then(res => {
          if (res.code === '200') {
            this.$message.success('提交成功')
            this.load()
          } else {
            this.$message.error(res.msg)
          }
        })
      }).catch()
    },
    load() {  // 查询当前用户自己的认证信息
      this.$request.get('/certification/selectUserCertification').then(res => {
        this.form = res.data || {}
      })
    },
    beforeAvatarUpload(file) {
      const format = file.type === 'image/jpeg' || 'image/png';
      if (!format) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      return format;
    },
    handleAvatarSuccess1(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.form, 'avatar', response.data)
    },
    handleAvatarSuccess2(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.form, 'card', response.data)
    },
  }
}
</script>

<style scoped>
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>