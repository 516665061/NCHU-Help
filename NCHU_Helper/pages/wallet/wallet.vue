<template>
	<view style="padding: 20rpx;">
		<view style="background-color: dodgerblue; padding: 80rpx; font-size: 40rpx; text-align: center; border-radius: 10rpx; color: white;">
			余额 <text style="margin-left: 10rpx; color: gold;">{{ account }}</text>
		</view>
		
		<view style="margin-top: 20rpx;">
			<button type="primary" style="background-color: dodgerblue;" @click="handleCharge">充 值</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				account: 0,
				user: uni.getStorageSync('xm-user')
			}
		},
		onShow() {
			this.load()
		},
		methods: {
			handleCharge() {
				uni.navigateTo({
					url: '/pages/charge/charge'
				})
			},
			load() {
				this.$request.get('/user/selectById/' + this.user.id).then(res => {
					this.account = res.data.account || 0
				})
			}
		}
	}
</script>

<style>

</style>
