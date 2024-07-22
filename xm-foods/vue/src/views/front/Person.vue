<template>
  <div class="main-content">
    <el-card style="width: 50%; margin: 30px auto">
      <div style="text-align: right; margin-bottom: 20px">
        <el-button type="primary" @click="updatePassword">修改密码</el-button>
      </div>
      <el-form :model="user" label-width="80px" style="padding-right: 20px">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="user.sex">
            <el-radio label="男"></el-radio>
            <el-radio label="女"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="years">
          <el-input v-model="user.years" placeholder="年龄"></el-input>
        </el-form-item>
        <el-form-item label="家庭成员">
          <div style="display: flex; align-items: center; margin-bottom: 5px" v-for="(item, index) in numberList" :key="index">
            <el-input style="flex: 1;" v-model="item.name" placeholder="请输入成员名称"></el-input>
            <el-input style="flex: 1;  margin-left: 10px"  v-model="item.value" placeholder="请输入成员年龄"></el-input>
            <i @click="delnumber(index)" class="el-icon-delete" style="margin-left: 10px; cursor: pointer"></i>
          </div>
          <div><span style="color: #2a60c9; font-size: 14px; cursor: pointer" @click="addnumber">添加家庭成员</span></div>
        </el-form-item>
        <el-form-item label="体质">
          <el-radio-group v-model="user.feature">
            <el-tooltip class="item" effect="dark" content="主要表现为乏力、气短、容易疲劳" placement="top">
              <el-radio label="气虚">气虚体质</el-radio>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="常感到怕冷、手足不温" placement="top">
              <el-radio label="阳虚">阳虚体质</el-radio>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="会有手心、脚心发热、潮热盗汗等症状" placement="top">
              <el-radio label="阴虚">阴虚体质</el-radio>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="通常身材较肥胖，伴有舌苔厚腻等表现" placement="top">
              <el-radio label="痰湿">痰湿体质</el-radio>
            </el-tooltip>
            <el-tooltip class="item" effect="dark" content="通常精神充沛、身体健康" placement="top">
              <el-radio label="平和">平和体质</el-radio>
            </el-tooltip>
          </el-radio-group>
        </el-form-item>

        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="user" label-width="80px" style="padding-right: 20px" :rules="rules" ref="formRef">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      dialogVisible: false,
      form:{},
      yearsList: [],
      numberList: [],
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      if (this.id) {
        this.$request.get('/user/selectById/' + this.id).then(res => {
          this.user = res.data || {}
          this.numberList = JSON.parse(this.user.number || '[]')

        })
      }
    },
    update() {
      let url
      if (this.user.role === 'ADMIN') {
        url = '/admin/update'
      } else {
        url = '/user/update'
      }
      // 保存当前的用户信息到数据库
      this.$request.put(url, this.user).then(res => {
        if (res.code === '200') {
          // 成功更新
          this.$message.success('保存成功')
          // 更新浏览器缓存里的用户信息
          localStorage.setItem('xm-user', JSON.stringify(this.user))
          this.user.number = JSON.stringify(this.numberList)
          // 触发父级的数据更新
          this.$emit('update:user')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(response, file, fileList) {
      // 把user的头像属性换成上传的图片的链接
      this.$set(this.user, 'avatar', response.data)
    },
    // 修改密码
    updatePassword() {
      this.dialogVisible = true
    },
    addnumber() {
      this.numberList.push({ name: '', value: '' })
    },
    delnumber(index) {
      this.numberList.splice(index, 1)
    },
    save() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              // 成功更新
              this.$message.success('修改密码成功')
              this.$router.push('/login')
              this.form.number = JSON.stringify(this.numberList)
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}

.item {
  margin-right: 10px;
}
</style>