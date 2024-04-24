<template>
  <div>
    <div class="search">
      <el-input v-model="orderNo" placeholder="请输入订单编号" style="width: 200px; margin-right: 5px" clearable></el-input>
      <el-select v-model="status" style="width: 200px; margin-right: 5px" clearable>
        <el-option value="已取消"></el-option>
        <el-option value="待支付"></el-option>
        <el-option value="待发货"></el-option>
        <el-option value="待收货"></el-option>
        <el-option value="已完成"></el-option>
      </el-select>
      <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="primary" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
        <el-table-column prop="goodsName" label="商品名称" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="goodsImg" label="商品图片" align="center">
          <template v-slot="scope">
            <el-image v-if="scope.row.goodsImg" style="width: 50px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单编号" width="120" align="center"></el-table-column>
        <el-table-column prop="price" label="总价" align="center">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="下单时间" show-overflow-tooltip align="center"></el-table-column>
        <el-table-column prop="payTime" label="支付时间" show-overflow-tooltip align="center"></el-table-column>

        <el-table-column prop="status" label="订单状态" align="center">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="danger" v-if="scope.row.status === '待支付'">待支付</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待发货'">待发货</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待收货'">待收货</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已完成'">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="salesName" label="卖家名称" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template v-slot="scope">
            <el-button size="mini" plain type="danger" @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin: 15px 0">
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
    </div>
  </div>
</template>
<script>
export default {
  name: "Orders",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
      },
      ids: [],
      orderNo: null,
      status: null
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    indexMethod(index) {
      return index + 1 + (this.pageNum -1) * this.pageSize;
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
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
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
      this.$request.get('/goodsOrders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          status: this.status,
          orderNo: this.orderNo,
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
    reset() {
      this.orderNo = null
      this.status = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>

</style>