<template>
  <div class="front-home">
    <div style="width: 90%; margin: 120px auto 20px;">
      <div style="display: flex; align-items: flex-start; grid-gap: 10px">
        <div style="width: 150px;height: calc(-140px + 100vh); padding: 10px; text-align: center;" class="card">
          <div class="circle" :class="{'circle-active' : tag === item.name }" v-for="item in tags" :key="item.id" @click="handleClick(item.name)">{{ item.name }}</div>
        </div>

        <div style="flex: 1 1 0%; width: 0px;">
          <div class="card" style="padding: 10px; height: calc(-140px + 100vh);">
            <div v-show="tag === '我的商品'">
              <Goods></Goods>
            </div>
            <div v-show="tag === '我的帖子'">
              <UserPosts></UserPosts>
            </div>
            <div v-show="tag === '我的订单'">
              <GoodsOrders></GoodsOrders>
            </div>
            <div v-show="tag === '收藏商品'">
              <Collect></Collect>
            </div>
            <div v-show="tag === '收支明细'">
              <Records></Records>
            </div>
            <div v-show="tag === '个人中心'">
              <Person></Person>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Collect from "@/views/front/Collect.vue";
import Goods from "@/views/front/Goods.vue"
import GoodsOrders from "@/views/front/GoodsOrders.vue";
import Records from "@/views/component/Records.vue";
import UserPosts from "@/views/front/UserPosts.vue";
import Person from "@/views/component/Person.vue";
export default {
  name: "FPerson",
  components: {Collect,Goods,GoodsOrders,Records,UserPosts,Person},
  data() {
    return{
      tag: this.$route.query.tag || '我的商品',
      tags: [
        {name: "我的商品"},
        {name: "我的帖子"},
        {name: "我的订单"},
        {name: "收藏商品"},
        {name: "收支明细"},
        {name: "个人中心"},
      ],
    }
  },
  methods: {
    handleClick(name) {
      this.tag = name
    },
  }
}
</script>

<style scoped>

</style>