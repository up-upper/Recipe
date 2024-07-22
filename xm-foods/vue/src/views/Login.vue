<template>
  <div class="container">
    <div style="background-color: #B0E0E6; height: 70px;">
      <div style="margin: 0 auto; width: 50%; height: 100%; display: flex; align-items: center">
        <img src="@/assets/imgs/logo.png" alt="" style="width: 50px">
        <span style="font-weight: bold; font-size: 24px; margin-left: 15px">家庭健康食谱管理系统</span>
      </div>
    </div>

    <div style="margin-left: 12%; width: 50%; height: calc(100vh - 140px); display: flex; justify-content: center; grid-gap: 20px; align-items: center">
      <div style="flex: 1">
<!--        <img style="width: 100%;" src="@/assets/imgs/login.png" alt="">-->
      </div>

      <div style="flex: 1">
        <div style="width: 350px; padding: 50px 30px; background-color: #ffffffe3; border-radius: 5px;">
          <div style="text-align: center; font-size: 20px; margin-bottom: 30px; color: #333">欢 迎 登 录</div>
          <el-form :model="form" :rules="rules" ref="formRef">
            <el-form-item prop="username">
              <el-input size="medium" prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input size="medium" autocomplete="new-password" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
            </el-form-item>
            <el-form-item prop="role">
              <el-select style="width: 100%" v-model="form.role">
                <el-option value="ADMIN" label="管理员"></el-option>
                <el-option value="USER" label="普通用户"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="medium" style="width: 100%; background-color: #65c5b8; border-color: #65c5b8; color: #333" @click="login">登 录</el-button>
            </el-form-item>
            <div style="display: flex; align-items: center">
              <div style="flex: 1"></div>
              <div style="flex: 1; text-align: right">
                还没有账号？请 <a href="/register">注册</a>
              </div>
            </div>
          </el-form>
        </div>
      </div>
    </div>

    <div style="height: 50px; line-height: 70px; color: #666; text-align: center">
<!--      ©2009-2024 食味美食有限公司 证书备31888号 备1101312328888号 网文[2014]0774-174号-->
    </div>

  </div>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      form: { role: 'ADMIN' },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  created() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          this.$request.post('/login', this.form).then(res => {
            if (res.code === '200') {
              localStorage.setItem("xm-user", JSON.stringify(res.data))  // 存储用户数据
              this.$message.success('登录成功')
              setTimeout(() => {
                if (res.data.role === 'ADMIN') {
                  location.href = '/home'    // 跳转主页
                } else {
                  location.href = '/front/home'   // 跳转主页
                }
              }, 500)
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
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url('../assets/imgs/bg3.jpg');
  background-size: 100%;
}
a {
  color: #2a60c9;
}
</style>