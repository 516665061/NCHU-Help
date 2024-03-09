<template>
	<view style="padding: 20rpx;">
		<view class="box" style="margin-bottom: 10rpx;" @click="selectAddress('取货')">
			<uni-section title="取货地址" type="line"></uni-section>
			<view v-if="pickAddress.id">
				<view style="font-weight: bold; font-size: 32rpx; margin-bottom: 10rpx;">{{ pickAddress.address + pickAddress.doorNo }}</view>
				<view style="color: #888; margin-bottom: 10rpx;">
					<text style="margin-right: 20rpx;">{{ pickAddress.userName }}</text>
					<text>{{ pickAddress.phone }}</text>
				</view>
			</view>
			<view style="color: #888;" v-else>请选择取货地址</view>
		</view>
		<view class="box" style="margin-bottom: 10rpx;" @click="selectAddress('收货')">
			<uni-section title="收货地址" type="line"></uni-section>
			<view v-if="recieveAddress.id">
				<view style="font-weight: bold; font-size: 32rpx; margin-bottom: 10rpx;">{{ recieveAddress.address + recieveAddress.doorNo }}</view>
				<view style="color: #888; margin-bottom: 10rpx;">
					<text style="margin-right: 20rpx;">{{ recieveAddress.userName }}</text>
					<text>{{ recieveAddress.phone }}</text>
				</view>
			</view>
			<view style="color: #888;" v-else>请选择收货地址</view>
		</view>
		
		<view class="box">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="160rpx" label-align="right" validateTrigger="blur">
			  <uni-forms-item label="物品名称" name="name" required>
			    <uni-easyinput type="text" v-model="form.name" placeholder="请输入" />
			  </uni-forms-item>
			  <uni-forms-item label="描述信息" name="descr">
				<uni-easyinput type="text" v-model="form.descr" placeholder="请输入描述信息" />
			  </uni-forms-item>
			  <uni-forms-item label="重量(kg)" name="weight">
				<uni-easyinput type="text" v-model="form.weight" placeholder="请输入物品重量" />
			  </uni-forms-item>
			  <uni-forms-item label="小费(元)" name="price" required>
				<uni-number-box v-model="form.price" :min="1" style="position: relative; top: 10rpx;"></uni-number-box>
			  </uni-forms-item>
			  <uni-forms-item label="订单类型" name="type" required>
				  <uni-data-select v-model="form.type" :localdata="orderTypes"></uni-data-select>
			  </uni-forms-item>
			  <uni-forms-item label="物品图片" name="img">
				<uni-file-picker limit="1" :image-styles="imageStyles" :del-icon="false" :disable-preview="true" 
					fileMediatype="image" v-model="imgs"  @select="handleImgUploadSuccess"></uni-file-picker>
			  </uni-forms-item>
			  <uni-forms-item label="备注" name="comment">
				<uni-easyinput type="text" v-model="form.comment" placeholder="请输入备注信息" />
			  </uni-forms-item>
			</uni-forms>
			<view>
				<button type="primary" @click="addOrder" class="my-button-primary">提交订单</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: { price: 1, type: '' },  // 表单绑定的对象
				rules: {
				  name: {
				    rules: [{
				      required: true,
				      errorMessage: '请填写物品名称',
				    }]
				  },
				  price: {
				    rules: [{
				      required: true,
				      errorMessage: '请设置小费',
				    }]
				  },
				  type: {
				    rules: [{
				      required: true,
				      errorMessage: '请设置订单类型',
				    }]
				  }
				},
				imgs: [],
				imageStyles: {
				  "height": 80,	// 边框高度
				  "width": 80,	// 边框宽度
				  "border":{ // 如果为 Boolean 值，可以控制边框显示与否
				    "color":"#eee",		// 边框颜色
				    "width":"1px",		// 边框宽度
				    "style":"solid", 	// 边框样式
				    "radius":"50%" 		// 边框圆角，支持百分比
				  }
				},
				orderTypes: [
					{ value: '代拿快递', text: "代拿快递" },
					{ value: '代取餐品', text: "代取餐品" },
					{ value: '代买零食', text: "代买零食" },
					{ value: '代送鲜花', text: "代送鲜花" },
				],
				pickAddress: {},
				recieveAddress: {}
			}
		},
		onShow() {
			let orderStore = uni.getStorageSync('orderStore')
			this.form.type = orderStore?.type
			this.pickAddress = orderStore?.pickAddress || {}
			this.recieveAddress = orderStore?.recieveAddress || {}
		},
		methods: {
			selectAddress(addressType) {
				uni.navigateTo({
					url: '/pages/address/address?addressType=' + addressType
				})
			},
			addOrder() {
				if (!this.pickAddress.id) {
					uni.showToast({
						icon: 'none',
						title: '请设置取货地址'
					})
					return
				}
				if (!this.recieveAddress.id) {
					uni.showToast({
						icon: 'none',
						title: '请设置收货地址'
					})
					return
				}
				if (this.pickAddress.id === this.recieveAddress.id) {
					uni.showToast({
						icon: 'none',
						title: '取货地址不能跟收货地址一致'
					})
					return
				}
				// 设置表单里的地址信息
				this.form.addressId = this.pickAddress.id
				this.form.targetId = this.recieveAddress.id
				this.$refs.formRef.validate().then(res => {
					this.$request.post('/orders/addOrder', this.form).then(res => {
						if (res.code === '200') {
							uni.showToast({
								icon: 'success',
								title: '下单成功'
							})
							// 清除缓存
							uni.removeStorageSync('orderStore')
							
							setTimeout(() => {
								uni.switchTab({
									url: '/pages/index/index'
								})
							}, 500)
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
					})	
				})
				.catch(error => {
					console.error(error)
				})
			},
			handleImgUploadSuccess(e) {
				let _this = this
				const filePath = e.tempFilePaths[0]
				uni.uploadFile({
					url: _this.$baseUrl +'/files/upload', //自己的后端接口（默认发送post请求） 注意 _this.$baseUrl需要在全局变量定义
					filePath: filePath,
					name:"file",  //这里应为自己后端文件形参的名字
					success(res) {
					  let url = JSON.parse(res.data || '{}').data  // 获取返回的图像链接
					  _this.form.img = url    // 给表单图像属性赋值
					}
				})
			}
		}
	}
</script>

<style>

</style>
