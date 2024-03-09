<template>
	<view style="padding: 20rpx;">
		<view class="box">
			<uni-row :gutter="10">
				<uni-col :span="8" v-for="item in items" :key="item">
					<view class="charge-item" :class="{ 'active' : current === item }" @click="clickItem(item)">￥{{ item }}</view>
				</uni-col>
			</uni-row>
			
			<view style="margin-top: 20rpx;">
				<button type="primary" style="background-color: #f94b4b;" @click="charge">支 付</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				current: 6,
				items: [6, 18, 68, 288, 588, 999]
			}
		},
		methods: {
			clickItem(item) {
				this.current = item
			},
			charge() {
				this.$request.put('/user/charge/' + this.current).then(res => {
					if (res.code === '200') {
						uni.showToast({
							icon: 'success',
							title: '操作成功'
						})
						uni.navigateBack()
					} else {
						uni.showToast({
							icon: 'none',
							title: res.msg
						})
					}
				})
			}
		}
	}
</script>

<style>
	.charge-item {
		border: 1px solid #ccc;
		padding: 40rpx 0;
		margin-bottom: 10rpx;
		text-align: center;
		font-size: 36rpx;
		color: #888;
		border-radius: 5rpx;
		cursor: pointer;
	}
	.active {
		background-color: #f94b4b;
		color: white;
		border-color: #f94b4b;
	}
</style>