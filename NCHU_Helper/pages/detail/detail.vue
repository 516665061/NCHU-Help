<template>
	<view style="padding: 20rpx;">
		<view class="box" v-if="orders.id">
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">订单编号：</view>
				<view class="line-content">{{ orders.orderNo }}</view>
			</view>

			<view class="line">
				<view class="line-title">取货地址：</view>
				<view class="line-content">{{ orders.address.address + orders.address.doorNo }}</view>
			</view>
			<view class="line">
				<view class="line-title">取货联系人：</view>
				<view class="line-content">{{ orders.address.userName }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">取货电话：</view>
				<view class="line-content">{{ orders.address.phone }}</view>
			</view>

			<view class="line">
				<view class="line-title">收货地址：</view>
				<view class="line-content">{{ orders.targetAddress.address + orders.targetAddress.doorNo }}</view>
			</view>
			<view class="line">
				<view class="line-title">收货联系人：</view>
				<view class="line-content">{{ orders.targetAddress.userName }}</view>
			</view>
			<view class="line" style="margin-bottom: 40rpx;">
				<view class="line-title">收货电话：</view>
				<view class="line-content">{{ orders.targetAddress.phone }}</view>
			</view>

			<view class="line">
				<view class="line-title">物品名称：</view>
				<view class="line-content">{{ orders.name }}</view>
			</view>
			<view class="line">
				<view class="line-title">物品描述：</view>
				<view class="line-content">{{ orders.descr || '无'}}</view>
			</view>
			<view class="line">
				<view class="line-title">物品图片：</view>
				<view class="line-content">
					<view v-if="orders.img">
						<image :src="orders.img" mode="widthFix" style="width: 180rpx;"></image>
					</view>
				</view>
			</view>
			<view class="line">
				<view class="line-title">物品类型：</view>
				<view class="line-content">{{ orders.type }}</view>
			</view>
			<view class="line">
				<view class="line-title">物品重量：</view>
				<view class="line-content">{{ orders.weight }} kg</view>
			</view>
			<view class="line">
				<view class="line-title">小费：</view>
				<view class="line-content" style="color: red;">￥{{ orders.weight }}</view>
			</view>
			<view class="line">
				<view class="line-title">下单时间：</view>
				<view class="line-content">{{ orders.time }}</view>
			</view>
			<view v-if="orders.certification">
				<view class="line">
					<view class="line-title">骑手姓名：</view>
					<view class="line-content">{{ orders.certification.name }}</view>
				</view>
							
				<view class="line">
					<view class="line-title">骑手电话：</view>
				    <view class="line-content">{{ orders.certification.phone }}</view>
				</view>
							
				<view class="line">
					<view class="line-title">骑手照片：</view>
					<view class="line-content">
							<image :src="orders.certification.avatar" mode="widthFix" style="width: 180rpx;"></image>
					</view>
			</view>
			
		</view>
						
			<view style="margin-top: 20rpx;"><button type="primary" class="my-button-primary" @click="back">确认</button></view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orders: {}
			}
		},
		onLoad(option) {
			const orderId = option.orderId
			this.load(orderId)
		},
		methods: {
			back() {
				uni.navigateBack()
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
	.line {
		display: flex;
		margin-bottom: 10rpx;
		grid-gap: 10rpx;
	}

	.line-title {
		width: 200rpx;
		font-weight: bold;
		text-align: right;
	}

	.line-content {
		flex: 1;
	}
</style>