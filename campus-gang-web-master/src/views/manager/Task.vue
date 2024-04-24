<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入订单编号查询" style="width: 200px" v-model="orderNo"></el-input>
      <el-button type="primary" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="primary" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
        <el-table-column prop="orderNo" label="订单编号" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="name" label="物品名称" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="descr" label="描述" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="img" label="物品图片" align="center">
          <template v-slot="scope">
            <el-image :src="scope.row.img" style="width: 50px; border-radius: 5px" :preview-src-list="[scope.row.img]"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="物品类型" align="center"></el-table-column>
        <el-table-column prop="weight" label="物品重量" align="center"></el-table-column>
        <el-table-column prop="price" label="小费" align="center">
          <template v-slot="scope">
            <span style="color: red">￥{{ scope.row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="发起人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="acceptName" label="接单人" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="创建时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="acceptTime" label="接单时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="arriveTime" label="送达时间" align="center" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="订单状态" align="center">
          <template v-slot="scope">
            <el-tag type="danger" v-if="scope.row.status === '已取消'">已取消</el-tag>
            <el-tag type="info" v-if="scope.row.status === '待接单'">待接单</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待送达'">待送达</el-tag>
            <el-tag type="primary" v-if="scope.row.status === '待收货'">待收货</el-tag>
            <el-tag type="warning" v-if="scope.row.status === '待评价'">待评价</el-tag>
            <el-tag type="success" v-if="scope.row.status === '已完成'">已完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="addressId" label="取货信息" align="center">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="showAddress(scope.row.addressId)">查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="targetId" label="送货信息" align="center">
          <template v-slot="scope">
            <el-button type="primary" size="mini" @click="showAddress(scope.row.targetId)">查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="descr" label="订单备注" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="150" fixed="right">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="信息" :visible.sync="fromVisible" width="40%" :close-on-click-modal="false" destroy-on-close>
      <el-form label-width="100px" style="padding-right: 50px" :model="form" ref="formRef">
        <el-form-item prop="status" label="订单状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option value="已取消"></el-option>
            <el-option value="待接单"></el-option>
            <el-option value="待送达"></el-option>
            <el-option value="待收货"></el-option>
            <el-option value="待评价"></el-option>
            <el-option value="已完成"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="content" label="内容">
          <el-input type="textarea" :rows="5" v-model="form.content" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="地址信息" :visible.sync="fromVisible1" width="30%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="address" label-width="100px">
        <el-form-item label="地址: ">
          <div>{{ address.address }}</div>
        </el-form-item>
        <el-form-item label="联系人: ">
          <div>{{ address.userName }}</div>
        </el-form-item>
        <el-form-item label="联系电话: ">
          <div>{{ address.phone }}</div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="fromVisible1 = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Tasks",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 5,  // 每页显示的个数
      total: 0,
      orderNo: null,
      form:{},
      fromVisible:false,
      fromVisible1:false,
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      ids: [],
      address:{},
      targetAddress:{}
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    indexMethod(index) {
      return index + 1 + (this.pageNum -1) * this.pageSize;
    },
    showAddress(id) {
      this.$request.get('/address/selectById/' + id).then(res => {
        this.address = res.data || {}
        this.fromVisible1 = true
      })
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/orders/update' : '/orders/add',
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
        this.$request.delete('/orders/delete/' + id).then(res => {
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
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/orders/delete/batch', {data: this.ids}).then(res => {
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
      this.$request.get('/orders/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          orderNo: this.orderNo
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
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

</style>
