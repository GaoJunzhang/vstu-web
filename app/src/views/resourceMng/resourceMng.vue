<style lang="less">
    @import "./resourceMng.less";
</style>
<template>
    <div class="search">
        <add v-if="currView=='add'" @close="currView='index'" @submited="submited"/>
        <edit v-if="currView=='edit'" @close="currView='index'" @submited="submited" :id="id"/>
        <Card v-show="currView=='index'">
            <Row>
                <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
                    <Form-item label="资源名称" prop="name">
                        <Input
                                type="text"
                                v-model="searchForm.name"
                                clearable
                                placeholder="请输入设备名称"
                                style="width: 200px"
                        />
                    </Form-item>
                    <Form-item label="资源标题" prop="title">
                        <Input
                                type="text"
                                v-model="searchForm.title"
                                clearable
                                placeholder="请输入设备编号"
                                style="width: 200px"
                        />
                    </Form-item>
                    <Form-item label="资源描述" prop="content">
                        <Input
                                type="text"
                                v-model="searchForm.content"
                                clearable
                                placeholder="请输入设备编号"
                                style="width: 200px"
                        />
                    </Form-item>
                    <span v-if="drop">
                                <Form-item label="创建时间">
                                  <DatePicker
                                          v-model="selectDate"
                                          type="daterange"
                                          format="yyyy-MM-dd"
                                          clearable
                                          @on-change="selectDateRange"
                                          placeholder="选择起始时间"
                                          style="width: 200px"
                                  ></DatePicker>
                                </Form-item>
                              </span>
                    <Form-item style="margin-left:-35px;" class="br">
                        <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
                        <Button @click="handleReset">重置</Button>
                        <a class="drop-down" @click="dropDown">
                            {{dropDownContent}}
                            <Icon :type="dropDownIcon"></Icon>
                        </a>
                    </Form-item>
                </Form>
            </Row>
            <Row class="operation">
                <Button @click="add" type="primary" icon="md-add">添加资源</Button>
                <Button @click="delAll" icon="md-trash">批量删除</Button>
                <Button @click="getDataList" icon="md-refresh">刷新</Button>
            </Row>
            <Row>
                <Alert show-icon>
                    已选择
                    <span class="select-count">{{selectCount}}</span> 项
                    <a class="select-clear" @click="clearSelectAll">清空</a>
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
    import {
        resourceData,
        resourceById,
        delResourceIds,
        addresource
    } from "@/api/index"
    import '@/locale/global'
    import add from "./add.vue";
    import edit from "./edit.vue";

    export default {
        name: "single-window",
        components: {
            add,
            edit
        },
        data() {
            return {
                dropDownContent: "展开",
                dropDownIcon: "ios-arrow-down",
                selectDate: null,
                drop: false,
                imgUrl: '',
                visible: false,
                showvideo: false,
                videoUrl: '',
                searchForm: {
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


                id: "",
                currView: "index",
                loading: true, // 表单加载状态
                sortColumn: "createTime", // 排序字段
                sortType: "desc", // 排序方式
                submitLoading: false, // 添加或编辑提交状态
                selectList: [], // 多选数据
                selectCount: 0, // 多选计数
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
                        title: "资源名称",
                        key: "name",
                        sortable: true
                    },
                    {
                        title: "标题",
                        key: "title",
                        sortable: true
                    },
                    {
                        title: "预览图",
                        key: "proImg",
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
                    }, {
                        title: "地址",
                        key: "url",
                        sortable: true
                    },
                    {
                        title: "资源内容",
                        key: "content",
                        sortable: true
                    },
                    {
                        title: "创建时间",
                        key: "createTime",
                        sortable: true,
                        sortType: "desc"
                    },
                    {
                        title: "操作",
                        key: "action",
                        align: "center",
                        render: (h, params) => {
                            return h("div", [
                                h(
                                    "Button",
                                    {
                                        props: {
                                            type: "primary",
                                            size: "small",
                                            icon: "ios-create-outline"
                                        },
                                        style: {
                                            marginRight: "5px"
                                        },
                                        on: {
                                            click: () => {
                                                this.edit(params.row);
                                            }
                                        }
                                    },
                                    "编辑"
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
                                                this.remove(params.row);
                                            }
                                        }
                                    },
                                    "删除"
                                )
                            ]);
                        }
                    }
                ],
                data: [], // 表单数据
                pageNumber: 1, // 当前页数
                pageSize: 10, // 页面大小
                total: 0 // 表单数据总数
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
            init() {
                this.getDataList();
            },
            submited() {
                this.currView = "index";
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
                console.log(this.searchForm)
                resourceData(this.searchForm).then(res => {
                    this.loading = false;
                    if (res.success == true) {
                        this.data = res.result.records;
                        this.total = res.result.total;
                    }
                });
            },
            handleCancel() {
                this.modalVisible = false;
            },
            add() {
                this.currView = "add";
            },
            edit(v) {
                if (!v.id) {
                    this.$Message.error("id不能为空");
                    return;
                }
                this.currView = "edit";
                this.id = v.id;
            },
            remove(v) {
                this.$Modal.confirm({
                    title: "确认删除",
                    // 记得确认修改此处
                    content: "您确认要删除 " + v.name + " ?",
                    onOk: () => {
                        delResourceIds(v.id).then(res => {
                            if (res.success == true) {
                                this.$Message.success("操作成功");
                                this.getDataList();
                            } else {
                                this.$Message.error("操作失败")
                            }
                        })
                    }
                });
            },
            clearSelectAll() {
                this.$refs.table.selectAll(false);
            },
            changeSelect(e) {
                this.selectList = e;
                this.selectCount = e.length;
            },
            delAll() {
                if (this.selectCount <= 0) {
                    this.$Message.warning("您还未选择要删除的数据");
                    return;
                }
                this.$Modal.confirm({
                    title: "确认删除",
                    content: "您确认要删除所选的 " + this.selectCount + " 条数据?",
                    onOk: () => {
                        let ids = "";
                        this.selectList.forEach(function (e) {
                            ids += e.id + ",";
                        });
                        ids = ids.substring(0, ids.length - 1);
                        delResourceIds(ids).then(res => {
                            if (res.success == true) {
                                this.$Message.success("操作成功");
                                this.clearSelectAll();
                                this.getDataList();
                            } else {
                                this.$Message.error("操作失败")
                            }
                        })

                    }
                });
            },
            handleSearch() {
                this.searchForm.pageNumber = 1;
                this.searchForm.pageSize = 10;
                this.getDataList();
            },
            handleReset() {
                this.$refs.searchForm.resetFields();
                this.searchForm.pageNumber = 1;
                this.searchForm.pageSize = 10;
                this.selectDate = null;
                this.searchForm.startDate = "";
                this.searchForm.endDate = "";
                this.selectDep = [];
                this.searchForm.departmentId = "";
                // 重新加载数据
                this.getDataList();
            },
            dropDown() {
                if (this.drop) {
                    this.dropDownContent = "展开";
                    this.dropDownIcon = "ios-arrow-down";
                } else {
                    this.dropDownContent = "收起";
                    this.dropDownIcon = "ios-arrow-up";
                }
                this.drop = !this.drop;
            },
            selectDateRange(v) {
                if (v) {
                    this.searchForm.startDate = v[0];
                    this.searchForm.endDate = v[1];
                }
            },
        },
        mounted() {
            this.init();
        }
    };
</script>