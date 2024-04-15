<template>
  <div style="position: fixed; top: 100px;">
    <div class="el-affix" style="height: 180px; width: 150px;">
      <div class="el-affix--fixed" style="height: 180px; width: 150px; top: 200px; z-index: 100;">
        <div class="el-affix-container">
          <a href="http://www.nchu.edu.cn" style="">
            <div class="el-affix-items">
              <div class="el-affix-item">
                <img src="@/assets/imgs/logo.png" style="height: 20px; width: 20px;">
                <div class="left-hot" style="color: red;">南昌航空大学</div>
              </div>
            </div>
          </a>
          <el-popover
              placement="right"
              width="300"
              trigger="hover">
            <div style="width: 300px; height: 300px">
              <div style="text-align: center; margin-top: 20px;">微信扫描二维码了解</div>
              <div style="display: flex; align-items: center;justify-content: center">
                <img src="@/assets/imgs/wechatbinary.jpg" alt="" style="width: 250px; height: 250px; border: 1px solid rgb(204, 204, 204); border-radius: 10px;">
              </div>
            </div>
            <div class="el-affix-items" slot="reference">
              <div class="el-affix-item">
                <img src="@/assets/imgs/wechat.png" style="height: 20px; width: 20px;">
                <div class="left-hot">添加微信咨询</div>
              </div>
            </div>
          </el-popover>
          <el-popover
              placement="right"
              width="300"
              trigger="hover"
          >
            <div style="width: 300px; height: 300px">
              <div style="text-align: center; margin-top: 20px;">QQ扫描二维码了解</div>
              <div style="display: flex; align-items: center;justify-content: center">
                <img src="@/assets/imgs/qqbinary.jpg" alt="" style="width: 250px; height: 250px; border: 1px solid rgb(204, 204, 204); border-radius: 10px;">
              </div>
            </div>
            <div class="el-affix-items" slot="reference">
              <div class="el-affix-item">
                <img src="@/assets/imgs/qq.png" style="height: 20px; width: 20px;">
                <div class="left-hot">添加QQ咨询</div>
              </div>
            </div>
          </el-popover>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Affix"
}
</script>

<style scoped>

</style>