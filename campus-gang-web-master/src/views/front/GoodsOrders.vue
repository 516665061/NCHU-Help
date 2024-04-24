<template>
  <div>

    <div style="margin-bottom: 10px; display: flex">
      <div style="flex: 1">
        <el-radio-group v-model="role" size="mini" @change="load(1)">
          <el-radio-button label="buy">我购买的订单</el-radio-button>
          <el-radio-button label="sale">我出售的订单</el-radio-button>
        </el-radio-group>
      </div>
      <div>
        <el-input v-model="name" placeholder="请输入商品名称" style="width: 200px; margin-right: 5px" size="mini" clearable></el-input>
        <el-input v-model="orderNo" placeholder="请输入订单编号" style="width: 200px; margin-right: 5px" size="mini" clearable></el-input>
        <el-select v-model="status" style="width: 200px; margin-right: 5px;" clearable size="mini">
          <el-option value="已取消"></el-option>
          <el-option value="待支付"></el-option>
          <el-option value="代发货"></el-option>
          <el-option value="待送达"></el-option>
          <el-option value="待收货"></el-option>
          <el-option value="已完成"></el-option>
        </el-select>
        <el-button type="primary" size="mini" plain @click="load(1)">搜索</el-button>
        <el-button type="primary" size="mini" plain @click="reset">重置</el-button>
      </div>
    </div>

      <el-table :data="tableData">
        <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="goodsImg" label="商品图片" show-overflow-tooltip align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.goodsImg" style="width: 50px;" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单编号" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="price" label="总价" show-overflow-tooltip align="center">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="下单时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="payTime" label="支付时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="arriveTime" label="送达时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="targetAddress.address" show-overflow-tooltip label="收货地址" align="center"></el-table-column>
        <el-table-column prop="targetAddress.phone" show-overflow-tooltip label="联系方式" align="center"></el-table-column>
        <el-table-column prop="targetAddress.userName" show-overflow-tooltip label="收货人名称" align="center">
          <template v-slot="scope">
            {{ scope.row.targetAddress.userName }}
            <el-tooltip effect="light" content="聊天" placement="right" :hide-after="2000">
              <i @click="chat(scope.row.userId)" class="el-icon-chat-dot-round" style="font-size: 18px; margin-left: 3px; cursor: pointer"></i>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" align="center">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '待支付'">待支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待发货'">待发货</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待送达'">待送达</el-tag>
            <el-tag type="warning" v-if="scope.row.status === '待收货'">待收货</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已完成'">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesName" label="卖家名称" align="center">
          <template v-slot="scope">
            {{ scope.row.salesName }}
            <el-tooltip effect="light" content="聊天" placement="right" :hide-after="2000">
              <i @click="chat(scope.row.acceptId)" class="el-icon-chat-dot-round" style="font-size: 18px; margin-left: 3px; cursor: pointer"></i>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" v-if="scope.row.status === '待支付' && scope.row.userId === user.id" type="primary" plain @click="pay(scope.row, '待发货')">支付</el-button>
            <el-button size="mini" v-if="scope.row.status === '待支付' && scope.row.userId === user.id" type="danger" plain @click="cancel(scope.row, '已取消')">取消订单</el-button>
            <div v-if="scope.row.status === '待支付' && scope.row.userId === user.id">
              <el-statistic :value="new Date(scope.row.time).getTime()+1000*60*60*24" time-indices format="HH:mm:ss" :value-style="{ color: 'red', fontSize: '13px' }" suffix="">
                <template slot="suffix" class="statistic-suffix">
                  后自动取消订单
                </template>
              </el-statistic>
            </div>
            <el-button size="mini" v-if="scope.row.status === '待发货' && scope.row.acceptId === user.id" type="primary" plain @click="pick(scope.row,'待送达')">发货</el-button>
            <el-button size="mini" v-if="scope.row.status === '待送达' && scope.row.acceptId === user.id" type="primary" plain @click="arrive(scope.row,'待收货')">送达</el-button>
            <el-button size="mini" v-if="scope.row.status === '待收货' && scope.row.userId === user.id" type="primary" plain @click="achieve(scope.row,'已完成')">收货</el-button>
            <div v-if="scope.row.status === '待收货' && scope.row.userId === user.id">
              <el-statistic :value="new Date(scope.row.arriveTime).getTime()+1000*60*60*24*7" time-indices format="HH:mm:ss" :value-style="{ color: 'red', fontSize: '13px' }" suffix="">
                <template slot="suffix" class="statistic-suffix">
                  后自动确认收货
                </template>
              </el-statistic>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 5px">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>

    <el-dialog title="订单信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="商品名称" prop="goodsName">
          <el-input v-model="form.goodsName" placeholder="商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品图片" prop="goodsImg">
          <el-input v-model="form.goodsImg" placeholder="商品图片"></el-input>
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="订单编号"></el-input>
        </el-form-item>
        <el-form-item label="总价" prop="total">
          <el-input v-model="form.price" placeholder="总价"></el-input>
        </el-form-item>
        <el-form-item label="下单时间" prop="time">
          <el-input v-model="form.time" placeholder="下单时间"></el-input>
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-input v-model="form.payTime" placeholder="支付时间"></el-input>
        </el-form-item>
        <el-form-item label="下单人ID" prop="userId">
          <el-input v-model="form.userId" placeholder="下单人ID"></el-input>
        </el-form-item>
        <el-form-item label="收货地址" prop="address">
          <el-input v-model="form.address" placeholder="收货地址"></el-input>
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input v-model="form.phone" placeholder="联系方式"></el-input>
        </el-form-item>
        <el-form-item label="收货人名称" prop="userName">
          <el-input v-model="form.userName" placeholder="收货人名称"></el-input>
        </el-form-item>
        <el-form-item label="订单状态" prop="status">
          <el-input v-model="form.status" placeholder="订单状态"></el-input>
        </el-form-item>
        <el-form-item label="卖家ID" prop="saleId">
          <el-input v-model="form.saleId" placeholder="卖家ID"></el-input>
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
export default {
  name: "GoodsOrders",
  data() {
    return {
      role: 'buy',
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      name: null,
      status: null,
      orderNo: null,
      ids: []
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    indexMethod(index) {
      return index + 1 + (this.pageNum -1) * this.pageSize;
    },
    chat(userId) {
      if (this.user.id === userId) {
        this.$message.warning('自己不能给自己留言！！！')
        return
      }
      this.$request.post('/chatGroup/add', { chatUserId: userId, userId: this.user.id }).then(res => {
        this.$router.push('/chat')
      })
    },
    pay(row, status) {
      this.$confirm('您确定支付该订单吗？', '确认支付', {type: "warning"}).then(res => {
        this.changeStatus(row,status)
      }).catch(e => {
      })
    },
    cancel(row, status) {
      this.$confirm('您确定取消该订单吗？', '确认取消', {type: "error"}).then(res => {
        this.changeStatus(row,status)
      }).catch(e => {
      })
    },
    pick(row, status) {
      this.$confirm('您确定发货吗？', '确认发货', {type: "info"}).then(res => {
        this.changeStatus(row,status)
      }).catch(e => {
      })
    },
    arrive(row, status) {
      this.$confirm('您确定送达吗？', '确认送达', {type: "info"}).then(res => {
        this.changeStatus(row,status)
      }).catch(e => {
      })
    },
    achieve(row, status) {
      this.$confirm('您确定收货吗？', '确认收货', {type: "warning"}).then(res => {
        this.changeStatus(row,status)
      }).catch(e => {
      })
    },
    changeStatus(row, status) {
      this.form = JSON.parse(JSON.stringify(row))
      this.form.status = status
      this.$request.put('goodsOrders/update', this.form).then(res => {
        if (res.code === '200') {  // 表示成功保存
          this.$message.success('操作成功')
          this.load(1)
          this.fromVisible = false
        } else {
          this.$message.error(res.msg)  // 弹出错误的信息
        }
      })
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/goodsOrders/update' : '/goodsOrders/add',
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
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/goodsOrders/delete/' + id).then(res => {
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
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/goodsOrders/delete/batch', {data: this.ids}).then(res => {
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
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      let parameters = {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
        status: this.status,
        orderNo: this.orderNo
      }
      if (this.role === 'buy') {
        parameters.userId = this.user.id
      } else {
        parameters.acceptId = this.user.id
      }
      this.$request.get('/goodsOrders/selectPage', {
        params: parameters
      }).then(res => {
        if (res.code === '200') {
          this.tableData = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    reset() {
      this.name = null
      this.status = null
      this.orderNo = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>
/deep/ .el-statistic .con span {
  display: inline-block;
  margin: 0;
  line-height: 100%;
  color: red;
}
</style>