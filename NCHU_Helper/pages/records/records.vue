<template>
	<view style="padding: 20rpx;">
		<view class="box" v-for="item in recordList" :key="item.id" style="margin-bottom: 10rpx;">
			<view style="display: flex; margin-bottom: 10rpx;">
				<view>
					<uni-tag type="success" text="充值" size="mini" v-if="item.type === '充值'"></uni-tag>
					<uni-tag type="primary" text="收入" size="mini"  v-if="item.type === '收入'"></uni-tag>
					<uni-tag type="error" text="支出" size="mini" v-if="item.type === '支出'"></uni-tag>
					<uni-tag type="warning" text="取消" size="mini" v-if="item.type === '取消'"></uni-tag>
					<text style="margin-left: 10rpx;">{{ item.content }}</text>
				</view>
				<view style="flex: 1; text-align: right; font-weight: bold;">
					<text style="color: red;" v-if="item.type === '充值'">+{{ item.money }}</text>
					<text style="color: dodgerblue;" v-if="item.type === '收入'">+{{ item.money }}</text>
					<text style="color: #888;" v-if="item.type === '支出'">-{{ item.money }}</text>
					<text style="color: orange;" v-if="item.type === '取消'">+{{ item.money }}</text>
				</view>
			</view>
			<view style="text-align: right; color: #888; font-size: 24rpx;">{{ item.time }}</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				recordList: [],
				user: uni.getStorageSync('xm-user')
			}
		},
		onLoad() {
			this.load()
		},
		methods: {
			load() {
				this.$request.get('/records/selectAll', { userId: this.user.id }).then(res => {
					this.recordList = res.data || []
				})
			}
		}
	}
</script>

<style>

</style>
