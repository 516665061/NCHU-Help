<template>
  <div class="front-home">
    <Affix></Affix>
    <div class="main-content" style="background-color: #FFFFFF; padding: 10px; position: relative">

      <el-button @click="$router.push('/addGoods')" style="position: absolute; top: 20px; right: -150px" size="medium" type="primary" icon="el-icon-plus" plain>发布商品</el-button>
      <el-button @click="$router.push('/search')" style="position: absolute; top: 60px; right: -150px" size="medium" type="primary" icon="el-icon-search" plain>搜索商品</el-button>

      <div style="margin-top: 20px; margin-bottom: 20px">
        <el-select v-model="category" size="medium" style="width: 200px" @change="loadGoods(1)">
          <el-option value="全部"></el-option>
          <el-option v-for="item in categoryList" :key="item.id" :value="item.name"></el-option>
        </el-select>

        <el-select v-model="sort" size="medium" style="width: 200px; margin-left: 10px" @change="loadGoods(1)">
          <el-option value="最新"></el-option>
          <el-option value="最热"></el-option>
        </el-select>
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
      </div>

    </div>
  </div>
</template>

<script>
import Affix from "@/views/component/Affix.vue";
export default {
  name: "Market",
  components: {Affix},
  data() {
    return {
      category: '全部',
      sort: '最新',
      categoryList: [],
      goodsList: [],  // 所有的数据
    }
  },
  mounted() {
    this.loadCategory()
    this.loadGoods(1)
  },
  methods: {
    loadCategory() {
      this.$request.get('/category/selectAll').then(res => {
        this.categoryList = res.data || []
      })
    },
    loadGoods(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/goods/selectFrontPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          category: this.category === '全部' ? null : this.category,
          sort: this.sort
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
  }
}
</script>

<style scoped>

</style>