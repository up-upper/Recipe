<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入名称查询" style="width: 200px" v-model="title"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="title" label="名称" show-overflow-tooltip></el-table-column>
        <el-table-column prop="descr" label="描述" show-overflow-tooltip></el-table-column>
        <el-table-column label="图片">
          <template v-slot="scope">
            <el-image :src="scope.row.img" style="width: 50px; height: 50px; border-radius: 5px"
                      :preview-src-list="[scope.row.img]">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="mon" label="周一" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tues" label="周二" show-overflow-tooltip></el-table-column>
        <el-table-column prop="wed" label="周三" show-overflow-tooltip></el-table-column>
        <el-table-column prop="thur" label="周四" show-overflow-tooltip></el-table-column>
        <el-table-column prop="fri" label="周五" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sat" label="周六" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sun" label="周日" show-overflow-tooltip></el-table-column>
        <el-table-column prop="time" label="日期"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
            background
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="信息" :visible.sync="fromVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="名称" prop="title">
          <el-input v-model="form.title" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="title">
          <el-input v-model="form.descr" placeholder="描述"></el-input>
        </el-form-item>
        <el-form-item label="图片">
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
        <el-form-item label="周一" prop="mon">
          <el-input type="textarea" v-model="form.mon" placeholder="周一"></el-input>
        </el-form-item>
        <el-form-item label="周二" prop="tues">
          <el-input type="textarea" v-model="form.tues" placeholder="周二"></el-input>
        </el-form-item>
        <el-form-item label="周三" prop="wed">
          <el-input type="textarea" v-model="form.wed" placeholder="周三"></el-input>
        </el-form-item>
        <el-form-item label="周四" prop="thur">
          <el-input type="textarea" v-model="form.thur" placeholder="周四"></el-input>
        </el-form-item>
        <el-form-item label="周五" prop="fri">
          <el-input type="textarea" v-model="form.fri" placeholder="周五"></el-input>
        </el-form-item>
        <el-form-item label="周六" prop="sat">
          <el-input type="textarea" v-model="form.sat" placeholder="周六"></el-input>
        </el-form-item>
        <el-form-item label="周日" prop="sun">
          <el-input type="textarea" v-model="form.sun" placeholder="周日"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

<!--    <el-dialog title="内容" :visible.sync="fromVisible1" width="50%" :close-on-click-modal="false" destroy-on-close>-->
<!--      <div class="w-e-text">-->
<!--        <div v-html="content"></div>-->
<!--      </div>-->

<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="fromVisible1 = false">关 闭</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

  </div>
</template>

<script>
import E from "wangeditor"

export default {
  name: "Plan",
  data() {
    return {
      tableData: [],  // 所有的数据
      pageNum: 1,   // 当前的页码
      pageSize: 10,  // 每页显示的个数
      total: 0,
      title: null,
      fromVisible: false,
      form: {},
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
        ],
        mon: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ],
        tues: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ],
        wed: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ],
        thur: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ],
        fri: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ],
        sat: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ],
        sun: [
          {required: true, message: '请输入食谱', trigger: 'blur'},
        ]
      },
      ids: [],
      fromVisible1: false,
      // content: ''
    }
  },
  created() {
    this.load(1)
  },
  methods: {
    // preview(content) {
    //   this.content = content
    //   this.fromVisible1 = true
    // },
    handleAdd() {   // 新增数据
      this.form = {}  // 新增数据的时候清空数据
      this.fromVisible = true   // 打开弹窗
      this.setRichText('')
    },
    handleEdit(row) {   // 编辑数据
      this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
      this.fromVisible = true   // 打开弹窗
      // this.setRichText(this.form.content)
    },
    save() {   // 保存按钮触发的逻辑  它会触发新增或者更新
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request({
            url: this.form.id ? '/plan/update' : '/plan/add',
            method: this.form.id ? 'PUT' : 'POST',
            data: this.form
          }).then(res => {
            if (res.code === '200') {  // 表示成功保存
              this.$message.success('保存成功')
              this.load(1)
              this.fromVisible = false
            } else {
              this.$message.error(res.msg)  // 弹出错误的信息
            }
          })
        }
      })
    },
    del(id) {   // 单个删除
      this.$confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/plan/delete/' + id).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    handleSelectionChange(rows) {   // 当前选中的所有的行数据
      this.ids = rows.map(v => v.id)   //  [1,2]
    },
    delBatch() {   // 批量删除
      if (!this.ids.length) {
        this.$message.warning('请选择数据')
        return
      }
      this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
        this.$request.delete('/plan/delete/batch', {data: this.ids}).then(res => {
          if (res.code === '200') {   // 表示操作成功
            this.$message.success('操作成功')
            this.load(1)
          } else {
            this.$message.error(res.msg)  // 弹出错误的信息
          }
        })
      }).catch(() => {
      })
    },
    load(pageNum) {  // 分页查询
      if (pageNum) this.pageNum = pageNum
      this.$request.get('/plan/selectPage', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          title: this.title,
        }
      }).then(res => {
        this.tableData = res.data?.list
        this.total = res.data?.total
      })
    },
    reset() {
      this.title = null
      this.load(1)
    },
    handleCurrentChange(pageNum) {
      this.load(pageNum)
    },
    handleImgSuccess(res) {
      this.form.img = res.data
    },
    setRichText(html) {
      this.$nextTick(() => {
        this.editor = new E(`#editor`)
        this.editor.config.uploadImgServer = this.$baseUrl + '/files/editor/upload'
        this.editor.config.uploadFileName = 'file'
        this.editor.config.uploadImgHeaders = {
          token: this.user.token
        }
        this.editor.config.uploadImgParams = {
          type: 'img',
        }
        this.editor.create()  // 创建
        this.editor.txt.html(html)
      })
    },
  }
}
</script>

<style>
.w-e-text img {
  width: 100%;
}
</style>
