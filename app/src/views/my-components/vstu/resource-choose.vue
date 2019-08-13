<template>
    <div>
        <Button @click="userModalVisible=true" :icon="icon">{{text}}</Button>
        <span @click="clearSelectData" class="clear">清空已选</span>
        <Collapse simple class="collapse">
            <Panel name="1">
                已选择
                <span class="select-count">{{selectResource.length}}</span>
                <p slot="content">
                    <Tag
                            v-for="(item, i) in selectResource"
                            :key="i"
                            :name="item.id"
                            color="default"
                            closable
                            @on-close="handleCancelUser"
                    >{{item.fileName}}
                    </Tag>
                </p>
            </Panel>
        </Collapse>
        <Drawer title="选择云盘文件" closable v-model="userModalVisible" width="800" draggable>
            <Form
                    ref="searchResourceForm"
                    :model="searchResourceForm"
                    inline
                    :label-width="65"
                    class="search-form"
            >
                <Form-item label="文件名称" prop="fileName">
                    <Input
                            type="text"
                            v-model="searchResourceForm.fileName"
                            clearable
                            placeholder="请输入文件名称"
                            style="width: 220px"
                    />
                </Form-item>
                <Form-item label="类型" prop="department">
                    <Select
                            v-model="searchResourceForm.fileType"
                            placeholder="请选择"
                            clearable
                            style="width: 200px"
                    >
                        <Option value="0">图片</Option>
                        <Option value="1">应用</Option>
                    </Select>
                </Form-item>
                 <Form-item style="margin-left:-35px;" class="br">
                     <Button @click="handleSearchResource" type="primary" icon="ios-search">搜索</Button>
                     <Button @click="handleResetResource">重置</Button>
                 </Form-item>
            </Form>
            <Table :loading="ossLoading" border :columns="userColumns" :data="ossData" :height="height"
                   ref="userTable"></Table>
            <Row type="flex" justify="end" class="code-row-bg page" style="margin: 10px 0;">
                <Page
                        :current="searchResourceForm.pageNumber"
                        :total="totalResource"
                        :page-size="searchResourceForm.pageSize"
                        @on-change="changeUserPage"
                        @on-page-size-change="changeUserPageSize"
                        :page-size-opts="[10,20,50]"
                        size="small"
                        show-total
                        show-elevator
                        show-sizer
                ></Page>
            </Row>
            <div class="my-drawer-footer">
                已选择
                <span class="select-count">{{selectResource.length}}</span>
                <Button @click="clearSelectData" style="margin-left:10px">清空已选</Button>
                <Button @click="userModalVisible=false" type="primary" style="margin-left:10px">关闭</Button>
            </div>
        </Drawer>
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
    import {getUserListData, pageOssData} from "@/api/index";
    import '../../../locale/global'

    export default {
        name: "resourceChoose",
        props: {
            text: {
                type: String,
                default: "选择资源"
            },
            icon: {
                type: String,
                default: "md-camera"
            }
        },
        data() {
            return {
                height: 500,
                ossLoading: true,
                userModalVisible: false,
                selectResource: [],
                videoUrl: '',
                imgUrl: '',
                visible: false,
                showvideo: false,
                searchResourceForm: {
                    fileName: "",
                    type: "",
                    status: "",
                    pageNumber: 1, // 当前页数
                    pageSize: 10, // 页面大小
                    sort: "createTime", // 默认排序字段
                    order: "desc" // 默认排序方式
                },
                userColumns: [
                    // 表头
                    {
                        type: "index",
                        width: 60,
                        align: "center"
                    },
                    {
                        title: "缩略图",
                        key: "key",
                        width: 100,
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
                            return h('img', {
                                attrs: {
                                    src: fileTmp,
                                    onerror: 'this.src="' + global.ERRORIMG_URL + '"'
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
                        key: "key",
                        width: 200
                    },
                    {
                        title: "文件大小",
                        key: "size",
                        width: 100,
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
                        key: "lastModified",
                        width: 150
                    },
                    {
                        title: "操作",
                        key: "action",
                        align: "center",
                        width: 100,
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
                                )
                            ]);
                        }
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
                                            type: "info",
                                            size: "small"
                                        },
                                        on: {
                                            click: () => {
                                                this.chooseSelect(params.row);
                                            }
                                        }
                                    },
                                    "添加"
                                )
                            ]);
                        }
                    }
                ],
                ossData: [],
                totalResource: 0,
                nextMarker: '',//oss下一次分页起点
                maxKeys: 10,//oss每页页数,
                dir: ''//oss指定目录
            };
        },
        methods: {
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
            handleSelectDep(v) {
                this.searchResourceForm.departmentId = v;
            },
            changeUserPage(v) {
                this.searchResourceForm.pageNumber = v;
                this.getOssDataList();
            },
            changeUserPageSize(v) {
                this.searchResourceForm.pageSize = v;
                this.getOssDataList();
            },
            getOssDataList() {
                this.ossLoading = true;
                let params = {
                    nextMarker: this.nextMarker,//oss下一次分页起点
                    maxKeys: this.maxKeys,//oss每页页数
                    dir: this.dir//oss指定目录
                };
                pageOssData(params).then(res => {
                    if (res.success) {
                        this.nextMarker = res.result.nextMarker
                        this.ossData = res.result.summaryList
                        this.totalResource = res.result.total
                    }
                })
                this.ossLoading = false;
            },
            handleSearchResource() {
                this.searchResourceForm.pageNumber = 1;
                this.searchResourceForm.pageSize = 9;
                this.getOssDataList();
            },
            handleResetResource() {
                this.$refs.searchResourceForm.resetFields();
                this.searchResourceForm.pageNumber = 1;
                this.searchResourceForm.pageSize = 9;
                this.$refs.dep.clearSelect();
                this.searchResourceForm.departmentId = "";
                // 重新加载数据
                this.getOssDataList();
            },
            setData(v) {
                this.selectResource = v;
                this.$emit("on-change", this.selectResource);
            },
            chooseSelect(v) {
                // 去重
                let that = this;
                let flag = true;
                this.selectResource.forEach(e => {
                    if (v.id == e.id) {
                        that.$Message.warning("已经添加过啦，请勿重复选择");
                        flag = false;
                    }
                });
                if (flag) {
                    let u = {
                        id: v.id,
                        fileName: v.fileName
                    };
                    this.selectResource.push(u);
                    this.$emit("on-change", this.selectResource);
                    this.$Message.success(`添加资源 ${v.fileName} 成功`);
                }
            },
            clearSelectData() {
                this.selectResource = [];
                this.$emit("on-change", this.selectResource);
            },
            handleCancelUser(e, id) {
                // 删除所选资源
                let newArray = [];
                this.selectResource.forEach(e => {
                    if (id != e.id) {
                        newArray.push(e);
                    }
                });
                this.selectResource = newArray;
                this.$emit("on-change", this.selectResource);
                this.$Message.success("删除所选资源成功");
            }
        },
        created() {
            // 计算高度
            this.height = Number(document.documentElement.clientHeight - 230);
            this.getOssDataList();
        }

    };
</script>

<style lang="less">
    .clear {
        font-size: 12px;
        margin-left: 15px;
        color: #40a9ff;
        cursor: pointer;
    }

    .collapse {
        font-size: 12px;
        margin-top: 15px;
        width: 500px;
    }

    .my-drawer-footer {
        z-index: 10;
        width: 100%;
        position: absolute;
        bottom: 0;
        left: 0;
        border-top: 1px solid #e8e8e8;
        padding: 10px 16px;
        text-align: right;
        background: #fff;
    }

    .select-count {
        font-size: 13px;
        font-weight: 600;
        color: #40a9ff;
    }
</style>

