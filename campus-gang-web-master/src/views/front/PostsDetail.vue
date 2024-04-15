<template>
  <div style="width: 60%; margin: 120px auto">
    <div class="card" style="margin-bottom: 10px">
      <div style="font-size: 20px; text-align: center; margin-bottom: 20px; font-weight: bold">{{ posts.title }}</div>
      <div style="color: #666; margin-bottom: 40px; text-align: center;">
        <span><i class="el-icon-user"></i> {{ posts.userName }}</span>
        <span style="margin-left: 20px"><i class="el-icon-time"></i> {{ posts.time }}</span>
        <span style="margin-left: 20px"><i class="el-icon-reading"></i> {{ posts.readCount }}</span>
      </div>
      <div style="border-bottom: 1px solid rgb(238, 234, 238); margin: 30px 20px 0px"></div>
      <div class="w-e-text" v-html="posts.content"></div>
    </div>

    <div class="card" style="padding: 20px 30px">
      <Evaluate :fid="id" module="posts" />
    </div>
  </div>
</template>

<script>
import Evaluate from "@/views/component/Evaluate.vue";
export default {
  name: "PostsDetail",
  components: {Evaluate},
  data() {
    return {
      id: this.$route.query.id,
      posts: {}
    }
  },
  created() {
    this.$request.put('/posts/updateCount/' + this.id).then(res => {
      this.load()
    })
  },
  methods: {
    load() {
      this.$request.get('/posts/selectById/' + this.id).then(res => {
        this.posts = res.data || {}
      })
    }
  }
}
</script>

<style scoped>

</style>