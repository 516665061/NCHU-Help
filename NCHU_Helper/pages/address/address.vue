<template>
	<view style="padding: 20rpx;">
		<view class="box" style="margin-bottom: 10rpx;">
			<uni-forms :modelValue="form" :rules="rules" ref="formRef" label-width="160rpx" label-align="right" validateTrigger="blur">
				<uni-forms-item label="地址" name="address" required>
					<uni-easyinput type="text" v-model="form.address" placeholder="请输入地址" />
				</uni-forms-item>
				<uni-forms-item label="门牌号" name="doorNo" required>
					<uni-easyinput type="text" v-model="form.doorNo" placeholder="请输入门牌号" />
				</uni-forms-item>
				<uni-forms-item label="联系人" name="userName" required>
					<uni-easyinput type="text" v-model="form.userName" placeholder="请输入联系人" />
				</uni-forms-item>
				<uni-forms-item label="联系电话" name="phone" required>
					<uni-easyinput type="text" v-model="form.phone" placeholder="请输入联系电话" />
				</uni-forms-item>
				<view>
					<button type="primary" class="my-button-primary" @click="save">保存并使用</button>
				</view>
			</uni-forms>
		</view>
		
		<view class="box" v-if="addressList.length">
			<view v-for="item in addressList" :key="item.id">
				<view style="padding: 10rpx 0; border-bottom: 1px solid #eee;" @click="selectAddress(item)">
					<view style="font-weight: bold; font-size: 32rpx; margin-bottom: 10rpx;">{{ item.address + item.doorNo }}</view>
					<view style="color: #888; margin-bottom: 10rpx;">
						<text style="margin-right: 20rpx;">{{ item.userName }}</text>
						<text>{{ item.phone }}</text>
					</view>
					<view style="text-align: right;">
						<uni-icons type="compose" size="18" color="#888" style="margin-right: 10rpx;" @click.native.stop="handleEdit(item)"></uni-icons>
						<uni-icons type="trash" size="18" color="#888" @click.native.stop="del(item.id)"></uni-icons>
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
				user: uni.getStorageSync('xm-user'),
				addressList: [],
				form: {},
				rules: {
					address: {
						rules: [{
							required: true,
							errorMessage: '请填写地址',
						}]
					},
					doorNo: {
						rules: [{
							required: true,
							errorMessage: '请填写门牌号',
						}]
					},
					userName: {
						rules: [{
							required: true,
							errorMessage: '请填写联系人',
						}]
					},
					phone: {
						rules: [{
							required: true,
							errorMessage: '请填写联系电话',
						}]
					}
				},
				addressType: '' 
			}
		},
		onLoad(option) {
			this.addressType = option.addressType  // 地址类型
			this.load()
		},
		methods: {
			selectAddress(address) {
				let orderStore = uni.getStorageSync('orderStore') || {}  // 先获取缓存的数据
				if (this.addressType === '取货') {
					orderStore.pickAddress = address
				} else {
					orderStore.receiveAddress = address
				}
				uni.setStorageSync('orderStore', orderStore)   // 再设置缓存的地址信息
				uni.redirectTo({
					url: '/pages/preOrder/preOrder'
				})
			},
			del(id) {
				this.$request.del('/address/delete/' + id).then(res => {
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
			handleEdit(address) {
				this.form = JSON.parse(JSON.stringify(address))
			},
			save() {
				this.$refs.formRef.validate().then(res => {
					this.form.userId = this.user.id
					this.$request.post('/address/add', this.form).then(res => {
						if (res.code === '200') {
							uni.showToast({
								icon: 'success',
								title: '操作成功'
							})
		
							if (this.addressType){
								this.selectAddress(res.data)  // 设置地址信息到缓存
							}
							
							
							this.form = {}
							this.load()
						} else {
							uni.showToast({
								icon: 'none',
								title: res.msg
							})
						}
					})
				}).catch(error => {
					console.error(error)
				})
			},
			load() {
				this.$request.get('/address/selectAll', { userId: this.user.id }).then(res => {
					this.addressList = res.data || []
				})
			}
		}
	}
</script>

<style>

</style>