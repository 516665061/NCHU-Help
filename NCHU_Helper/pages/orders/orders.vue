<template>
	<view style="padding: 20rpx;">
		<view style="margin-bottom: 10rpx;">
			<uni-segmented-control :current="current" :values="items" @clickItem="onClickItem" styleType="text"
				activeColor="#006eff"></uni-segmented-control>
		</view>

		<view>
			<view v-for="item in orderList" :key="item.id" class="box" style="margin-bottom: 10rpx;"
				@click="goDetail(item.id)">
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
						<text style="margin-right: 20rpx;"
							v-if="item.status === '待接单' || item.status === '待送达' || item.status === '待收货'">已下单{{ item.minutes }}分钟</text>

						<text style="color: #888;" v-if="item.status === '已取消'">{{ item.status }}</text>
						<text style="color: orange;" v-if="item.status === '待接单'">{{ item.status }}</text>
						<text style="color: dodgerblue" v-if="item.status === '待送达'">{{ item.status }}</text>
						<text style="color: mediumpurple;" v-if="item.status === '待收货'">{{ item.status }}</text>
						<text style="color: indianred;" v-if="item.status === '待评价'">{{ item.status }}</text>
						<text style="color: #18bc37;" v-if="item.status === '已完成'">{{ item.status }}</text>
					</view>
					<view style="flex: 1; text-align: right;">
						<view style="display: inline-block;" v-if="item.status === '已取消' || item.status === '已完成'">
							<uni-icons type="trash" size="18" color="#888"
								style="position: relative; top: 4rpx;"></uni-icons>
							<text style="color: #888;" @click.native.stop="handleDel(item.id)">删除</text>
						</view>
						<uni-tag text="确认收货" type="primary" size="small" v-if="item.status === '待收货'" @click.native.stop="changeStatus(item,'待评价')"></uni-tag>
						<uni-tag text="取消订单" type="default" size="small" v-if="item.status === '待接单'" @click.native.stop="changeStatus(item,'已取消')"></uni-tag>
						<uni-tag text="评价" type="success" size="small" v-if="item.status === '待评价'" @click.native.stop="goComment(item.id)"></uni-tag>
					</view>
				</view>
			</view>
		</view>
		
		<view>
			<!-- 提示窗示例 -->
			<uni-popup ref="alertDialog" type="dialog">
				<uni-popup-dialog :type="msgType" cancelText="取消" confirmText="确认" title="删除确认" content="您确认删除订单吗？" 
					@confirm="del"></uni-popup-dialog>
			</uni-popup>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				items: ['全部订单', '待接单', '待送达', '待收货', '待评价'],
				orderList: [],
				current: '全部订单',
				user: uni.getStorageSync('xm-user'),
				orderId: 0
			}
		},
		onShow() {
			this.load()
		},
		methods: {
			goComment(orderId) {
				uni.navigateTo({
					url: '/pages/comment/comment?orderId=' + orderId 
				})
			}
			changeStatus(orders, status) {
			  orders.status = status
			  this.$request.put('/orders/update', orders).then(res => {
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
			del() {
				this.$request.del('/orders/delete/' + this.orderId).then(res => {
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
			handleDel(orderId) {
				this.orderId = orderId
				this.$refs.alertDialog.open()
			},
			goDetail(orderId) {
				uni.navigateTo({
					url: '/pages/detail/detail?orderId=' + orderId
				})
			},
			onClickItem(e) { // 点击菜单的时候触发
				this.current = this.items[e.currentIndex]
				this.load()
			},
			load() {
				let params = {
					userId: this.user.id
				}
				if (this.current !== '全部订单') {
					params.status = this.current
				}
				this.$request.get('/orders/selectAll', params).then(res => {
					this.orderList = res.data || []
				})
			}
		}
	}
</script>

<style>

</style>