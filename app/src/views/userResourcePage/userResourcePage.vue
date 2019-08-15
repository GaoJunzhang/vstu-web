<template>
    <div class="demo-split">
        <allot v-if="currView=='allot'" @close="currView='index'" @submited="submited" :userList="userList"/>
        <Split v-model="split1" v-show="currView=='index'">
            <div slot="left" class="demo-split-pane">
                <Row>
                    <Col>
                        <Card>
                            <Row>
                                <Form ref="searchForm" :model="searchForm" inline :label-width="70" class="search-form">
                                    <Form-item label="用户名称" prop="username">
                                        <Input
                                                type="text"
                                                v-model="searchForm.username"
                                                clearable
                                                placeholder="请输入用户名"
                                                style="width: 200px"
                                        />
                                    </Form-item>
                                    <Form-item label="部门" prop="department">
                                        <department-choose @on-change="handleSelectDep" style="width: 200px"
                                                           ref="dep"></department-choose>
                                    </Form-item>
                                    <span v-if="drop">
                <Form-item label="手机号" prop="mobile">
                  <Input
                          type="text"
                          v-model="searchForm.mobile"
                          clearable
                          placeholder="请输入手机号"
                          style="width: 200px"
                  />
                </Form-item>
                <Form-item label="邮箱" prop="email">
                  <Input
                          type="text"
                          v-model="searchForm.email"
                          clearable
                          placeholder="请输入邮箱"
                          style="width: 200px"
                  />
                </Form-item>
                <Form-item label="性别" prop="sex">
                  <Select v-model="searchForm.sex" placeholder="请选择" clearable style="width: 200px">
                    <Option v-for="(item, i) in dictSex" :key="i" :value="item.value">{{item.title}}</Option>
                  </Select>
                </Form-item>
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
                                        <Button @click="setUserResource" type="warning" icon="ios-settings">分配资源</Button>
                                        <Button @click="handleSearch" type="primary" icon="ios-search">搜索</Button>
                                        <Button @click="handleReset">重置</Button>
                                        <a class="drop-down" @click="dropDown">
                                            {{dropDownContent}}
                                            <Icon :type="dropDownIcon"></Icon>
                                        </a>
                                    </Form-item>
                                </Form>
                            </Row>
                            <Row>
                                <Table
                                        :loading="loading"
                                        border
                                        :columns="columns"
                                        :data="data"
                                        sortable="custom"
                                        @on-row-click="showResource"
                                        ref="table"
                                ></Table>
                            </Row>
                            <Row type="flex" justify="end" class="page">
                                <Page
                                        :current="searchForm.pageNumber"
                                        :total="total"
                                        :page-size="searchForm.pageSize"
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

            </div>
            <div slot="right" class="demo-split-pane">
                <Row>
                    <Col>
                        <Card>
                            <Row>
                                <Affix>
                                    <span class="demo-affix">点击左侧用户</span>
                                </Affix>
                            </Row>
                            <Row>
                                <Table
                                        :loading="rloading"
                                        border
                                        :columns="rcolumns"
                                        :data="rData"
                                        ref="table"
                                        sortable="custom"
                                ></Table>
                            </Row>
                        </Card>
                    </Col>
                </Row>

            </div>
        </Split>
    </div>
</template>

<script>
    import {
        getUserListData
    } from "@/api/index";
    import departmentChoose from "../my-components/xboot/department-choose";
    import allot from "../sys/user-manage/allot.vue";
    import '@/locale/global';
    export default {
        name: "userResourcePage",
        components: {
            departmentChoose,
            allot
        },
        data() {
            return {
                rloading:false,
                rData:[],
                total: 0,
                drop: false,
                selectDate: null,
                dropDownContent: "展开",
                dropDownIcon: "ios-arrow-down",
                split1: 0.5,
                loading: false,
                data: [],
                searchForm: {
                    username: "",
                    departmentId: "",
                    mobile: "",
                    email: "",
                    sex: "",
                    type: "0",
                    status: "0",
                    pageNumber: 1,
                    pageSize: 10,
                    sort: "createTime",
                    order: "desc",
                    startDate: "",
                    endDate: ""
                },
                dictSex: this.$store.state.dict.sex,
                columns: [
                    {
                        type: "index",
                        width: 60,
                        align: "center",
                        fixed: "left"
                    },
                    {
                        title: "用户名",
                        key: "username",
                        minWidth: 145,
                        sortable: true,
                        fixed: "left"
                    },
                    {
                        title: "头像",
                        key: "avatar",
                        width: 80,
                        align: "center",
                        render: (h, params) => {
                            return h("Avatar", {
                                props: {
                                    src: params.row.avatar
                                }
                            });
                        }
                    },
                    {
                        title: "所属部门",
                        key: "departmentTitle",
                        width: 80
                    },
                    {
                        title: "手机",
                        key: "mobile",
                        width: 115,
                        sortable: true
                    },
                    {
                        title: "邮箱",
                        key: "email",
                        width: 180,
                        sortable: true
                    },
                    {
                        title: "用户类型",
                        key: "type",
                        align: "center",
                        width: 100,
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
                ],
                rcolumns: [
                    // 表头
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
                                    "移除资源"
                                )
                            ]);
                        }
                    }
                ],
                currView:'index'
            }
        },
        methods: {
            init() {
                this.getUserList();
            },
            showResource(v) {
                console.log(v)
            },
            getUserList() {
                // 多条件搜索用户列表
                this.loading = true;
                // 避免后台默认值
                if (!this.searchForm.type) {
                    this.searchForm.type = "";
                }
                if (!this.searchForm.status) {
                    this.searchForm.status = "";
                }
                getUserListData(this.searchForm).then(res => {
                    this.loading = false;
                    if (res.success == true) {
                        this.data = res.result.content;
                        this.total = res.result.totalElements;
                    }
                });
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
            handleSearch() {
                this.searchForm.pageNumber = 1;
                this.searchForm.pageSize = 10;
                this.getUserList();
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
                this.getUserList();
            },
            selectDateRange(v) {
                if (v) {
                    this.searchForm.startDate = v[0];
                    this.searchForm.endDate = v[1];
                }
            },
            handleSelectDep(v) {
                this.searchForm.departmentId = v;
            },
            changePage(v) {
                this.searchForm.pageNumber = v;
                this.getUserList();
                this.clearSelectAll();
            },
            changePageSize(v) {
                this.searchForm.pageSize = v;
                this.getUserList();
            },
            setUserResource(){

                this.currView = "allot"
                this.userList = []
            },
            submited() {
                this.currView = "index";
                this.getDataList();
            },
        },
        mounted() {
            this.init();
        }
    }
</script>

<style>

</style>