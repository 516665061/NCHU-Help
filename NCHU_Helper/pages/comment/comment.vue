<template>
	<view style="padding: 20rpx;">
		<view class="box" style="padding: 50rpx 20rpx;">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="140rpx" label-align="right">
				<uni-forms-item label="订单编号">
					<navigator :url="'/pages/detail/detail?orderId=' + orders.id" style="padding: 15rpx 0; color: dodgerblue;">{{ orders.orderNo }}</navigator>
				</uni-forms-item>
				<uni-forms-item label="内容" name="content" required>
					<uni-easyinput type="textarea" v-model="form.content" placeholder="请输入内容" />
				</uni-forms-item>
				<uni-forms-item label="评分" name="star" required>
					<view style="padding: 15rpx 0;">
						<uni-rate v-model="form.star" />
					</view>
				</uni-forms-item>
				<view style="margin-top: 20rpx;">
					<button type="primary" class="my-button-primary" @click="save">提 交</button>
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
					content: {
						rules: [{
							required: true,
							errorMessage: '请填写内容',
						}]
					},
					star: {
						rules: [{
							required: true,
							errorMessage: '请评分',
						}]
					}
				},
				orders: {},
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad(option) {
			this.load(option.orderId)
		},
		methods: {
			save() {
				this.form.userId = this.user.id
				this.form.orderId = this.orders.id
				this.form.acceptId = this.orders.acceptId
				this.$request.post('/comment/add', this.form).then(res => {
					if (res.code === '200') {
						uni.showToast({
							icon: 'success',
							title: '操作成功'
						})
						
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
					// 延时跳转
					setTimeout(() => {
						uni.navigateBack()
					}, 500)
				})
			},
			load(orderId) {
				this.$request.get('/orders/selectById/' + orderId).then(res => {
					this.orders = res.data || {}
				})
			}
		}
	}
</script>

<style>

</style>