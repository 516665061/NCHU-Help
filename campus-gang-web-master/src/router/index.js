import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [
  {
    path: '/admin',
    name: 'Manager',
    component: () => import('../views/Manager'),
    redirect: '/admin/home',  // 重定向到主页
    children: [
      { path: 'home', name: 'Home', meta: { name: '系统首页' }, component: () => import('../views/manager/Home') },
      { path: 'adminInfo', name: 'Admin', meta: { name: '管理员信息' }, component: () => import('../views/manager/Admin') },
      { path: 'adminPerson', name: 'AdminPerson', meta: { name: '个人信息' }, component: () => import('../views/manager/AdminPerson') },
      { path: 'password', name: 'Password', meta: { name: '修改密码' }, component: () => import('../views/manager/Password') },
      { path: 'notice', name: 'Notice', meta: { name: '公告信息' }, component: () => import('../views/manager/Notice') },
      { path: 'user', name: 'User', meta: {name: '用户信息'}, component: () => import('../views/manager/User') },
      { path: 'goods', name: 'Goods', meta: {name: '商品信息'}, component: () => import('../views/manager/Goods') },
      { path: 'tasks', name: 'Tasks', meta: {name: '悬赏订单'}, component: () => import('../views/manager/Task') },
      { path: 'category', name: 'Category', meta: { name: '分类信息' }, component: () => import('../views/manager/Category') },
      { path: 'circles', name: 'Circles', meta: { name: '圈子管理' }, component: () => import('../views/manager/Circles') },
      { path: 'orders', name: 'Orders', meta: {name: '订单信息'}, component: () => import('../views/manager/Orders') },
      { path: 'address', name: 'Address', meta: {name: '地址信息'}, component: () => import('../views/manager/Address') },
      { path: 'certification', name: 'Certification', meta: {name: '骑手认证'}, component: () => import('../views/manager/Certification') },
      { path: 'records', name: 'Records', meta: {name: '收支明细'}, component: () => import('../views/manager/Records') },
      { path: 'comment', name: 'Comment', meta: {name: '用户评价'}, component: () => import('../views/manager/Comment') },
      { path: 'slides', name: 'Slides', meta: {name: '幻灯片管理'}, component: () => import('../views/manager/Slides') },
      { path: 'lost', name: 'Lost', meta: {name: '失物招领信息'}, component: () => import('../views/manager/Lost') },
      { path: 'evaluate', name: 'Evaluate', meta: {name: '商品评价'}, component: () => import('../views/manager/Evaluate') },
      { path: 'posts', name: 'Posts', meta: {name: '社区帖子'}, component: () => import('../views/manager/Posts')},
    ]
  },
  {
    path: '/',
    name: 'Front',
    component: () => import('../views/Front'),
    redirect: '/home',
    children: [
      { path: 'home', name: 'FHome', meta: { name: '系统首页' }, component: () => import('../views/front/Home') },
      { path: 'market', name: 'Market', meta: { name: '校园市场' }, component: () => import('../views/front/Market') },
      { path: 'lost', name: 'FLost', meta: { name: '失物招领' }, component: () => import('../views/front/Lost') },
      { path: 'orders', name: 'FrontOrder', meta: { name: '订单' }, component: () => import('../views/front/Orders') },
      { path: 'person', name: 'Person', meta: { name: '个人信息' }, component: () => import('../views/front/Person') },
      { path: 'certification', name: 'Certification', meta: {name: '骑手认证'}, component: () => import('../views/front/Certification')},
      { path: 'goodsDetail', name: 'GoodsDetail', meta: {name: '商品详情'}, component: () => import('../views/front/GoodsDetail')},
      { path: 'addGoods', name: 'AddGoods', meta: {name: '发布商品'}, component: () => import('../views/front/AddGoods')},
      { path: 'posts', name:'Posts', meta:{name: '论坛帖子'}, component: () => import('../views/front/Posts') },
      { path: 'postsDetail', name:'PostsDetail', meta:{name: '帖子详情'}, component: () => import('../views/front/PostsDetail') },
      { path: 'chat', name:'Chat', meta:{name: '聊天'}, component: () => import('../views/front/Chat') },
      { path: 'person', name:'FPerson', meta:{name: '个人中心'}, component: () => import('../views/front/Person') },
    ]
  },
  { path: '/login', name: 'Login', meta: { name: '登录' }, component: () => import('../views/Login') },
  { path: '/register', name: 'Register', meta: { name: '注册' }, component: () => import('../views/Register') },
  { path: '/403', name: 'NoAuth', meta: { name: '无权限' }, component: () => import('../views/403') },
  { path: '*', name: 'NotFound', meta: { name: '无法访问' }, component: () => import('../views/404') },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to ,from, next) => {
  let adminPaths = ['/admin/home','/admin/adminInfo','/admin/adminPerson','/admin/password','/admin/notice','/admin/user','/admin/orders','/admin/address','/admin/certification','/admin/records','/admin/comment','/admin/slides']
  let userPaths = ['/orders','/records','/person','/certification']
  let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
  if ((user.role !== 'ADMIN' && adminPaths.includes(to.path))|| (user.role !== 'USER' && userPaths.includes(to.path))) {
    next('/403')
  } else {
    next()
  }
})

router.afterEach((to, from, next) => {
  window.scrollTo(0, 0)
})

export default router
