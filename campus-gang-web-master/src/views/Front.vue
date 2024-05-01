<template>
  <div class="front-container">
    <div class="front-notice"><i class="el-icon-bell" style="margin-right: 2px"></i>公告：{{ top }}</div>
    <!--头部-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">校园帮</div>
      </div>
      <div class="front-header-center">
        <div class="front-header-nav">
          <el-menu :default-active="$route.path" mode="horizontal" router text-color="#606266" active-text-color="#409EFF" class="el-menu-demo">
						<el-menu-item index="/home">首页</el-menu-item>
            <el-menu-item index="/market" v-if="user.username">校园市场</el-menu-item>
            <el-menu-item index="/posts" v-if="user.username">校园论坛</el-menu-item>
						<el-menu-item index="/lost" v-if="user.username">失物招领</el-menu-item>
						<el-menu-item index="/orders" v-if="user.username">校园跑腿</el-menu-item>
          </el-menu>
        </div>
      </div>
      <div style="margin-right: 20px">
        <span @click="$router.push('/chat')" style="font-size: 16px; cursor: pointer"><i class="el-icon-chat-dot-round"></i> 聊天消息</span>
      </div>
      <div class="front-header-right">
        <div v-if="!user.username">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="front-header-dropdown">
              <img :src="user.avatar" alt="">
              <div style="margin-left: 10px">
                <span>{{ user.name }}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
            </div>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="text-decoration: none" @click="openWallet">钱包</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="loadAddress">地址管理</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="loadComment">评价管理</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="$router.push('/certification')">骑手认证</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="$router.push('/person')">个人中心</div>
              </el-dropdown-item>
              <el-dropdown-item>
                <div style="text-decoration: none" @click="logout">退出登录</div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <el-dialog title="地址管理 " :visible.sync="dialogTableVisible" center>
      <div class="table">
        <el-table :data="addressData" height="100%" :header-cell-style="{'text-align':'center'}">
          <el-table-column type="index" :index="indexMethodOther" label="序号" align="center" sortable></el-table-column>
          <el-table-column prop="address" label="地址" align="center"></el-table-column>
          <el-table-column prop="userName" label="联系人" align="center"></el-table-column>
          <el-table-column prop="phone" label="联系电话" align="center"></el-table-column>
          <el-table-column label="操作" align="center" width="180">
            <template v-slot="scope">
              <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" plain @click="delAddress(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="warning" plain @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" plain @click="handleAdd">新 增</el-button>
      </span>
    </el-dialog>
    <el-dialog title="收支明细" :visible.sync="dialogRecordVisible" center>
      <Records></Records>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" plain @click="dialogRecordVisible = false">确  定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="信息" :visible.sync="fromVisible" width="30%" append-to-body center>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="地址"></el-input>
        </el-form-item>
        <el-form-item label="联系人" prop="userName">
          <el-input v-model="form.userName" placeholder="联系人"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="联系电话"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="评价" :visible.sync="commentVisible" width="70%" append-to-body center>
      <el-tabs v-model="activeName" @tab-click="handleClick" :header-cell-style="{'text-align':'center'}">
        <el-tab-pane label="评价我的" name="first">
          <div class="table">
            <el-table :data="commentList" height="100%" :default-sort = "{prop: 'time', order: 'descending'}">
              <el-table-column type="index" :index="indexMethodOther" label="序号" align="center"></el-table-column>
              <el-table-column prop="content" label="评价" align="center">
                <template slot-scope="{row}">
                  {{ row.content || '该用户暂未做出评价' }}
                </template>
              </el-table-column>
              <el-table-column prop="star" label="评分" align="center">
                <template v-slot="scope">
                  <el-rate v-model="scope.row.star" disabled></el-rate>
                </template>
              </el-table-column>
              <el-table-column prop="acceptName" label="用户" align="center"></el-table-column>
              <el-table-column prop="orderNo" label="订单编号" align="center"></el-table-column>
              <el-table-column prop="time" label="时间" align="center"></el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        <el-tab-pane label="我的评价" name="second">
          <div class="table">
            <el-table :data="commentList" height="100%"  @selection-change="handleSelectionChange" :default-sort = "{prop: 'time', order: 'descending'}">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column type="index" :index="indexMethodOther" label="序号"></el-table-column>
              <el-table-column prop="content" label="评价">
                <template slot-scope="{row}">
                  {{ row.content || '该用户暂未做出评价' }}
                </template>
              </el-table-column>
              <el-table-column prop="star" label="评分">
                <template v-slot="scope">
                  <el-rate v-model="scope.row.star" disabled></el-rate>
                </template>
              </el-table-column>
              <el-table-column prop="userName" label="用户"></el-table-column>
              <el-table-column prop="orderNo" label="订单编号"></el-table-column>
              <el-table-column prop="time" label="时间"></el-table-column>
              <el-table-column label="操作" align="center" width="180">
                <template v-slot="scope">
                  <el-button size="mini" type="danger" plain @click="delComment(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" @click="delBatchComment" v-if="ids.length != 0">删除选中（{{ids.length}}）条</el-button>
        <el-button type="primary" plain @click="commentVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="钱包" :visible.sync="walletVisible" width="30%" append-to-body center>
      <div style="text-align: center; margin: 10px 0; font-size: 18px">
        <i class="el-icon-wallet"></i>账户余额：{{account}}元
      </div>
      <el-row :gutter="10">
        <el-col :span="8" v-for="item in items" :key="item">
          <div class="wallet" :class="{ 'active' : current === item }" @click="clickItem(item)">￥{{ item }}</div>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="wallet">充 值</el-button>
      </div>
    </el-dialog>

    <!--主体-->
    <div class="main-body">
      <router-view ref="child" @update:user="updateUser" />
    </div>

    <div class="front-footer">
      <div style="width:70%;margin:0 auto;line-height:30px;font-size:14px;color:#93999F;">
        <span style="color:#666666;">友情链接：</span>
        <span style="margin-left:10px;">
          <a style="margin-left:10px;color:#93999F;" href="https://github.com/" target="_blank">Github</a>
        </span>
        <span style="margin-left:10px;">
          <a style="margin-left:10px;color:#93999F;" href="https://apache.org/" target="_blank">Apache</a>
        </span>
        <span style="margin-left:10px;">
          <a style="margin-left:10px;color:#93999F;" href="https://cn.vuejs.org/" target="_blank">VUE</a>
        </span></div>
      <div style="text-align:center;line-height:30px;font-size:13px;color:#93999F;"> Copyright ©2024 版权所有
      </div>
    </div>
  </div>

</template>

<script>
import Records from './component/Records.vue'
export default {
  name: "FrontLayout",
  components: {
    Records
  },

  data () {
    return {
      top: '',
      notice: [],
      current: 6,
      account: 0,
      items: [6, 10, 30, 60, 100, 200],
      activeName: 'first',
      user: JSON.parse(localStorage.getItem("xm-user") || '{}'),
      dialogTableVisible: false,
      dialogRecordVisible: false,
      fromVisible: false,
      commentVisible: false,
      walletVisible: false,
      addressData: [],
      commentList: [],
      ids: [],
      form: {},
      child: 0,
      flag: '',
      flag2: true,
      rules: {
        address: [
          {required: true, message: '请输入地址信息', trigger: 'blur'},
        ],
        userName: [
          {required: true, message: '请输入联系人', trigger: 'blur'},
        ],
        phone: [
          {required: true, message: '请输入电话', trigger: 'blur'},
        ]
      },
    }
  },

  mounted() {
    this.loadNotice()
  },
  methods: {
    loadNotice() {
      this.$request.get('/notice/selectAll').then(res => {
        this.notice = res.data
        let i = 0
        if (this.notice && this.notice.length) {
          this.top = this.notice[0].content
          setInterval(() => {
            this.top = this.notice[i].content
            i++
            if (i === this.notice.length) {
              i = 0
            }
          }, 2500)
        }
      })
    },
    indexMethod(index) {
      return index + 1 + (this.pageNum -1) * this.pageSize;
    },
    indexMethodOther(index) {
      return index + 1;
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    handleClick(tab, event) {
      if (tab.name == 'first'){
        this.loadComment(true)
      }else if (tab.name == 'second'){
        this.loadComment(false)
      }
    },
    clickItem(item) {
      this.current = item
    },
    openWallet() {
      this.walletVisible = true
      this.loadAccount()
    },
    delComment(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    delBatchComment() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些评论吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/comment/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('删除成功')
            this.loadComment()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    loadComment(f) {  // 查询
      this.commentVisible = true
      if (f != null) {
        this.flag2 = f
      }
      this.$request.get(this.flag2 ? '/comment/selectAcceptComment/' + this.user.id : '/comment/selectUserComment/' + this.user.id).then(res => {
        this.commentList = res.data || []
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
      this.flag = 0
    },
    wallet (){
      this.$request.put('/user/charge/' + this.current).then(res => {
        if (res.code === '200') {
          this.$message.success('充值成功')
          this.loadAccount()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    loadAccount() {
      this.$request.get('/user/selectById/' + this.user.id).then(res => {
        this.account = res.data.account || 0
        this.user.account = this.account
        localStorage.setItem('xm-user',JSON.stringify(this.user))
      })
    },
    delAddress(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning", center: true}).then(response => {
        this.$request.delete('/address/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.loadAddress()
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true
      this.flag = 1
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.userId = this.user.id
          this.$request({
            url: this.flag ? '/address/update' : '/address/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }
          ).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.fromVisible = false
              if (!this.child){
                this.loadAddress()
              }else {
                this.child = 0
              }
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    loadAddress() {
      this.dialogTableVisible = true
      this.$request.get('/address/selectAll',{
        params: {
          userId: this.user.id
        }
      }).then(res => {
        if (res.code === '200'){
          this.addressData = res.data
        }
      })
    },
    loadRecords() {
      this.dialogRecordVisible = true
      this.$request.get('/address/selectAll',{
        params: {
          userId: this.user.id
        }
      }).then(res => {
        if (res.code === '200'){
          this.addressData = res.data
        }
      })
    },
    updateUser() {
      this.user = JSON.parse(localStorage.getItem('xm-user') || '{}')   // 重新获取下用户的最新信息
    },
    // 退出登录
    logout() {
      localStorage.removeItem("xm-user");
      this.$router.push("/login");
    },
  }

}
</script>

<style scoped>
  @import "@/assets/css/front.css";
</style>