<template>
	<view style="padding: 20rpx;">
		<view style="margin-bottom: 20rpx;">
			<swiper circular autoplay :interval="3000" :duration="500" indicator-dots style="height: 320rpx;" 
			  indicator-color="rgba(255, 255, 255, 0.6)" indicator-active-color="#006eff">
			  <swiper-item v-for="item in imgs" :key="item" >
			    <image :src="item" alt="" style="width: 100%; height: 350rpx;" />
			  </swiper-item>
			</swiper>
		</view>
		
		<view style="margin-bottom: 20rpx;">
			<uni-notice-bar v-if="content" show-icon single :text="content" />
		</view>
		
		<view style="display: flex; margin-bottom: 20rpx;" class="box">
			<view class="cartegory-item" @click="goPreOrder('代拿快递')">
				<image src="../../static/imgs/快递.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代拿快递</view>
			</view>
			<view class="cartegory-item" @click="goPreOrder('代取餐品')">
				<image src="../../static/imgs/取餐.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代取餐品</view>
			</view>
			<view class="cartegory-item" @click="goPreOrder('代买零食')">
				<image src="../../static/imgs/零食.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代买零食</view>
			</view>
			<view class="cartegory-item" @click="goPreOrder('代送鲜花')">
				<image src="../../static/imgs/花.png" style="width: 50%;" mode="widthFix"></image>
				<view style="flex: 1;">代送鲜花</view>
			</view>
		</view>
		
		<view class="box" style="color: #006eff; font-weight: bold; margin-bottom: 10rpx;">跑腿订单</view>
		<view>
			<view v-for="item in orderList" :key="item.id" class="box" style="margin-bottom: 10rpx;" @click="goDetail(item.id)">
				<view style="display: flex; align-items: center; margin-bottom: 20rpx;">
					<view style="flex: 1;">
						<uni-tag text="餐品" size="small" type="success" v-if="item.type === '代取餐品'"></uni-tag>
						<uni-tag text="快递" size="small" type="primary" v-if="item.type === '代拿快递'"></uni-tag> 
						<uni-tag text="零食" size="small" type="warning" v-if="item.type === '代买零食'"></uni-tag> 
						<uni-tag text="鲜花" size="small" type="error" v-if="item.type === '代送鲜花'"></uni-tag> 
						<text style="margin-left: 10rpx;">{{ item.name }}</text>
					</view>
					<view style="flex: 1; text-align: right;">
						<text style="color: #888;">跑腿费</text>
						<text style="color: red; font-size: 34rpx;">￥{{ item.price }}</text>
					</view>
				</view>
				
				<view style="display: flex; align-items: center;">
					<view style="flex: 1;">
						<text style="margin-right: 10rpx;">已下单{{ item.minutes }}分钟</text>
						<text style="color: orange;">待接单</text>
					</view>
					<view style="flex: 1; text-align: right;">
						<uni-tag text="接单" type="primary" size="small" @click.native.stop="accept(item)"></uni-tag>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				imgs: [
				  require('../../static/imgs/banner1.png'),
				  require('../../static/imgs/banner2.png'),
				],
				content: '',
				noticeList: [],
				inter: null,
				orderList: [],
				user: uni.getStorageSync('xm-user')
			}
		},
		onShow() {
			this.load()
			this.loadNotice()
		},
		onHide() {
			clearInterval(this.inter)
			this.inter = null
		},
		methods: {
			accept(orders) {
			  if (!this.user.isRider) {  // 判断是否是骑手
			    uni.showToast({
			      icon: 'none',
			      title: '只有认证骑手才可以接单'
			    })
			    return
			  }
			  this.$request.put('/orders/accept', orders).then(res => {
			    if (res.code === '200') {
			      uni.showToast({
			        icon: 'success',
			        title: '操作成功'
			      })
			      this.load()
			    } else {
			      uni.showToast({
			        icon: 'none',
			        title: res.msg
			      })
			    }
			  })
			},
			goDetail(orderId) {
				uni.navigateTo({
					url: '/pages/detail/detail?orderId=' + orderId
				})
			},
			goPreOrder(type) {
				let orderStore = uni.getStorageSync('orderStore') || {}  // 先获取缓存的数据
				orderStore.type = type   // 设置订单的类型
				uni.setStorageSync('orderStore', orderStore)
				uni.navigateTo({
					url: '/pages/preOrder/preOrder'
				})
			},
			load() {
				this.$request.get('/orders/selectAll', {
					status: '待接单'
				}).then(res => {
					this.orderList = res.data || []
				})
			},
			loadNotice() {
				this.$request.get('/notice/selectAll').then(res => {
					this.noticeList = res.data || []
					
					let i = 0
					this.content = this.noticeList.length ? this.noticeList[i].content : ''
					
					// 切换展示公告内容
					if (this.noticeList.length > 1) {
						this.inter = setInterval(() => {
							i++
							if (i === this.noticeList.length) {
								i = 0
							}
							this.content = this.noticeList[i].content
						}, 5000)
					}
				
				})
			}
			
		}
	}
</script>

<style>
	.cartegory-item {
		flex: 1; 
		display: flex; 
		justify-content: space-between; 
		align-items: center; 
		flex-direction: column; 
		grid-gap: 20rpx;
	}
</style>
