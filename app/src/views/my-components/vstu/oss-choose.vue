<template>
    <div>
        <Button @click="ossModalVisible=true" :icon="icon">{{text}}</Button>
        <span @click="clearSelectData" class="clear">清空已选</span>
        <Collapse simple class="collapse">
            <Panel name="1">
                已选择
                <span class="select-count">{{selectOss.length}}</span>
                <p slot="content">
                    <Tag
                            v-for="(item, i) in selectOss"
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
        <Drawer title="选择云盘文件" closable v-model="ossModalVisible" width="800" draggable>
            <Form
                    ref="searchOssForm"
                    :model="searchOssForm"
                    inline
                    :label-width="65"
                    class="search-form"
            >
                <!--<Form-item label="文件名称" prop="fileName">
                    <Input
                            type="text"
                            v-model="searchOssForm.fileName"
                            clearable
                            placeholder="请输入文件名称"
                            style="width: 220px"
                    />
                </Form-item>-->
                <Form-item label="类型" prop="department">
                    <Select
                            v-model="searchOssForm.fileType"
                            placeholder="请选择"
                            clearable
                            style="width: 200px"
                    >
                        <Option value="img/">图片</Option>
                        <Option value="video/">视频</Option>
                        <Option value="apk/">应用</Option>
                    </Select>
                </Form-item>
                 <Form-item style="margin-left:-35px;" class="br">
                     <Button @click="handleSearchOss" type="primary" icon="ios-search">搜索</Button>
                     <Button @click="handleResetOss">重置</Button>
                 </Form-item>
            </Form>
            <Table :loading="ossLoading" border :columns="userColumns" :data="ossData" :height="height"
                   ref="userTable"></Table>
            <Row type="flex" justify="end" class="code-row-bg page" style="margin: 10px 0;">
                <Page
                        :current="searchOssForm.pageNumber"
                        :total="totalOss"
                        :page-size="searchOssForm.pageSize"
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
                <span class="select-count">{{selectOss.length}}</span>
                <Button @click="clearSelectData" style="margin-left:10px">清空已选</Button>
                <Button @click="ossModalVisible=false" type="primary" style="margin-left:10px">关闭</Button>
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
            <iframe :src="videoUrl" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"
                    style="width:100%;height:550px;"></iframe>
        </Modal>
    </div>
</template>

<script>
    import {getUserListData, pageOssData} from "@/api/index";
    import '../../../locale/global'

    export default {
        name: "ossChoose",
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
                ossModalVisible: false,
                selectOss: [],
                videoUrl: '',
                imgUrl: '',
                visible: false,
                showvideo: false,
                searchOssForm: {
                    fileType: '',
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
                            }else if(global.EXESTR_URL.indexOf(fileType) != -1){
                                fileTmp = global.EXEIMG_URL
                                flage = 4
                            } else {
                                fileTmp = global.FILEIMG_URL
                                flage = 5
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
                                                let tmpKey = params.row.key
                                                console.log(params)

                                                if (tmpKey.charAt(tmpKey.length-1)=='/'){
                                                    this.$Message.error("文件夹不可添加")
                                                }else {
                                                    this.chooseSelect(params.row);
                                                }
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
                totalOss: 0,
                nextMarker: '',//oss下一次分页起点
                maxKeys: 10,//oss每页页数,
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
/*            handleSelectDep(v) {
                this.searchOssForm.departmentId = v;
            },*/
            changeUserPage(v) {
                this.searchOssForm.pageNumber = v;
                this.getOssDataList();
            },
            changeUserPageSize(v) {
                this.searchOssForm.pageSize = v;
                this.getOssDataList();
            },
            getOssDataList() {
                this.ossLoading = true;
                let params = {
                    nextMarker: this.nextMarker,//oss下一次分页起点
                    maxKeys: this.maxKeys,//oss每页页数
                    dir: this.searchOssForm.fileType,//oss指定目录,
                    keyPrefix:this.searchOssForm.fileName
                };
                pageOssData(params).then(res => {
                    if (res.success) {
                        this.nextMarker = res.result.nextMarker
                        this.ossData = res.result.summaryList
                        this.totalOss = res.result.total
                    }
                })
                this.ossLoading = false;
            },
            handleSearchOss() {
                this.searchOssForm.pageNumber = 1;
                this.searchOssForm.pageSize = 9;
                this.getOssDataList();
            },
            handleResetOss() {
                this.$refs.searchOssForm.resetFields();
                this.searchOssForm.pageNumber = 1;
                this.searchOssForm.pageSize = 9;
                this.$refs.dep.clearSelect();
                this.searchOssForm.departmentId = "";
                // 重新加载数据
                this.getOssDataList();
            },
/*            setData(v) {
                this.selectOss = v;
                this.$emit("on-change", this.selectOss);
            },*/
            chooseSelect(v) {
                console.log(v)
                // 去重
                let that = this;
                let flag = true;
                this.selectOss.forEach(e => {
                    if (v._index == e.id) {
                        that.$Message.warning("已经添加过啦，请勿重复选择");
                        flag = false;
                    }
                });
                if (flag) {
                    let u = {
                        id: v._index,
                        fileName: global.OSS_URL+v.key
                    };
                    this.selectOss.push(u);
                    this.$emit("on-change", this.selectOss);
                    this.$Message.success(`添加成功`);
                }
            },
            clearSelectData() {
                this.selectOss = [];
                this.$emit("on-change", this.selectOss);
            },
            handleCancelUser(e, id) {
                // 删除所选资源
                let newArray = [];
                this.selectOss.forEach(e => {
                    if (id != e.id) {
                        newArray.push(e);
                    }
                });
                this.selectOss = newArray;
                this.$emit("on-change", this.selectOss);
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

