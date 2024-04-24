<template>
  <div class="front-home">
    <Affix></Affix>
    <div class="main-content">
      <div style="display: flex; grid-gap: 20px; margin-bottom: 40px">
        <img :src="goods.img" alt="" style="width: 50%; height: 400px; display: block">
        <div style="flex: 1; width: 0">
          <el-tooltip :content="goods.name" placement="top-start">
            <div  class="line1" style="font-weight: bold; font-size: 26px; margin: 20px 0">{{ goods.name }}</div>
          </el-tooltip>
          <div style="color: #666; font-size: 16px;">
            <span>浏览 {{ goods.readCount }}</span>
            <span style="margin-left: 20px">点赞 {{ goods.likesCount }}</span>
            <span style="margin-left: 20px">收藏 {{ goods.collectCount }}</span>
          </div>
          <div style="color: red; font-size: 24px; margin: 40px 0">￥{{ goods.price }}</div>
          <div style="margin-bottom: 20px"><span style="color: #666">发货地：</span> {{ goods.address }}</div>
          <div style="margin-bottom: 20px"><span style="color: #666">卖家：</span>
            {{ goods.userName }}
            <el-tooltip effect="light" content="聊天" placement="right" :hide-after="2000">
              <i @click="chat(goods.userId)" class="el-icon-chat-dot-round" style="font-size: 18px; margin-left: 3px; cursor: pointer"></i>
            </el-tooltip>
          </div>
          <div style="margin-bottom: 40px"><span style="color: #666">发布日期：</span> {{ goods.date }}</div>
          <div>
            <el-button v-if="!goods.userLikes" @click="addLikes" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">点赞</el-button>
            <el-button v-if="goods.userLikes" @click="addLikes" size="medium" style="background-color: orangered; color: #eee; border-color: orangered">已点赞</el-button>
            <el-button v-if="!goods.userCollect" @click="addCollect" size="medium" type="warning">收藏</el-button>
            <el-button v-if="goods.userCollect" @click="addCollect" size="medium" type="warning">已收藏</el-button>
            <el-button size="medium" type="danger" @click="handleBuy">立即购买</el-button>
          </div>
        </div>
      </div>

      <div>
        <div style="display: flex; border-bottom: 1px solid orangered; margin-bottom: 10px">
          <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === '商品详情' }" @click="changeItem('商品详情')">商品详情</div>
          <div style="padding: 10px 20px; cursor: pointer" :class="{ 'active' : current === '商品评论' }" @click="changeItem('商品评论')">商品评论</div>
        </div>

        <div v-if="current === '商品详情'">
          <div v-html="goods.content"></div>
        </div>

        <div v-if="current === '商品评论'">
          <Evaluate :fid="id" module="goods"></Evaluate>
        </div>

        <el-dialog title="选择收货地址" :visible.sync="formVisible" width="30%" :close-on-click-model="false" destroy-on-close>
          <div>
            <el-select v-model="form.targetId" placeholder="请选择" @click.native.stop="loadAddress" clearable>
              <el-option
                  v-for="item in addressList"
                  :key="item.id"
                  :label= "'地址：' + item.address + '  姓名：' + item.userName + '  联系方式：' + item.phone"
                  :value="item.id">
              </el-option>
            </el-select>
            <div style="color: #409EFF; margin-top: 10px" @click="add">还没有收货地址？去创建</div>
          </div>
          <div slot="footer" class="dialog-footer">
            <el-button @click="formVisible = false">取 消</el-button>
            <el-button type="primary" @click="addOrder">确 定</el-button>
          </div>
        </el-dialog>

      </div>
    </div>
  </div>
</template>

<script>
import Evaluate from '@/views/component/Evaluate.vue'
import Affix from "@/views/component/Affix.vue";
export default {
  name: "GoodsDetail",
  components: {Evaluate,Affix},
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      id: this.$route.query.id,
      goods: {},
      current: '商品详情',
      formVisible: false,
      form: {},
      addressList: []
    }
  },
  created() {
    this.$request.put('/goods/updateReadCount/' + this.id).then(res => {
      this.load()
    })
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
    add() {
      this.$parent.child = 1
      this.$parent.handleAdd()
    },
    addOrder() {
      if (!this.form.targetId) {
        this.$message.warning('请选择收货地址')
        return
      }
      this.form.goodsId = this.id
      this.$request.post('goodsOrders/add', this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('下单成功')
          this.$router.push('/person?tag=我的订单')
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    loadAddress() {
      this.$request.get('/address/selectAll', {
        params: {userId: this.user.id}
      }).then(res => {
        this.addressList = res.data || []
      })
    },
    handleBuy() {
      if (this.goods.userId === this.user.id) {
        this.$message.warning('不可购买自己的商品')
        return
      }
      this.form = {}
      this.formVisible = true
    },
    addCollect(){
      this.$request.post('/collect/add', { fid: this.goods.id, module: 'goods'}).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    addLikes(){
      this.$request.post('/likes/add', { fid: this.goods.id, module: 'goods'}).then(res => {
        if (res.code === '200') {
          this.$message.success('操作成功')
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    changeItem(current) {
      this.current = current
    },
    load() {
      this.$request.get('/goods/selectById/' + this.id).then(res => {
        this.goods = res.data || {}
      })
    }
  }
}
</script>

<style scoped>
.active {
  background-color: orangered;
  color: #eee;
}
</style>