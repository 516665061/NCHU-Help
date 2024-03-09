<template>
	<view style="padding: 20rpx;">
		<view class="box" v-for="item in commentList" :key="item.id" style="margin-bottom: 10rpx; color: #666;" 
			@click="goDetail(item.orderId)">
			<view style="margin-bottom: 10rpx;">{{ item.content }}</view>
			<view style="display: flex;">
				<view style="flex: 1;">
					<uni-rate :value="item.star" readonly></uni-rate>
				</view>
				<view style="padding-top: 10rpx; font-size: 24rpx;">{{ item.time }}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				commentList: [],
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad() {
			this.load()
		},
		methods: {
			goDetail(orderId) {
				console.log(orderId)
				uni.navigateTo({
					url: '/pages/detail/detail?orderId=' + orderId
				})
			},
			load() {
				this.$request.get('/comment/selectComment/' + this.user.id).then(res => {
					this.commentList = res.data || []
				})
			}
		}
	}
</script>

<style>

</style>
