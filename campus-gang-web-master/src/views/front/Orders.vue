<template>
  <el-container style="height: calc(100vh - 100px); margin-top: 120px;">
    <el-aside width="200px" style="height: calc(100vh - 100px);">
      <el-menu style="border: none" default-active="1">
        <el-menu-item index="1" @click.native="onload(1,'抢单大厅')">
          <i class="el-icon-menu"></i>
          <span slot="title">抢单大厅</span>
        </el-menu-item>
        <el-menu-item index="2" @click.native="onload(1,'我发布的')">
          <i class="el-icon-menu"></i>
          <span slot="title">我发布的</span>
        </el-menu-item>
        <el-menu-item index="3" @click.native="onload(1,'我接收的')">
          <i class="el-icon-menu"></i>
          <span slot="title">我接收的</span>
        </el-menu-item>

        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>发布订单</span>
          </template>
          <el-menu-item index="4-1" @click.native="openDialog('快递代取')">
            <i class="el-icon-user"></i>
            <span slot="title">快递代取</span>
          </el-menu-item>
          <el-menu-item index="4-2" @click.native="openDialog('外卖代取')">
            <i class="el-icon-eleme"></i>
            <span slot="title">外卖代取</span>
          </el-menu-item>
          <el-menu-item index="4-3" @click.native="openDialog('店铺代购')">
            <i class="el-icon-goods"></i>
            <span slot="title">店铺代购</span>
          </el-menu-item>
          <el-menu-item index="4-4" @click.native="openDialog('行李代搬')">
            <i class="el-icon-truck"></i>
            <span slot="title">行李代搬</span>
          </el-menu-item>
          <el-menu-item index="4-5" @click.native="openDialog('其它服务')">
            <i class="el-icon-help"></i>
            <span slot="title">其它服务</span>
          </el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>

    <el-main>
      <el-row :gutter="10">
        <el-col :span="8" v-for="item in orders" :key="item.id">
          <div style="margin-bottom: 10px" class="order-item">
            <el-card class="box-card transition" @click.native.stop="detail(item.id)">
              <div slot="header" class="clearfix">
                <span style="font-size: 15px">{{item.type}}</span>
                <el-tag effect="plain" style="margin-left: 5px;">金额￥{{item.price}}</el-tag>
                <el-tag style="float: right" v-if="item.status==='待接单'">{{ item.status }}</el-tag>
                <el-tag type="success" style="float: right" v-if="item.status==='待送达'">{{ item.status }}</el-tag>
                <el-tag type="warning" style="float: right" v-if="item.status==='待收货'">{{ item.status }}</el-tag>
                <el-tag type="danger" style="float: right" v-if="item.status==='待评价'">{{ item.status }}</el-tag>
                <el-tag type="info" style="float: right" v-if="item.status==='已取消'">{{ item.status }}</el-tag>
                <el-tag type="success" style="float: right" v-if="item.status==='已完成'">{{ item.status }}</el-tag>
                <div style="color: #AAAAAA; margin-top: 5px">
                  {{item.time}}
                  <div style="float: right" v-if="item.status === '待接单' && item.userId === user.id">
                    <el-statistic :value="new Date(item.expectTime)" time-indices format="HH:mm:ss" style="float: right" :value-style="{ color: 'red', fontSize: '16px' }" suffix="">
                      <template slot="suffix" class="statistic-suffix">
                        后自动取消订单
                      </template>
                    </el-statistic>
                  </div>
                  <div style="float: right" v-if="item.status === '待收货' && item.userId === user.id">
                    <el-statistic :value="new Date(item.arriveTime).getTime()+1000*60*60*24*7" time-indices format="HH:mm:ss" style="float: right" :value-style="{ color: 'red', fontSize: '16px' }" suffix="">
                      <template slot="suffix" class="statistic-suffix">
                        后自动确认收货
                      </template>
                    </el-statistic>
                  </div>
                </div>
              </div>
              <div class="text item line" v-if="item.descr != null">
                <div class="line-title">帮助内容:</div>
                <div class="line-content">{{item.descr}}</div>
              </div>
              <div class="text item line">
                <div class="line-title">取货地址:</div>
                <div class="line-content">{{item.pickAddress}}</div>
              </div>
              <div class="text item line">
                <div class="line-title">收货地址:</div>
                <div class="line-content">{{item.receiveAddress}}</div>
              </div>
              <div class="text item line">
                <div class="line-title">期望时间:</div>
                <div class="line-content">{{item.expectTime}}</div>
              </div>
              <div style="display: flow">
                <el-button style="float: left; margin-bottom: 18px;" type="danger" plain size="mini" @click.stop="confirm(item,'已取消')" v-if="item.status==='待接单' && item.userId === user.id" icon="el-icon-close">取消订单</el-button>
                <el-button style="float: right; margin-bottom: 18px;" type="primary" plain size="mini" @click.stop="acceptOrders(item)" v-if="item.status==='待接单'" icon="el-icon-check">接单({{item.price}}元)</el-button>
                <el-button style="float: right; margin-bottom: 18px;" type="primary" plain size="mini" @click.stop="handleArrive(item)" v-if="item.status==='待送达' && current === '我接收的'" icon="el-icon-check">确认送达</el-button>
                <el-button style="float: right; margin-bottom: 18px;" type="primary" plain size="mini" @click.stop="confirm(item,'待评价')" v-if="item.status==='待收货' && current === '我发布的'" icon="el-icon-check">确认收货</el-button>
                <el-button style="float: right; margin-bottom: 18px;" type="primary" plain size="mini" @click.stop="comment(item)" v-if="item.status==='待评价' && current === '我发布的'" icon="el-icon-check">评  价</el-button>
                <el-button style="float: right; margin-bottom: 18px; visibility: hidden;" type="primary" plain size="mini" @click.stop="comment(item)" v-if="(item.status==='已取消' || item.status==='已完成') && (current === '我发布的' || current === '我接收的')" icon="el-icon-check">评  价</el-button>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
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
    </el-main>

    <!--发布订单-->
    <el-dialog title="发布订单" :visible.sync="dialogFormVisible" top="0vh" size="mini">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="取货地址" prop="addressId">
          <el-select id="addressId" v-model="form.addressId" placeholder="请选择" @click.native.stop="getAddress" clearable >
            <el-option
                v-for="item in addressList"
                :key="item.id"
                :label= "'地址：' + item.address + '  姓名：' + item.userName + '  联系方式：' + item.phone"
                :value="item.id">
            </el-option>
          </el-select>
          <el-button plain @click="add" style="margin-left: 20px">添加地址</el-button>
        </el-form-item>
        <el-form-item label="收货地址" prop="targetId">
          <el-select id="targetId" v-model="form.targetId" placeholder="请选择" @click.native.stop="getAddress" clearable >
            <el-option
                v-for="item in addressList"
                :key="item.id"
                :label= "'地址：' + item.address + '\t姓名：' + item.userName + '\t联系方式：' + item.phone"
                :value="item.id">
            </el-option>
          </el-select>
          <el-button plain @click="add" style="margin-left: 20px">添加地址</el-button>
        </el-form-item>
        <el-form-item label="物品名称" prop="name">
          <el-input placeholder="请输入物品名称" v-model="form.name" id="name"></el-input>
        </el-form-item>
        <el-form-item label="重量(kg)" prop="weight">
          <el-input-number id="weight" v-model="form.weight" :precision="2" :step="0.5" :min="0.1" clearable>kg</el-input-number>
        </el-form-item>
        <el-form-item label="送达时间" required>
          <el-col :span="11">
            <el-form-item prop="date1">
              <el-date-picker id="date1" type="date" placeholder="选择日期" v-model="form.date1" value-format="yyyy-MM-dd" clearable style="width: 100%;"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col style="text-align: center" :span="1">-</el-col>
          <el-col :span="11">
            <el-form-item prop="date2">
              <el-time-picker id="date2" placeholder="选择时间" v-model="form.date2" value-format="HH:mm:ss" clearable style="width: 100%;"></el-time-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="订单类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="快递代取">快递代取</el-radio>
            <el-radio label="外卖代取">外卖代取</el-radio>
            <el-radio label="店铺代购">店铺代购</el-radio>
            <el-radio label="行李代搬">行李代搬</el-radio>
            <el-radio label="其它服务">其它服务</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="小费(元)" prop="price">
          <el-input-number id="price" v-model="form.price" :precision="2" :step="1" :min="1.00"></el-input-number>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" :autosize="{ minRows: 2}" placeholder="请输入备注内容" v-model="form.descr"></el-input>
        </el-form-item>
      </el-form>
      <div style="color: black;display: flex; justify-content: center">
        需支付
        <span style="margin-left: 10px; color: red">{{form.price}}元</span>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"  @click.stop="onSubmit()">立即发布</el-button>
      </div>
    </el-dialog>

    <!--订单详情-->
    <el-dialog title="订单详情" :visible.sync="detailDialogVisible">
      <div>
        <div style="display: flex; flex-direction: row;">
          <div style="flex: 1; border-right: 1px solid #cccccc">
            <div class="detail_box">
              <span class="detail_title">订单编号:</span>
              <span class="detail_content">{{orderDetail.orderNo}}</span>
            </div>

            <!--取货信息-->
            <div class="detail_box">
              <span class="detail_title">取货地址:</span>
              <span class="detail_content">{{orderDetail.address.address}}</span>
            </div>
            <div class="detail_box">
              <span class="detail_title">取货人:</span>
              <span class="detail_content">{{orderDetail.address.userName}}</span>
            </div>
            <div class="detail_box">
              <span class="detail_title">取货人电话:</span>
              <span class="detail_content">{{orderDetail.address.phone}}</span>
            </div>

            <!--收货信息-->
            <div class="detail_box" v-if="orderDetail.targetAddress">
              <span class="detail_title">收货地址:</span>
              <span class="detail_content">{{orderDetail.targetAddress.address}}</span>
            </div>
            <div class="detail_box" v-if="orderDetail.targetAddress">
              <span class="detail_title">收货人:</span>
              <span class="detail_content">{{orderDetail.targetAddress.userName}}</span>
            </div>
            <div class="detail_box" v-if="orderDetail.targetAddress">
              <span class="detail_title">收货人电话:</span>
              <span class="detail_content">{{orderDetail.targetAddress.phone}}</span>
            </div>

            <div class="detail_box">
              <span class="detail_title">物品名称:</span>
              <span class="detail_content">{{orderDetail.name}}</span>
            </div>

            <div class="detail_box" v-if="orderDetail.img">
              <span class="detail_title">图片:</span>
              <span class="detail_content">
                <el-image :src="orderDetail.img" :fit="fit" :preview-src-list="[orderDetail.img]" style="width: 200px;"></el-image></span>
            </div>
          </div >
          <div style="flex: 1">
            <div class="detail_box">
              <span class="detail_title">备注:</span>
              <span class="detail_content">{{orderDetail.descr || '无'}}</span>
            </div>
            <div class="detail_box">
              <span class="detail_title">物品类型:</span>
              <span class="detail_content">{{orderDetail.type}}</span>
            </div>
            <div class="detail_box">
              <span class="detail_title">物品重量:</span>
              <span class="detail_content">{{orderDetail.weight}} kg</span>
            </div>
            <div class="detail_box">
              <span class="detail_title">费用:</span>
              <span class="detail_content">{{orderDetail.price}}</span>
            </div>
            <div class="detail_box">
              <span class="detail_title">下单时间:</span>
              <span class="detail_content">{{orderDetail.time}}</span>
            </div>
            <div class="detail_box" v-if="orderDetail.acceptTime">
              <span class="detail_title">接单时间:</span>
              <span class="detail_content">{{orderDetail.acceptTime}}</span>
            </div>
            <div class="detail_box" v-if="orderDetail.arriveTime">
              <span class="detail_title">送达时间:</span>
              <span class="detail_content">{{orderDetail.arriveTime}}</span>
            </div>

            <!--骑手信息-->
            <div class="detail_box" v-if="orderDetail.certification">
              <span class="detail_title">骑手姓名:</span>
              <span class="detail_content">{{orderDetail.certification.name}}</span>
              <el-tooltip effect="light" content="聊天" placement="right" :hide-after="2000">
                <i @click="chat(orderDetail.certification.userId)" class="el-icon-chat-dot-round" style="font-size: 18px; margin-left: 3px; cursor: pointer"></i>
              </el-tooltip>
            </div>
            <div class="detail_box" v-if="orderDetail.certification">
              <span class="detail_title">骑手电话:</span>
              <span class="detail_content">{{orderDetail.certification.phone}}</span>
            </div>
          </div>
        </div>
      </div>


      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="detailDialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
        title="提示"
        :visible.sync="commentVisible"
        width="40%">
      <div>
        <el-form :model="commentForm" :rules="commentRules" ref="commentForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="评分" prop="star">
            <el-rate v-model="commentForm.star" show-text></el-rate>
          </el-form-item>
          <el-form-item label="评价" prop="content">
            <el-input type="textarea" placeholder="请输入内容" v-model="commentForm.content" :autosize="{ minRows: 3}" show-word-limit></el-input>
          </el-form-item>
        </el-form>
      </div>

      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
        title="提示"
        :visible.sync="arriveVisible"
        width="30%">
      <el-form :model="midTemp">
        <el-form-item label="图片" prop="img">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :before-upload="beforeAvatarUpload"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="midTemp.img" :src="midTemp.img" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click="confirm(midTemp,'待收货')">确 定</el-button>
  </span>
    </el-dialog>
  </el-container>
</template>

<script>
export default {
  name: "FrontOrders",
  data() {
    return{
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      orders: [],
      pageNum: 1,   // 当前的页码
      pageSize: 6,  // 每页显示的个数
      total: 0,
      orderDetail: {img: '',arriveTime: '',acceptTime: '',address:{},targetAddress:{},certification: {}},
      current: '抢单大厅',
      dialogFormVisible: false,
      detailDialogVisible: false,
      commentVisible: false,
      arriveVisible: false,
      commentForm: {},
      midTemp: {},
      form: {addressId: '', targetId: '', name: '', weight: 0.5, date1: '', date2: '', price: 1, type: '', descr: ''},
      rules: {
        name: [
          { required: true, message: '请输入物品名称', trigger: 'blur' },
        ],
        descr: [
          { required: true, message: '请选择活动区域', trigger: 'change' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        weight: [
          { required: true, message: '请输入物品重量', trigger: 'blur' }
        ],
        date1: [
          { required: true, message: '请选择日期', trigger: 'change'}
        ],
        date2: [
          { required: true, message: '请选择时间', trigger: 'change' }
        ],
        addressId: [
          { required: true, message: '请填写取货地址', trigger: 'blur' }
        ],
        targetId: [
          { required: true, message: '请填写收货地址', trigger: 'blur' }
        ],
      },
      commentRules: {
        star: [
          { required: true, message: '请评分', trigger: 'change'}
        ]
      },
      addressList: [],
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    chat(userId) {
      if (this.user.id === userId) {
        this.$message.warning('自己不能给自己留言！！！')
        return
      }
      this.$request.post('/chatGroup/add', { chatUserId: userId, userId: this.user.id }).then(res => {
        this.$router.push('/chat')
      })
    },
    save() {
      this.$request.post('/comment/add', this.commentForm).then(res => {
        if (res.code === '200') {
          this.$message.success('评价成功')
        } else {
          this.$message.error(res.msg)
        }
        this.commentVisible = false
        this.load()
      })
    },
    beforeAvatarUpload(file) {
      const format = file.type === 'image/jpeg'|| 'image/png';
      if (!format) {
        this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
      }
      return format;
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.midTemp, 'img', response.data)
    },
    comment(item) {
      this.commentVisible = true
      this.commentForm.userId = this.user.id
      this.commentForm.orderId = item.id
      this.commentForm.acceptId = item.acceptId
    },
    detail(id) {
      if (this.current === '我接收的' || this.current === '我发布的' ) {
        this.detailDialogVisible = true
        this.$request.get('/orders/selectById/' + id).then(res => {
          this.orderDetail = res.data || {}
        })
      }
    },
    confirm(item,status) {
      this.arriveVisible = false
      item.status = status
      this.$request.put('/orders/update', item).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    acceptOrders(item) {
      if (!this.user.isRider) {  // 判断是否是骑手
        this.$message.info('只有骑手才能接单')
        return
      }
      if (item.userId === this.user.id) {
        this.$message.error('不能接收自己的订单')
        return
      }
      this.$request.put('/orders/accept', item).then(res => {
        if (res.code === '200') {
          this.$message.success('接单成功')
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    add() {
      this.$parent.child = 1
      this.$parent.handleAdd()
    },
    openDialog(msg) {
      this.form.addressId = ''
      this.form.targetId = ''
      this.form.name = ''
      this.form.date1 = ''
      this.form.date2 = ''
      this.form.price = ''
      this.form.descr = ''
      this.form.type = msg
      this.dialogFormVisible = true
    },
    onSubmit() {
      if (this.form.addressId === this.form.targetId){
        this.$message.warning('取货地址与收货地址不能相同')
        return
      }
      this.$refs.form.validate((valid) => {
        if (valid) {
          let expTime = this.form.date1 + ' ' + this.form.date2
          this.form.expectTime = expTime
          this.$request.post('/orders/addOrder',this.form).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('发布成功')
              this.dialogFormVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    handleArrive(item) {
      this.arriveVisible=true
      this.midTemp = item
    },
    getAddress() {
      this.$request.get('/address/selectAll', {
        params: {userId: this.user.id}
      }).then(res => {
        this.addressList = res.data || []
      })
    },
    handleCurrentChange(pageNum) {
      if (this.current === '抢单大厅'){
        this.load(pageNum)
      }else if (this.current === '我发布的'){
        this.load(pageNum)
      }else if (this.current === '我接收的'){
        this.load(pageNum)
      }
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      let parameters = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        property: "悬赏"
      }
      if (this.current === '抢单大厅'){
        parameters.status = '待接单'
      }else if (this.current === '我发布的'){
        parameters.userId = this.user.id
      }else if (this.current === '我接收的'){
        parameters.acceptId = this.user.id
      }
      this.$request.get('/orders/selectPage', {
        params: parameters
      }).then(res => {
        this.orders = res.data?.list
        this.total = res.data?.total
      })
    },
    onload(pageNum,current){
      this.current = current
      this.load(pageNum)
    },
  },
}
</script>

<style scoped>
.text {
  font-size: 15px;
}

.item {
  margin-bottom: 12px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.order-item {
  border: 1px solid transparent;
  cursor: pointer;
}

.el-menu {
  width: 200px;
  height: 100%;
  background-color: #fff;
}

.el-submenu__title {
  color: #666;
  height: 50px;
  line-height: 50px;
}

.el-menu-item {
  color: #666;
  height: 50px;
  line-height: 50px;
  background-color: #fff !important; /* 必须加上这个背景色，否则鼠标离开会出现白色 */
}

.el-menu-item.is-active {
  background-color: #cce2ff !important;
  color: #409eff;
  border-right: 3px solid #409eff;
}

.el-menu--inline .el-menu-item {
  padding-left: 50px !important;
}

.el-menu .is-active {
  color: #409eff !important;
}

.el-submenu__title:hover {
  background-color: #e6f1ff !important;
}

.el-menu-item:hover {
  background-color: #e6f1ff !important;
  color: #409eff;
}

.el-card__header {
  padding: 10px 20px;
  border-bottom: 1px solid #ebeef5;
  box-sizing: border-box;
}

.line {
  display: flex;
  margin-bottom: 5px;
  grid-gap: 5px;
}

.line-title {
  width: 65px;
  text-align: right;
}

.line-content {
  flex: 1;
}
.detail_box {
  margin-top: 5px;
}
.detail_title {
  display: inline-block;
  width: 100px;
  text-align: right;
  font-weight: bold;
}
.detail_content {
  margin-left: 8px;
}
/deep/ .el-statistic .con span {
  display: inline-block;
  margin: 0;
  line-height: 100%;
  color: red;
}
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