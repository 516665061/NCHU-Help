<template>
	<view style="padding: 20rpx;">
		<view class="box">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="180rpx" label-align="right">
				<uni-forms-item label="姓名" name="name" required>
					<uni-easyinput type="text" v-model="form.name" placeholder="请输入姓名" />
				</uni-forms-item>
				<uni-forms-item label="本人照片" name="avatar" required>
					<uni-file-picker limit="1" :image-styles="imageStyles" :del-icon="false" :disable-preview="true"
						fileMediatype="image" v-model="imgs" @select="handleAvatarUploadSuccess"></uni-file-picker>
				</uni-forms-item>
				<uni-forms-item label="联系方式" name="phone" required>
					<uni-easyinput type="text" v-model="form.phone" placeholder="请输入联系方式" />
				</uni-forms-item>
				<uni-forms-item label="身份证号码" name="cardNo" required>
					<uni-easyinput type="text" v-model="form.cardNo" placeholder="请输入身份证号码" />
				</uni-forms-item>
				<uni-forms-item label="身份证正面" name="card1" required>
					<uni-file-picker limit="1" :image-styles="imageStyles" :del-icon="false" :disable-preview="true"
						fileMediatype="image" v-model="imgs1" @select="handleCard1UploadSuccess"></uni-file-picker>
				</uni-forms-item>
				<uni-forms-item label="身份证反面" name="card2" required>
					<uni-file-picker limit="1" :image-styles="imageStyles" :del-icon="false" :disable-preview="true"
						fileMediatype="image" v-model="imgs2" @select="handleCard2UploadSuccess"></uni-file-picker>
				</uni-forms-item>
				<uni-forms-item label="常住地址" name="address" required>
					<uni-easyinput type="text" v-model="form.address" placeholder="请输入常住地址" />
				</uni-forms-item>
				<uni-forms-item label="审核状态" name="status">
					<view style="padding-top: 15rpx;">
						<uni-tag type="default" v-if="!form.status" text="待申请"></uni-tag>
						<uni-tag type="warning" v-if="form.status === '待审核'" text="待审核">待审核</uni-tag>
						<uni-tag type="success" v-if="form.status === '通过'" text="通过">通过</uni-tag>
						<uni-tag type="error" v-if="form.status === '拒绝'" text="拒绝">拒绝</uni-tag>
					</view>
				</uni-forms-item>
				<uni-forms-item label="审核理由" name="reason" v-if="form.reason">
					<view style="padding-top: 15rpx;">
						{{ form.reason }}
					</view>
				</uni-forms-item>
				
				<view style="margin-bottom: 20rpx;">
					<button :disabled="form.status" class="my-button-primary" type="primary" @click="submitRequest">提交申请</button>
				</view>
				<view v-if="form.id && form.status === '拒绝'">
					<button class="my-button-danger" type="primary" @click="deleteRequest">删除申请</button>
				</view>
			</uni-forms>
		
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {},
				rules: {
					name: {
						rules: [{
							required: true,
							errorMessage: '请填写姓名',
						}]
					},
					phone: {
						rules: [{
							required: true,
							errorMessage: '请填写手机号',
						}]
					},
					cardNo: {
						rules: [{
							required: true,
							errorMessage: '请填写身份证号码',
						}]
					},
					address: {
						rules: [{
							required: true,
							errorMessage: '请填写常住地址',
						}]
					}
				},
				imgs: {},
				imgs1: {},
				imgs2: {},
				imageStyles: {
					"height": 80, // 边框高度
					"width": 80, // 边框宽度
					"border": { // 如果为 Boolean 值，可以控制边框显示与否
						"color": "#eee", // 边框颜色
						"width": "1px", // 边框宽度
						"style": "solid", // 边框样式
						// "radius": "50%" // 边框圆角，支持百分比
					}
				},
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad() {
			this.load()
		},
		methods: {
			deleteRequest() {
				this.$request.del('/certification/delete/' + this.form.id).then(res => {
					if (res.code === '200') {
						uni.showToast({
							icon: 'success',
							title: '删除成功'
						})
						this.load()
						uni.reLaunch({
							url: '/pages/certification/certification'
						})
							
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			},
			load() {  // 查询当前用户自己的认证信息
				this.$request.get('/certification/selectUserCertification').then(res => {
					this.form = res.data || {}
					if (this.form.avatar) {
						this.imgs.url = this.form.avatar
					}
					if (this.form.card1) {
						this.imgs1.url = this.form.card1
					}
					if (this.form.card2) {
						this.imgs2.url = this.form.card2
					}
					
				})
			},
			submitRequest() {
				this.$refs.formRef.validate().then(r => {
					this.form.userId = this.user.id 
					this.$request.post('/certification/add', this.form).then(res => {
						if (res.code === '200') {
							uni.showToast({
								icon: 'success',
								title: '提交成功'
							})
							
							this.load()
								
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
					})
				}).catch()
			},
			handleAvatarUploadSuccess(e) {
				let _this = this
				const filePath = e.tempFilePaths[0]
				uni.uploadFile({
					url: _this.$baseUrl + '/files/upload', //自己的后端接口（默认发送post请求） 注意 _this.$baseUrl需要在全局变量定义
					filePath: filePath,
					name: "file", //这里应为自己后端文件形参的名字
					success(res) {
						let url = JSON.parse(res.data || '{}').data // 获取返回的图像链接
						_this.form.avatar = url // 给表单图像属性赋值
						console.log(_this.form)
					}
				})
			},
			handleCard1UploadSuccess(e) {
				let _this = this
				const filePath = e.tempFilePaths[0]
				uni.uploadFile({
					url: _this.$baseUrl + '/files/upload', //自己的后端接口（默认发送post请求） 注意 _this.$baseUrl需要在全局变量定义
					filePath: filePath,
					name: "file", //这里应为自己后端文件形参的名字
					success(res) {
						let url = JSON.parse(res.data || '{}').data // 获取返回的图像链接
						_this.form.card1 = url // 给表单图像属性赋值
					}
				})
			},
			handleCard2UploadSuccess(e) {
				let _this = this
				const filePath = e.tempFilePaths[0]
				uni.uploadFile({
					url: _this.$baseUrl + '/files/upload', //自己的后端接口（默认发送post请求） 注意 _this.$baseUrl需要在全局变量定义
					filePath: filePath,
					name: "file", //这里应为自己后端文件形参的名字
					success(res) {
						let url = JSON.parse(res.data || '{}').data // 获取返回的图像链接
						_this.form.card2 = url // 给表单图像属性赋值
					}
				})
			},
		}
	}
</script>

<style>

</style>