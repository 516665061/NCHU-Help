<template>
  <div class="front-home">
    <Affix></Affix>

    <div class="main-content">
      <div style="text-align: right">
        <el-input placeholder="请输入内容" size="medium" v-model="inputMsg" clearable style="width: 500px; margin-right: 10px; "></el-input>
        <el-button size="medium" @click="(activeName === 'first' || activeName === 'second') ? load(1):loadList(1)">搜索</el-button>
      </div>

      <div style="margin-top: 20px;">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="失物广场" name="first">
            <div style="margin-top: 10px">
              <el-row :gutter="10">
                <el-col :span="6" v-for="item in gridData" style="margin-top: 10px">
                  <div class="transition card">
                    <div style="display: flex;">
                      <img :src="item.img" alt="" style="height: 75px; width: 75px; border: 2px solid #eeeeee">
                      <div style="flex: 1 1 0; margin-left: 5px; width: 0;">
                        <div class="overflowShow" style="font-weight: 550; color: #409EFF">{{ item.name }}</div>
                        <el-tooltip class="item" effect="dark" :content=item.userName placement="top">
                          <div class="overflowShow" style="margin-top: 10px; color: #666666">发布人：{{ item.userName }}</div>
                        </el-tooltip>
                        <el-tooltip class="item" effect="dark" :content=item.time placement="top">
                          <div class="overflowShow" style="margin-top: 5px; color: #666666">时间：{{ item.time }}</div>
                        </el-tooltip>
                      </div>
                    </div>
                    <div style="margin-top: 20px; text-align: center">
                      <el-button type="info" size="mini" @click="viewEditor(item.content)">查看详情</el-button>
                      <el-button type="primary" size="mini" @click="contact(item.userId)">联系失主</el-button>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div style="margin: 10px;">
               <el-pagination
                  :hide-on-single-page="true"
                  background
                  @current-change="handleCurrentChange"
                  :current-page="pageNum"
                  :page-sizes="[5, 10, 20]"
                  :page-size="pageSize"
                  layout="total, prev, pager, next, jumper"
                  :total="total">
              </el-pagination>
            </div>
          </el-tab-pane>
          <el-tab-pane label="招领广场" name="second">
            <div style="margin-top: 10px">
              <el-row :gutter="10">
                <el-col :span="6" v-for="item in gridData" style="margin-top: 10px">
                  <div class="transition card">
                    <div style="display: flex;">
                      <img :src="item.img" alt="" style="height: 75px; width: 75px; border: 2px solid #eeeeee">
                      <div style="flex: 1 1 0; margin-left: 5px; width: 0;">
                        <div class="overflowShow" style="font-weight: 550; color: #409EFF">{{ item.name }}</div>
                        <el-tooltip class="item" effect="dark" :content=item.userName placement="top">
                          <div class="overflowShow" style="margin-top: 10px; color: #666666">发布人：{{ item.userName }}</div>
                        </el-tooltip>
                        <el-tooltip class="item" effect="dark" :content=item.time placement="top">
                          <div class="overflowShow" style="margin-top: 5px; color: #666666">时间：{{ item.time }}</div>
                        </el-tooltip>
                      </div>
                    </div>
                    <div style="margin-top: 20px; text-align: center">
                      <el-button type="info" size="mini" @click="viewEditor(item.content)">查看详情</el-button>
                      <el-button type="primary" size="mini" @click="contact(item.userId)">联系他</el-button>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div style="margin: 10px;">
               <el-pagination
                  :hide-on-single-page="true"
                  background
                  @current-change="handleCurrentChange"
                  :current-page="pageNum"
                  :page-sizes="[5, 10, 20]"
                  :page-size="pageSize"
                  layout="total, prev, pager, next, jumper"
                  :total="total">
              </el-pagination>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的失物" name="third">
            <div>
              <div style="margin: 20px 0;">
                <el-button type="primary" plain @click="handleAdd()">发布失物</el-button>
                <el-button type="danger" plain @click="delBatch()">批量删除</el-button>
              </div>

              <div class="card">
                <el-table :data="myLostData" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
                  <el-table-column type="selection" width="55" align="center"></el-table-column>
                  <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
                  <el-table-column prop="img" label="物品主图" align="center">
                    <template v-slot="scope">
                      <div style="display: flex; align-items: center">
                        <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.img"
                                  :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="name" label="物品名称" show-overflow-tooltip align="center"></el-table-column>
                  <el-table-column prop="content" label="物品描述" align="center">
                    <template v-slot="scope">
                      <el-button type="primary" plain size="mini" @click="viewEditor(scope.row.content)">查看详情</el-button>
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态" align="center"></el-table-column>
                  <el-table-column prop="time" label="时间" show-overflow-tooltip align="center"></el-table-column>
                  <el-table-column label="操作" width="180" align="center">
                    <template v-slot="scope">
                      <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
                      <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>

                <div style="margin: 10px;">
                   <el-pagination
                      :hide-on-single-page="true"
                      background
                      @current-change="handleCurrentChange"
                      :current-page="pageNum"
                      :page-sizes="[5, 10, 20]"
                      :page-size="pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="total">
                  </el-pagination>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="我的招领" name="fourth">
            <div>
              <div style="margin: 20px 0;">
                <el-button type="primary" plain @click="handleAdd()">发布招领</el-button>
                <el-button type="danger" plain @click="delBatch()">批量删除</el-button>
              </div>

              <div class="card">
                <el-table :data="myLostData" @selection-change="handleSelectionChange" :header-cell-style="{'text-align':'center'}">
                  <el-table-column type="selection" width="55" align="center"></el-table-column>
                  <el-table-column type="index" :index="indexMethod" label="序号" align="center"></el-table-column>
                  <el-table-column prop="img" label="物品主图" align="center">
                    <template v-slot="scope">
                      <div style="display: flex; align-items: center">
                        <el-image style="width: 40px; height: 40px; border-radius: 50%" v-if="scope.row.img"
                                  :src="scope.row.img" :preview-src-list="[scope.row.img]"></el-image>
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="name" label="物品名称" show-overflow-tooltip align="center"></el-table-column>
                  <el-table-column prop="content" label="物品描述" align="center">
                    <template v-slot="scope">
                      <el-button type="primary" size="mini" plain @click="viewEditor(scope.row.content)">查看详情</el-button>
                    </template>
                  </el-table-column>
                  <el-table-column prop="status" label="状态" align="center"></el-table-column>
                  <el-table-column prop="time" label="时间" show-overflow-tooltip align="center"></el-table-column>
                  <el-table-column label="操作" width="180" align="center">
                    <template v-slot="scope">
                      <el-button plain type="primary" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
                      <el-button plain type="danger" size="mini" @click=del(scope.row.id)>删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>

                <div style="margin: 10px;">
                   <el-pagination
                      :hide-on-single-page="true"
                      background
                      @current-change="handleCurrentChange"
                      :current-page="pageNum"
                      :page-sizes="[5, 10, 20]"
                      :page-size="pageSize"
                      layout="total, prev, pager, next, jumper"
                      :total="total">
                  </el-pagination>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <el-dialog title="信息" :visible.sync="fromVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
        <el-form label-width="100px" style="padding-right: 50px" :model="form" :rules="rules" ref="formRef">
          <el-form-item prop="img" label="图片">
            <el-upload
                class="avatar-uploader"
                :action="$baseUrl + '/files/upload'"
                :headers="{ token: user.token }"
                list-type="picture"
                :on-success="handleImgSuccess"
            >
              <el-button type="primary">上传图片</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item prop="name" label="物品名称">
            <el-input v-model="form.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item prop="status" label="物品状态">
            <el-select prefix-icon="el-icon-key" v-model="form.status" placeholder="请选择状态">
              <el-option v-if="activeName === 'third'" label="丢失中" value="丢失中"></el-option>
              <el-option v-else label="未找到失主" value="未找到失主"></el-option>
              <el-option v-if="activeName === 'third'" label="已找到" value="已找到"></el-option>
              <el-option v-else label="已找到失主" value="已找到失主"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="content" label="物品描述">
            <div id="editor"></div>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="fromVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="详细信息" :visible.sync="viewVisible" width="55%" :close-on-click-modal="false" destroy-on-close>
        <div v-html="viewData" class="w-e-text w-e-text-container"></div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import Edit from 'wangeditor'
import Affix from "@/views/component/Affix.vue";
export default {
  name: "FLost",
  data() {
    return{
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      form: {},
      editor: null,
      inputMsg: '',
      activeName: this.$route.query.activeName || 'first',
      fromVisible: false,
      viewVisible: false,
      viewData: null,
      pageNum: 1,   // 当前的页码
      pageSize: 12,  // 每页显示的个数
      total: 0,
      gridData: [],
      myLostData: [],
      ids: [],
      rules: {
        name: [
          {required: true, message: '请输入物品名称', trigger: 'blur'},
        ],
        status: [
          {required: true, message: '请选择物品状态', trigger: 'change'},
        ],
        content: [
          {required: true, message: '请输入内容', trigger: 'blur'},
        ]
      },
    }
  },
  components: {
    Affix,
  },
  mounted() {
    this.load(1)
  },
  methods: {
    indexMethod(index) {
      return index + 1 + (this.pageNum -1) * this.pageSize;
    },
    contact(userId) {
      if (this.user.id === userId) {
        this.$message.warning('自己不能给自己留言！！！')
        return
      }
      this.$request.post('/chatGroup/add', { chatUserId: userId, userId: this.user.id }).then(res => {
        this.$router.push('/chat')
      })
    },
    load(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/lost/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.inputMsg,
          status: this.activeName === 'first' ? '丢失中' : '未找到失主'
        }
      }).then(res => {
        this.gridData = res.data?.list
        this.total = res.data?.total
      })
    },
    loadList(pageNum) {
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/lost/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.inputMsg,
          status: this.activeName === 'third' ? '丢失中' : '未找到失主',
          viceStatus: this.activeName === 'third' ? '已找到' : '已找到失主',
          userId: this.user.id
        }
      }).then(res => {
        this.myLostData = res.data?.list
        this.total = res.data?.total
      })
    },
    handleCurrentChange(pageNum) {
      if (this.activeName === 'first' || this.activeName === 'second'){
        this.load(pageNum);
      }else {
        this.loadList(pageNum)
      }
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/lost/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.loadList(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    viewEditor(content) {
      this.viewData = content
      this.viewVisible = true
    },
    initWangEditor(content) {
      this.$nextTick(() => {
        this.editor = new Edit('#editor')
        this.editor.config.placeholder = '请输入内容'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
        this.editor.create()
        setTimeout(() => {
          this.editor.txt.html(content)
        })
      })
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.initWangEditor(this.form.content || '')
      this.fromVisible = true   // 打开弹窗
    },
    handleClick(tab, event) {
      console.log(tab, event);
      this.inputMsg = ''
      if (this.activeName === 'first' || this.activeName === 'second'){
        this.pageSize = 12
        this.load(1);
      }else {
        this.pageSize = 8
        this.loadList(1)
      }
    },
    handleAdd() {
      this.form = {}  // 新增数据的时候清空数据
      this.initWangEditor('')
      this.fromVisible = true   // 打开弹窗
    },
    handleImgSuccess(res) {
      this.form.img = res.data
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.form.content = this.editor.txt.html()
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/lost/update' : '/lost/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.fromVisible = false
              this.loadList(1)
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/lost/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.loadList(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
  }
}
</script>

<style scoped>

</style>