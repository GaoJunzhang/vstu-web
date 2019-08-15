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
                <div style="background:#eee;padding: 20px">
                    <Card :bordered="false">
                        <Form ref="form" :model="form" :label-width="90" :rules="formValidate" inline>
                            <FormItem label="是否永久" prop="isForever">
                                <Select v-model="form.isForever" style="width:200px" placeholder="请选择">
                                    <Option v-for="item in statusList" :value="item.value" :key="item.value">{{item.label }}</Option>
                                </Select>
                            </FormItem>
                            <Form-item label="有效期" prop="validDate">
                                <DatePicker
                                        v-model="selectDate"
                                        type="daterange"
                                        format="yyyy-MM-dd"
                                        clearable
                                        @on-change="selectDateRange"
                                        placeholder="选择有效期"
                                        style="width: 200px"
                                ></DatePicker>
                            </Form-item>
                            <Form-item>
                                <Button
                                        @click="handleSubmit"
                                        :loading="submitLoading"
                                        type="primary"
                                        style="margin-right:5px"
                                >保存
                                </Button>
                            </Form-item>
                        </Form>
                    </Card>
                </div>
                <div class="demo-split" :height="height">
                    <div class="demo-split">
                        <Split v-model="split1">
                            <div slot="left" class="demo-split-pane">
                                <userobj-choose text="选择用户" @on-change="handleSelectUserObj"
                                                ref="userObj"></userobj-choose>
                                <Table :loading="userLoading" border :columns="userColumns" :data="userData"
                                       ref="userTable"></Table>
                            </div>
                            <div slot="right" class="demo-split-pane">
                                <resource-choose text="选择资源" @on-change="handleSelectFile"
                                                 ref="resource"></resource-choose>
                                <Table :loading="Rloading" border :columns="rColumns" :data="rData"
                                       ref="resourceTable"></Table>
                            </div>
                        </Split>
                    </div>
                </div>
            </Card>
        </Row>
    </div>
</template>

<script>
    import resourceChoose from "@/views/my-components/vstu/resource-choose";
    import userobjChoose from "@/views/my-components/vstu/userobj-choose";
    import {addUResource} from "@/api/index"
    export default {
        name: "allot",
        props: {
            userList: Array
        },
        components: {
            resourceChoose,
            userobjChoose
        },
        data() {
            return {
                submitLoading: false, // 表单提交状态
                selectDate: null,
                split1: 0.5,
                height: 600,
                statusList: [
                    {
                        value: '0',
                        label: '是'
                    },
                    {
                        value: '1',
                        label: '否'
                    },
                ],
                form: {
                    isForever: "",
                    startDate: "",
                    endDate: ""
                },
                // 表单验证规则
                formValidate: {
                    isForever: [{required: true, message: "请选择", trigger: "change"}],
                },
                userColumns: [
                    {
                        type: "index",
                        width: 60,
                        align: "center"
                    },
                    {
                        title: "用户名",
                        key: "username",
                        width: 100,
                    },
                    {
                        title: "头像",
                        key: "avatar",
                        align: "center",
                        width: 145,
                        render: (h, params) => {
                            return h("Avatar", {
                                props: {
                                    src: params.row.avatar
                                }
                            });
                        }
                    },
                    {
                        title: "手机",
                        key: "mobile",
                    },
                    {
                        title: "用户类型",
                        key: "type",
                        align: "center",
                        render: (h, params) => {
                            let re = "";
                            if (params.row.type == 1) {
                                re = "管理员";
                            } else if (params.row.type == 0) {
                                re = "普通用户";
                            }
                            return h("div", re);
                        }
                    },
                    {
                        title: "创建时间",
                        key: "createTime",
                    },
                    {
                        title: "操作",
                        key: "action",
                        width: 100,
                        align: "center",
                        fixed: "right",
                        render: (h, params) => {
                            return h("div", [
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "error",
                                            size: "small"
                                        },
                                        on: {
                                            click: () => {

                                                this.removeUser(params.row);
                                            }
                                        }
                                    },
                                    "删除"
                                )
                            ]);
                        }
                    }
                ],
                userData: [],
                rData: [],
                userLoading: true,
                Rloading: true,
                rColumns: [
                    // 表头
                    {
                        type: "index",
                        width: 50,
                        align: "center"
                    },
                    {
                        title: "资源名称",
                        key: "name",
                        width: 120,
                    },
                    {
                        title: "标题",
                        width: 120,
                        key: "title",
                        sortable: true
                    },
                    {
                        title: "预览图",
                        key: "proImg",
                        width: 130,
                        render: (h, params) => {
                            return h('img', {
                                attrs: {
                                    src: params.row.proImg,
                                    onerror: 'this.src="' + global.ERRORIMG_URL + '"'
                                },
                                on: {
                                    click: () => {
                                        this.previewFile(1, params.row.proImg)

                                    }
                                },
                                style: {
                                    'margin-top': '10px',
                                    'margin-bottom': '10px',
                                    'border-radius': '4px',
                                    width: '80px',
                                    height: '80px',
                                    cursor: 'pointer'
                                }
                            });
                        }
                    },
                    {
                        title: "预览视频",
                        key: "proVideo",
                        width: 120,
                        render: (h, params) => {
                            return h('img', {
                                attrs: {
                                    src: global.VIDEOIMG_URL,
                                    onerror: 'this.src="' + global.ERRORIMG_URL + '"'
                                },
                                on: {
                                    click: () => {
                                        this.previewFile(2, params.row.proVideo)

                                    }
                                },
                                style: {
                                    'margin-top': '10px',
                                    'margin-bottom': '10px',
                                    'border-radius': '4px',
                                    width: '80px',
                                    height: '80px',
                                    cursor: 'pointer'
                                }
                            });
                        }
                    },
                    {
                        title: "资源内容",
                        key: "content",
                    },
                    {
                        title: "操作",
                        key: "action",
                        width: 100,
                        align: "center",
                        fixed: "right",
                        render: (h, params) => {
                            return h("div", [
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "error",
                                            size: "small"
                                        },
                                        on: {
                                            click: () => {

                                                this.removeResource(params.row);
                                            }
                                        }
                                    },
                                    "删除"
                                )
                            ]);
                        }
                    }
                ]
            }
        },
        methods: {
            init() {
                this.userData = this.userList
                this.userLoading = false
                this.Rloading = false
            },
            handleReset() {
                this.$refs.form.resetFields();
            },
            handleSubmit() {
                if (this.form.isForever == 1 && this.form.startDate == "") {
                    this.$Message.error("请选择有效期")
                    return
                }
                if (this.userData.length<=0){
                    this.$Message.error("请选择用户")
                    return
                }
                if (this.rData.length<=0){
                    this.$Message.error("请选择资源")
                    return
                }
                this.$refs.form.validate(valid => {
                    if (valid) {
                        var uIds = [];
                        var rIds = [];
                        this.userData.forEach(function (v, k) {
                            uIds.push(v.id)
                        })
                        this.rData.forEach(function (v, k) {
                            rIds.push(v.id)
                        })
                        this.form.uIds = uIds
                        this.form.rIds = rIds
                        this.submitLoading = true
                        console.log(this.form)
                        addUResource(this.form).then(res => {
                            if (res.success == true) {
                                this.$Message.success("添加成功")
                                this.submited();
                            }
                        })
                    }
                    this.submitLoading = false
                });
            },
            close() {
                this.$emit("close", true);
            },
            submited() {
                this.$emit("submited", true);
            },
            selectDateRange(v) {
                if (v) {
                    this.form.startDate = v[0];
                    this.form.endDate = v[1];
                }
            },
            handleSelectFile(v) {

                this.selectResource = v
                this.rData = this.selectResource
            },
            handleSelectUserObj(v) {
                this.userData = v
            },
            removeResource(v) {
                for (var i = 0; i < this.rData.length; i++) {
                    if (this.rData[i].id == v.id) {//item.id==122
                        this.rData.splice(i, 1)
                    }
                }
                this.selectResource = this.rData
            },
            removeUser(v) {
                for (var i = 0; i < this.userData.length; i++) {
                    if (this.userData[i].id == v.id) {//item.id==122
                        this.userData.splice(i, 1)
                    }
                }
            }
        },
        mounted() {
            this.height = Number(document.documentElement.clientHeight - 230);
            this.init();
        }
    }
</script>

<style>
    .demo-split {
        height: 700px;
        border: 1px solid #dcdee2;
    }

    .demo-split-pane {
        padding: 10px;
    }

    .demo-split-pane.no-padding {
        height: 300px;
        padding: 0;
    }
</style>