<template>
  <div class="container">
    <div style="width: 400px; padding: 30px; background-color: white; border-radius: 5px; opacity: 0.85; background-size: cover;background-repeat: no-repeat;">
      <div style="text-align: center; font-size: 20px; margin-bottom: 20px; color: #333">欢迎使用</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select prefix-icon="el-icon-key" v-model="form.role" placeholder="登录类型">
            <el-option label="用户" value="USER"></el-option>
            <el-option label="审核员" value="AUDITOR"></el-option>
            <el-option label="管理员" value="ADMIN"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="code">
          <div style="display: flex">
            <el-input placeholder="请输入验证码" prefix-icon="el-icon-circle-check" size="medium" style="flex: 1" v-model="form.code"></el-input>
            <div style="flex: 1; height: 36px">
              <valid-code @update:value="getCode" />
            </div>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; background-color: #409EFF; border-color: #409EFF; color: white" @click="login">登 录</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
          <div style="flex: 1; text-align: right">
            还没有账号？请 <a href="/register">注册</a>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import ValidCode from "@/views/ValidCode.vue";

export default {
  name: "Login",
  components: {
    ValidCode
  },
  data() {
    const validateCode = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入验证码'))
      } else if (value.toLowerCase() !== this.code) {
        callback(new Error('验证码错误'))
      } else {
        callback()
      }
    }
    return {
      code: '',  // 验证码组件传递过来的code
      form: { role: 'USER', code:''},
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        role: [
          { required: true, message: '请选择登录类型', trigger: 'change' },
        ],
        code: [
          { validator: validateCode, trigger: 'blur' }
        ]
      }
    }
  },
  created() {

  },
  methods: {
    getCode(code) {
      this.code = code.toLowerCase()
    },
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              if (res.data.role === 'USER') {
                this.$router.push('/')
              }else if (res.data.role === 'ADMIN') {
                this.$router.push('/admin')
              }else if (res.data.role === 'AUDITOR') {
                this.$router.push('/auditor')
              }
              this.$message.success('登录成功')
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>