<style lang="less">
    @import "../../xboot-vue-template/simple-table/simpleTable.less";
</style>
<template>
    <div class="search">
        <Row>
            <Col>
                <Card>
                    <Row class="operation">
                        <!--<Button @click="add" type="primary" icon="md-add">添加</Button>-->
                        <Button @click="delAll" icon="md-trash">批量删除</Button>
                        <Button @click="getDataList" icon="md-refresh">刷新</Button>
                    </Row>
                    <Row>
                        <Alert show-icon>
                            已选择
                            <span class="select-count">{{selectCount}}</span> 项
                            <a class="select-clear" @click="clearSelectAll">清空</a> 这里还可以做一些数据统计显示
                        </Alert>
                    </Row>
                    <Row>
                        <Table
                                :loading="loading"
                                border
                                :columns="columns"
                                :data="data"
                                ref="table"
                                sortable="custom"
                                @on-sort-change="changeSort"
                                @on-selection-change="changeSelect"
                        ></Table>
                    </Row>
                    <Row type="flex" justify="end" class="page">
                        <Page
                                :current="pageNumber"
                                :total="total"
                                :page-size="pageSize"
                                @on-change="changePage"
                                @on-page-size-change="changePageSize"
                                :page-size-opts="[10,20,50]"
                                size="small"
                                show-total
                                show-elevator
                                show-sizer
                        ></Page>
                    </Row>
                </Card>
            </Col>
        </Row>
        <!--        <Modal :title="modalTitle" v-model="modalVisible" :mask-closable="false" :width="500">
                    <Form ref="form" :model="form" :label-width="80" :rules="formValidate">
                        <FormItem label="名称" prop="name">
                            <Input v-model="form.name"/>
                        </FormItem>
                    </Form>
                    <div slot="footer">
                        <Button type="text" @click="handleCancel">取消</Button>
                        <Button type="primary" :loading="submitLoading" @click="handleSubmit">提交</Button>
                    </div>
                </Modal>-->
        <Modal title="查看大图" v-model="visible" class-name="fl-image-modal">
            <img :src="imgUrl" v-if="visible" style="width: 100%">
        </Modal>
        <Modal
                v-model="showvideo"
                title="预览视频"
                :styles="{top: '30px'}"
                footer-hide
                width="1000"
        >
            <!--<iframe src="//player.bilibili.com/player.html?aid=30284667&cid=52827707&page=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="width:100%;height:550px;"> </iframe>-->
            <iframe :src="videoUrl" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"
                    style="width:100%;height:550px;"></iframe>
        </Modal>
    </div>
</template>

<script>
    import {ossData, deleteOss, pageOssData, deltetOssObj} from "@/api/index";
    import '../../../locale/global'

    export default {
        name: "simple-table",
        data() {
            return {
                videoUrl: '',
                showvideo: false,
                loading: true, // 表单加载状态
                // sortColumn: "createTime", // 排序字段
                // sortType: "desc", // 排序方式
                modalType: 0, // 添加或编辑标识
                modalVisible: false, // 添加或编辑显示
                modalTitle: "", // 添加或编辑标题
                form: {
                    // 添加或编辑表单对象初始化数据
                    name: "",
                    createTime: ""
                },
                // 表单验证规则
                formValidate: {
                    name: [{required: true, message: "不能为空", trigger: "blur"}]
                },
                submitLoading: false, // 添加或编辑提交状态
                selectList: [], // 多选数据
                selectCount: 0, // 多选计数
                visible: false,
                imgUrl: '',
                columns: [
                    // 表头
                    {
                        type: "selection",
                        width: 60,
                        align: "center"
                    },
                    {
                        type: "index",
                        width: 60,
                        align: "center"
                    },
                    {
                        title: "缩略图",
                        key: "key",
                        render: (h, params) => {
                            var fileTmp = params.row.key;
                            var fileType = fileTmp.substr(fileTmp.lastIndexOf(".")).toLowerCase()
                            let flage = 0;
                            let videoUrl = ''
                            if (global.IMGSTR_URL.indexOf(fileType) != -1) {
                                fileTmp = global.OSS_URL + params.row.key
                                videoUrl = fileTmp
                                flage = 1
                            } else if (global.VIDEOSTR_URL.indexOf(fileType) != -1) {
                                fileTmp = global.VIDEOIMG_URL
                                videoUrl = global.OSS_URL + params.row.key
                                flage = 2
                            } else if (".apk".indexOf(fileType) != -1) {
                                fileTmp = global.APKIMG_URL
                                flage = 3
                            } else {
                                fileTmp = global.FILEIMG_URL
                                flage = 4
                            }
                            console.log(fileTmp)
                            console.log(fileTmp)
                            return h('img', {
                                attrs: {
                                    src: fileTmp,
                                    onerror: 'this.src="'+global.ERRORIMG_URL+'"'
                                },
                                on: {
                                    click: () => {
                                        this.previewFile(flage, videoUrl)

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
                        title: "名称",
                        key: "key"
                    },
                    {
                        title: "文件大小",
                        key: "size",
                        render: (h, params) => {
                            let size = 0;
                            if (params.row.size >= 1048576) {
                                size = params.row.size / 1048576
                                return h('div', [
                                    h('strong', parseFloat(size).toFixed(2) + "MB")
                                ])
                            } else {
                                size = params.row.size / 1024
                                return h('div', [
                                    h('strong', parseFloat(size).toFixed(2) + "KB")
                                ])
                            }

                        }
                    },
                    {
                        title: "更新时间",
                        key: "lastModified"
                    },
                    {
                        title: "操作",
                        key: "action",
                        align: "center",
                        render: (h, params) => {
                            var fileTmp = params.row.key;
                            var fileType = fileTmp.substr(fileTmp.lastIndexOf(".")).toLowerCase()
                            let flage = 0;
                            let videoUrl = ''
                            if (global.IMGSTR_URL.indexOf(fileType) != -1) {
                                videoUrl = global.OSS_URL + params.row.key
                                flage = 1
                            } else if (global.VIDEOSTR_URL.indexOf(fileType) != -1) {
                                videoUrl = global.OSS_URL + params.row.key
                                flage = 2
                            }
                            return h("div", [
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "primary",
                                            size: "small",
                                            icon: "md-arrow-dropright-circle",
                                            ghost: true
                                        },
                                        style: {
                                            marginRight: "5px"
                                        },
                                        on: {
                                            click: () => {
                                                this.previewFile(flage, videoUrl);
                                            }
                                        }
                                    },
                                    "预览"
                                ),
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "error",
                                            size: "small",
                                            icon: "md-trash"
                                        },
                                        on: {
                                            click: () => {
                                                if (this.isFile(params.row.key)){
                                                    this.$Message.warning("文件夹不可删除")
                                                    return
                                                }
                                                this.remove(params.row);
                                            }
                                        },
                                        style: {
                                            marginRight: "5px"
                                        },
                                    },
                                    "删除"
                                ),
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "info",
                                            size: "small",
                                            icon: "md-archive"
                                        },
                                        on: {
                                            click: () => {
                                                this.downLoadOss(params.row)
                                            }
                                        }
                                    },
                                    "下载"
                                )
                            ]);
                        }
                    }
                ],
                data: [], // 表单数据
                pageNumber: 1, // 当前页数
                pageSize: 10, // 页面大小
                total: 0, // 表单数据总数
                nextMarker: '',//oss下一次分页起点
                maxKeys: 10,//oss每页页数
                dir: ''//oss指定目录
            };
        },
        methods: {
            init() {
                this.getDataList();
            },
            changePage(v) {
                this.pageNumber = v;
                this.getDataList();
                this.clearSelectAll();
            },
            changePageSize(v) {
                this.pageSize = v;
                this.getDataList();
            },
            changeSort(e) {
                this.sortColumn = e.key;
                this.sortType = e.order;
                if (e.order == "normal") {
                    this.sortType = "";
                }
                this.getDataList();
            },
            getDataList() {
                this.loading = true;
                let params = {
                    nextMarker: this.nextMarker,//oss下一次分页起点
                    maxKeys: this.maxKeys,//oss每页页数
                    dir: this.dir//oss指定目录
                };
                pageOssData(params).then(res => {
                    if (res.success) {
                        this.nextMarker = res.result.nextMarker
                        this.data = res.result.summaryList
                        this.total = res.result.total
                    }
                })
                this.loading = false;
            },
            handleCancel() {
                this.modalVisible = false;
            },
            handleSubmit() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.submitLoading = true;
                        if (this.modalType == 0) {
                            // 添加 避免编辑后传入id等数据 记得删除
                            delete this.form.id;
                            // this.postRequest("请求地址", this.form).then(res => {
                            //   this.submitLoading = false;
                            //   if (res.success == true) {
                            //     this.$Message.success("操作成功");
                            //     this.getDataList();
                            //     this.modalVisible = false;
                            //   }
                            // });
                            // 模拟请求成功
                            this.submitLoading = false;
                            this.$Message.success("操作成功");
                            this.getDataList();
                            this.modalVisible = false;
                        } else {
                            // 编辑
                            // this.postRequest("请求地址", this.form).then(res => {
                            //   this.submitLoading = false;
                            //   if (res.success == true) {
                            //     this.$Message.success("操作成功");
                            //     this.getDataList();
                            //     this.modalVisible = false;
                            //   }
                            // });
                            // 模拟请求成功
                            this.submitLoading = false;
                            this.$Message.success("操作成功");
                            this.getDataList();
                            this.modalVisible = false;
                        }
                    }
                });
            },
            remove(v) {
                this.$Modal.confirm({
                    title: "确认删除",
                    // 记得确认修改此处
                    content: "您确认要删除 " + v.key + " ?",
                    onOk: () => {
                        // 删除
                        // this.deleteRequest("请求地址，如/deleteByIds/" + v.id).then(res => {
                        //   if (res.success == true) {
                        //     this.$Message.success("操作成功");
                        //     this.getDataList();
                        //   }
                        // });
                        // 模拟请求成功

                        deltetOssObj({key: v.key}).then(res => {
                            if (res.success == true) {

                                this.$Message.success("操作成功");
                                this.getDataList();
                            }
                        })
                    }
                });
            },
            downLoadOss(v) {
                // downloadFile({key:v.key})
                window.location.href = 'http://127.0.0.1:8888/zboot/common/oss/downloadFile?key=' + v.key
            },
            clearSelectAll() {
                this.$refs.table.selectAll(false);
            },
            changeSelect(e) {
                this.selectList = e;
                this.selectCount = e.length;
            },
            delAll() {
                let that = this
                if (this.selectCount <= 0) {
                    this.$Message.warning("您还未选择要删除的数据");
                    return;
                }
                var keys = new Array();
                this.selectList.forEach(function (e) {
                    if (that.isFile(e.key)){
                        that.$Notice.info({
                            title:"提醒",
                            desc: '选项中包涵文件夹，其中文件夹不删除',
                            duration:10000
                        })
                    }else {
                        keys.push(e.key)
                    }
                });
                this.$Modal.confirm({
                    title: "确认删除",
                    content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
                    onOk: () => {

                        deleteOss({keyStrs: keys}).then(res => {
                            if (res.success) {
                                // 模拟请求成功
                                this.$Message.success("操作成功");
                                this.clearSelectAll();
                                this.getDataList();
                            } else {
                                this.$Message.success("删除失败");
                            }
                        })

                    }
                });
            },
            previewFile(flage, url) {
                if (flage == 1) {
                    this.imgUrl = url
                    this.visible = true
                }
                if (flage == 2) {
                    this.videoUrl = url
                    this.showvideo = true
                }
            },
            isFile(fileName){
                fileName = fileName.substr(fileName.length - 1, 1)
                if (fileName == "/"){
                    return true
                }
                return false
            }
        },
        mounted() {
            this.init();
        }
    };
</script>