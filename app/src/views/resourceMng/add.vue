<style lang="less">
  @import "./resourceMng.less";
</style>
<template>
  <div>
    <Row>
      <Card>
        <div slot="title">
          <a @click="close" class="back-title">
            <Icon type="ios-arrow-back" style="margin: 0 0 2px 0"/>
            返回
          </a>
        </div>
        <Form ref="form" :model="form" :label-width="90" :rules="formValidate">
          <FormItem label="名称" prop="name">
            <Input v-model="form.name" style="width: 400px"/>
          </FormItem>
          <FormItem label="标题" prop="title">
            <Input v-model="form.title" style="width: 400px"/>
          </FormItem>
          <FormItem label="描述" prop="content">
            <Input v-model="form.content" style="width: 400px"/>
          </FormItem>
          <FormItem label="预览图" prop="proImg">
            <!--<Input v-model="form.proImg" style="width: 400px"/>-->
            <resource-choose text="选择预览图" @on-change="handleSelectResource" ref="user"></resource-choose>
          </FormItem>
          <FormItem label="预览视频" prop="proVideo">
            <!--<Input v-model="form.proVideo" style="width: 400px"/>-->
            <resource-choose text="选择预览视频" @on-change="handleSelectResource" ref="user"></resource-choose>
          </FormItem>
          <FormItem label="资源文件" prop="rul">
            <!--<Input v-model="form.url" style="width: 400px"/>-->
            <resource-choose text="选择文件" @on-change="handleSelectResource" ref="user"></resource-choose>
          </FormItem>

          <Form-item>
            <Button
                    @click="handleSubmit"
                    :loading="submitLoading"
                    type="primary"
                    style="margin-right:5px"
            >提交并保存
            </Button>
            <Button @click="handleReset">重置</Button>
          </Form-item>
        </Form>
      </Card>
    </Row>
  </div>
</template>

<script>
    import resourceChoose from "@/views/my-components/vstu/resource-choose";
    export default {
        name: "add",
        components: {
            resourceChoose
        },
        data() {
            return {
                loading: true, // 表单加载状态
                submitLoading: false, // 表单提交状态
                selectResource:[],
                form: {
                    id: "",
                    name: "",
                    title: "",
                    url: "",
                    proImg: "",
                    proVideo: "",
                    content: "",
                },
                // 表单验证规则
                formValidate: {
                    name: [{required: true, message: "不能为空", trigger: "blur"}]
                }
            };
        },
        methods: {
            init() {
            },
            handleReset() {
                this.$refs.form.resetFields();
            },
            handleSubmit() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        // this.postRequest("请求路径", this.form).then(res => {
                        //   this.submitLoading = false;
                        //   if (res.success == true) {
                        //     this.$Message.success("添加成功");
                        //     this.submited();
                        //   }
                        // });
                        // 模拟成功
                        this.submitLoading = false;
                        this.$Message.success("添加成功");
                        this.submited();
                    }
                });
            },
            close() {
                this.$emit("close", true);
            },
            submited() {
                this.$emit("submited", true);
            },
            handleSelectResource(){
                this.selectResource = v;
            }
        },
        mounted() {
            this.init();
        }
    };
</script>