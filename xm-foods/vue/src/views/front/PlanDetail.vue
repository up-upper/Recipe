<template>
  <div style="width: 50%; margin: 20px auto">
    <div style="display: flex">
      <img :src="plan.img" alt="" style="width: 50%; height: 350px; border-radius: 5px">
      <div style="padding-left: 20px">
        <div style="margin: 20px 0; font-size: 22px; font-weight: bold">名称：{{ plan.title }}</div>
        <div style="margin:20px 60px;font-size: 20px;">{{ plan.descr }}</div>
<!--        <div style="margin: 50px 0">别名：{{ plan.descr }}</div>-->
<!--        <div style="margin: 50px 0">功效：{{ plan.price }}</div>-->
<!--        <div style="margin: 20px 0">-->
<!--          <span style="color: #666">建议搭配：<span style="color: #333">{{ plan.address }}</span></span>-->
<!--          <span style="margin-left: 20px; color: #666">热量：<span style="color: #333">{{ plan.store }}</span></span>-->
<!--        </div>-->
        <!--        <div style="margin: 20px 0">-->
        <!--          <el-input-number :min="1" :max="10" v-model="form.num"></el-input-number>-->
        <!--          <el-button type="primary" style="background-color: #f59300; border-color: #f59300; margin-left: 10px" >立即购买</el-button>-->
        <!--        </div>-->
      </div>
    </div>

    <div style="margin: 50px 0">
      <div style="margin-bottom: 20px; font-size: 22px; font-weight: bold; text-align: center">食谱安排</div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周一食谱：</div>
        <div v-html="plan.mon"></div>
        <div class="divider"></div>
      </div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周二食谱：</div>
        <div v-html="plan.tues"></div>
        <div class="divider"></div>
      </div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周三食谱：</div>
        <div v-html="plan.wed"></div>
        <div class="divider"></div>
      </div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周四食谱：</div>
        <div v-html="plan.thur"></div>
        <div class="divider"></div>
      </div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周五食谱：</div>
        <div v-html="plan.fri"></div>
        <div class="divider"></div>
      </div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周六食谱：</div>
        <div v-html="plan.sat"></div>
        <div class="divider"></div>
      </div>
      <div class="w-e-text">
        <div style="margin-bottom: 20px; font-size: 16px; font-weight: bold; text-align: left">周日食谱：</div>
        <div v-html="plan.sun"></div>
        <div class="divider"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "planDetail",
  data() {
    return {
      id: this.$route.query.id,
      plan: {},
      form: { num: 1 }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get('/plan/selectById/' + this.id).then(res => {
        this.plan = res.data || {}
      })
    }
  }
}
</script>

<style scoped>

.divider {
  border-top: 1px solid #ccc; /* 分割线样式 */
  margin: 10px 0; /* 外边距 */
}

</style>