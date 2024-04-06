<template>
  <div class="main-content">
    <div style="margin-bottom: 10px">
      <el-input v-model="name" size="medium" style="width: 300px; margin-right: 10px"></el-input>
      <el-button type="primary" @click="loadGoods(1)">搜 索</el-button>
      <el-button type="warning" @click="reset">重 置</el-button>
    </div>
    <div>
      <el-row :gutter="10">
        <el-col :span="8" v-for="item in goodsList" :key="item.id">
          <div style="margin-bottom: 20px" class="goods-item transition" @click="$router.push('/goodsDetail?id=' + item.id)">
            <img :src="item.img" alt="" style="width: 150px; display: block; height: 150px; margin: 10px; border-radius: 5px">
            <div style="display: flex; flex:1; flex-direction: column; height: 100%; margin-top: 10px; width: 0">
              <div class="line" style="font-size: 16px; height: 105px; color: #555; margin-bottom: 10px">{{ item.name }}</div>
              <div style="display: flex; justify-content: space-between; align-items: baseline">
                <strong style="color: red; font-size: 20px">￥{{ item.price }}</strong>
                <span style="margin-right: 10px; font-size: 14px; color: #666">浏览量{{ item.readCount }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>

      <div style="margin: 15px auto">
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
  name: "Search",
  data() {
    return {
      goodsList: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 8,  // 每页显示的个数
      total: 0,
      name: '',
    }
  },
  created() {
    this.loadGoods(1)
  },
  methods: {
    loadGoods(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/selectFrontPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        if (res.code === '200') {
          this.goodsList = res.data?.list
          this.total = res.data?.total
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleCurrentChange(pageNum) {
      this.loadGoods(pageNum)
    },
    reset() {
      this.name = null
      this.loadGoods(1)
    }
  }
}
</script>

<style scoped>

</style>