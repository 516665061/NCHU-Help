<template>
  <div>
    <div class="search">
      <el-date-picker
          v-model="date"
          type="date"
          value-format="yyyy-MM-dd"
          clearable
          placeholder="选择日期">
      </el-date-picker>
      <el-button type="primary" plain style="margin-left: 10px" @click="load()">查询</el-button>
      <el-button type="primary" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>
    <el-table :data="tableData" :default-sort = "{prop: 'time', order: 'descending'}">
      <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
      <el-table-column prop="content" label="事件" align="center" show-overflow-tooltip></el-table-column>
      <el-table-column prop="userName" label="用户" align="center"></el-table-column>
      <el-table-column prop="type" label="类型" align="center"
                       :filters="[{ text: '充值', value: '充值' }, { text: '支出', value: '支出' }, { text: '收入', value: '收入' }, { text: '退回', value: '取消' }]"
                       :filter-method="filterTag"
                       filter-placement="bottom-end">>
        <template v-slot="scope">
          <el-tag type="success" v-if="scope.row.type === '收入'">收入{{scope.row.money}}元</el-tag>
          <el-tag type="danger" v-if="scope.row.type === '支出'">支出{{scope.row.money}}元</el-tag>
          <el-tag type="primary" v-if="scope.row.type === '充值'">充值{{scope.row.money}}元</el-tag>
          <el-tag type="info" v-if="scope.row.type === '取消'">退回{{scope.row.money}}元</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="时间" align="center" show-overflow-tooltip></el-table-column>
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
  </div>
</template>

<script>
export default {
  name: "Records",
  data() {
    return {
      tableData: [],  // 所有的数据
      date: '',
      pageNum: 1,   // 当前的页码
      pageSize: 8,  // 每页显示的个数
      total: 0,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {},
      ids: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    indexMethod(index) {
      return index + 1 + (this.pageNum -1) * this.pageSize;
    },
    filterTag(value, row) {
      return row.type === value;
    },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
    },
    load(pageNum) {  // 查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/records/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          userId: this.user.id,
          time: this.date,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.date = null
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
  }
}
</script>

<style scoped>
.search {
  padding: 10px;
  background-color: #fff;
  border-radius: 5px;
  text-align: center;
}
.table {
  height: 40vh;
  overflow: hidden;
}
.el-table {}
</style>
