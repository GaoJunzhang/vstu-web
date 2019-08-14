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
                        <oss-choose text="选择预览图" @on-change="handleSelectProImg"
                                         ref="oss"></oss-choose>
                        <div class="upload-list" v-for="item in selectProImg">
                            <img :src="item.fileName" onerror="this.src='https://s2.ax1x.com/2019/07/19/ZviZM4.jpg'">
                        </div>
                    </FormItem>
                    <FormItem label="预览视频" prop="proVideo">
                        <!--<Input v-model="form.proVideo" style="width: 400px"/>-->
                        <oss-choose text="选择预览视频" @on-change="handleSelectProVideo"
                                         ref="oss"></oss-choose>
                        <div class="upload-list" v-for="item in selectProVideo">
                            <img :src="item.fileName" onerror="this.src='https://s2.ax1x.com/2019/07/19/ZviZM4.jpg'">
                        </div>
                    </FormItem>
                    <FormItem label="资源文件" prop="rul">
                        <!--<Input v-model="form.url" style="width: 400px"/>-->
                        <oss-choose text="选择文件" @on-change="handleSelectFile" ref="oss"></oss-choose>
                        <div class="upload-list" v-for="item in selectFile">
                            <img :src="item.fileName" onerror="this.src='https://s2.ax1x.com/2019/07/19/ZviZM4.jpg'">
                        </div>
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
    import ossChoose from "@/views/my-components/vstu/oss-choose";
    import {addresource} from "@/api/index"

    export default {
        name: "add",
        components: {
            ossChoose
        },
        data() {
            return {
                loading: true, // 表单加载状态
                submitLoading: false, // 表单提交状态
                selectProImg: [],
                selectProVideo: [],
                selectFile: [],
                form: {
                    name: "",
                    title: "",
                    url: "",
                    proImg: "",
                    proVideo: "",
                    content: "",
                },
                // 表单验证规则
                formValidate: {
                    name: [{required: true, message: "不能为空", trigger: "blur"}],
                    title: [{required: true, message: "不能为空", trigger: "blur"}],
                    content: [{required: true, message: "不能为空", trigger: "blur"}],
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
                        addresource(this.form).then(res => {
                            if (res.success == true) {
                                this.$Message.success("添加成功")
                                this.submited();
                            }
                            this.submitLoading = false;
                        })
                    }
                });
            },
            close() {
                this.$emit("close", true);
            },
            submited() {
                this.$emit("submited", true);
            },
            handleSelectProImg(v) {
                this.selectProImg = v;
                if (v.length > 0) {
                    this.form.proImg = v[0].fileName
                } else {
                    this.form.proImg = ""
                }
            },
            handleSelectProVideo(v) {
                this.selectProVideo = v;
                if (v.length > 0) {
                    this.form.proVideo = v[0].fileName
                } else {
                    this.form.proVideo = ""
                }
            },
            handleSelectFile(v) {
                this.selectFile = v;
                if (v.length > 0) {
                    this.form.url = v[0].fileName
                } else {
                    this.form.url = ""
                }
            }
        },
        mounted() {
            this.init();
        }
    };
</script>
<style>
    .upload-list {
        margin-top: 10px;
        display: inline-block;
        width: 60px;
        height: 60px;
        text-align: center;
        line-height: 60px;
        border: 1px solid transparent;
        border-radius: 4px;
        overflow: hidden;
        background: #fff;
        position: relative;
        box-shadow: 0 1px 1px rgba(0, 0, 0, .2);
        margin-right: 4px;
    }

    .upload-list img {
        width: 100%;
        height: 100%;
    }

    .upload-list:hover {
        display: block;
    }
</style>