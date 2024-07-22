<template>
  <div style="width: 50%; margin: 20px auto">
    <div style="display: flex">
      <img :src="material.img" alt="" style="width: 50%; height: 350px; border-radius: 5px">
      <div style="padding-left: 20px">
        <div style="margin: 20px 0; font-size: 22px; font-weight: bold">名称：{{ material.name }}</div>
        <div style="margin: 20px 0;font-size: 20px;">别名：{{ material.descr }}</div>
        <div style="margin: 20px 0;font-size: 20px;">功效：{{ material.price }}</div>
        <div style="margin: 20px 0;color: red; font-size: 20px">搭配禁忌：</div>
        <div style="margin: 15px 0;font-size: 20px;">{{ material.unit }}</div>

        <div style="margin: 20px 0;font-size: 20px;">建议搭配：{{ material.address }}</div>
        <div style="margin: 20px 0;font-size: 20px;">热量：{{ material.store }}</div>
<!--        <div style="margin: 20px 0">-->
<!--          <span style="color: #666">建议搭配：<span style="color: #333">{{ material.address }}</span></span>-->
<!--          <span style="margin-left: 20px; color: #666">热量：<span style="color: #333">{{ material.store }}</span></span>-->
<!--        </div>-->
<!--        <div style="margin: 20px 0">-->
<!--          <el-input-number :min="1" :max="10" v-model="form.num"></el-input-number>-->
<!--          <el-button type="primary" style="background-color: #f59300; border-color: #f59300; margin-left: 10px" >立即购买</el-button>-->
<!--        </div>-->
      </div>
    </div>

    <div style="margin: 50px 0">
      <div style="margin-bottom: 20px; font-size: 22px; font-weight: bold; text-align: center">食材详情</div>
      <div class="w-e-text">
        <div v-html="material.content"></div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "materialDetail",
  data() {
    return {
      id: this.$route.query.id,
      material: {},
      form: { num: 1 }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.$request.get('/material/selectById/' + this.id).then(res => {
        this.material = res.data || {}
      })
    }
  }
}
</script>

<style scoped>

</style>