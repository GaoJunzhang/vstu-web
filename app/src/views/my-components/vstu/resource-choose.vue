<template>
    <div>
        <Button @click="resourceModalVisible=true" :icon="icon">{{text}}</Button>
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
        <Drawer title="选择资源" closable v-model="resourceModalVisible" width="800" draggable>
            <Form
                    ref="searchResourceForm"
                    :model="searchResourceForm"
                    inline
                    :label-width="65"
                    class="search-form"
            >
                <Form-item label="资源名称" prop="fileName">
                    <Input
                            type="text"
                            v-model="searchResourceForm.name"
                            clearable
                            placeholder="请输资源名称"
                            style="width: 220px"
                    />
                </Form-item>
                <!--                <Form-item label="类型" prop="department">
                                    <Select
                                            v-model="searchResourceForm.fileType"
                                            placeholder="请选择"
                                            clearable
                                            style="width: 200px"
                                    >
                                        <Option value="img/">图片</Option>
                                        <Option value="video/">视频</Option>
                                        <Option value="apk/">应用</Option>
                                    </Select>
                                </Form-item>-->
                <Form-item style="margin-left:-35px;" class="br">
                    <Button @click="handleSearchResource" type="primary" icon="ios-search">搜索</Button>
                    <Button @click="handleResetResource">重置</Button>
                </Form-item>
            </Form>
            <Table :loading="ossLoading" border :columns="resourceColumns" :data="data" :height="height"
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
                <Button @click="resourceModalVisible=false" type="primary" style="margin-left:10px">关闭</Button>
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
    import {resourceData} from "@/api/index"
    import '@/locale/global'

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
                resourceModalVisible: false,
                selectResource: [],
                videoUrl: '',
                imgUrl: '',
                visible: false,
                showvideo: false,
                searchResourceForm: {
                    name: "",
                    title: "",
                    content: "",
                    pageNumber: 1,
                    pageSize: 10,
                    sort: "create_time",
                    order: "desc",
                    startDate: "",
                    endDate: ""
                },
                resourceColumns: [
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
                        sortable: true
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
                        sortable: true,
                        render: (h, params) => {
                            console.log("===============")
                            console.log(params)
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
                        sortable: true,
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
                        width: 130,
                        sortable: true
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
                                                let tmpKey = params.row.id
                                                console.log(params)
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
                data: [],
                totalResource: 0,
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
                            this.searchResourceForm.departmentId = v;
                        },*/
            changeUserPage(v) {
                this.searchResourceForm.pageNumber = v;
                this.getResourceDataList();
            },
            changeUserPageSize(v) {
                this.searchResourceForm.pageSize = v;
                this.getResourceDataList();
            },
            getResourceDataList() {
                this.ossLoading = true;
                resourceData(this.searchResourceForm).then(res => {
                    if (res.success == true) {
                        this.data = res.result.records;
                        this.totalResource = res.result.total;
                    }
                });
                this.ossLoading = false;
            },
            handleSearchResource() {
                this.searchResourceForm.pageNumber = 1;
                this.searchResourceForm.pageSize = 9;
                this.getResourceDataList();
            },
            handleResetResource() {
                this.$refs.searchResourceForm.resetFields();
                this.searchResourceForm.pageNumber = 1;
                this.searchResourceForm.pageSize = 9;
                this.$refs.dep.clearSelect();
                this.searchResourceForm.departmentId = "";
                // 重新加载数据
                this.getResourceDataList();
            },
            /*            setData(v) {
                            this.selectResource = v;
                            this.$emit("on-change", this.selectResource);
                        },*/
            chooseSelect(v) {
                console.log(v)
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
                        fileName: v.name
                    };
                    this.selectResource.push(u);
                    this.$emit("on-change", this.selectResource);
                    this.$Message.success(`添加成功`);
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
            this.getResourceDataList();
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

