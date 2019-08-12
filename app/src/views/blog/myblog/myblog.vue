<style lang="less">
    @import "./myblog.less";
</style>
<template>
    <div class="layout">
        <layout>
            <!--      <Header>
                      <Row  type="flex">
                          <Col span="22"><Input v-model="articleTitle" size="large" placeholder="标题" /></Col>
                          <Col span="2" >
                              <Button type="primary" :loading="loading" icon="ios-checkmark-circle" @click="saveHtml">
                                  <span v-if="!loading">保存</span>
                                  <span v-else>Loading...</span>
                              </Button>
                          </Col>
                      </Row>
                  </Header>-->
            <Content>
                <Row type="flex" :gutter="16">
                    <Col span="22"><Input v-model="articleTitle" size="large" placeholder="标题"/></Col>
                    <Col span="2">
                        <Button type="primary" :loading="loading" icon="ios-checkmark-circle" @click="saveHtml">
                            <span v-if="!loading">保存</span>
                            <span v-else>Loading...</span>
                        </Button>
                        <Button type="primary" :loading="loading" icon="ios-checkmark-circle" @click="flushall">
                            <span v-if="!loading">清空</span>
                            <span v-else>Loading...</span>
                        </Button>
                    </Col>
                </Row>
                <Row type="flex" :gutter="16">
                    <Col span="10"><Input v-model="imgDesc" size="large" placeholder="图述"/></Col>
                    <Col span="10"><Input v-model="wedge" size="large" placeholder="楔子"/></Col>
                    <Select v-model="Season" style="width:200px" prefix="ios-home">
                        <Option v-for="item in cityList" :value="item.value" :key="item.value"
                                @click.native="chioce(item)">{{ item.label }}
                        </Option>
                    </Select>
                </Row>
                <Row type="flex">
                    <Col span="24">
                        <div ref="imageU" class="demo-upload-list" v-for="item in uploadList">
                            <img :src="item">
                            <div class="demo-upload-list-cover">
                                <Icon type="ios-eye-outline" @click.native="handleView(item)"></Icon>
                                <Icon type="ios-trash-outline" @click.native="handleRemove(item)"></Icon>
                            </div>
                        </div>
                        <!--                    <Modal title="View Image" v-model="visible">
                                                <img :src="showImgUrl"  style="width: 100%">
                                            </Modal>-->
                    </Col>
                </Row>
                <Divider>正文</Divider>
                <div class="edit_container">
                    <!--<Upload
                            :show-upload-list="false"
                            :on-success="handleSuccess"
                            :format="['jpg','jpeg','png','gif']"
                            :max-size="5120"
                            multiple
                            :headers="accessToken"
                            :action="uploadFileUrl"
                    >
                        <Button icon="ios-cloud-upload-outline"></Button>
                    </Upload>-->
                    <quill-editor
                            style="height: 700px;"
                            v-model="content"
                            ref="myQuillEditor"
                            :options="editorOption"
                            @blur="onEditorBlur($event)" @focus="onEditorFocus($event)"
                            @change="onEditorChange($event)">
                    </quill-editor>

                </div>
            </Content>

        </layout>
        <Modal
                v-model="visible"
                title="预览视频"
                :styles="{top: '30px'}"
                footer-hide
                width="1000"
        >
            <!--<iframe src="//player.bilibili.com/player.html?aid=30284667&cid=52827707&page=1" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="width:100%;height:550px;"> </iframe>-->
            <iframe :src="showImgUrl" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"
                    style="width:100%;height:550px;"></iframe>
        </Modal>
    </div>

</template>

<script>
    import {
        uploadFile,
        addArticle,
        deleteArticle,
        articlesLikeTitle,
        articleByUid,
        articleById,
        updateArticle,
        flushallArticle
    } from "@/api/index"
    import {Quill, quillEditor} from 'vue-quill-editor'
    import 'quill/dist/quill.core.css'
    import 'quill/dist/quill.snow.css'
    import 'quill/dist/quill.bubble.css'
    import {ImageExtend, QuillWatch} from 'quill-image-extend-module'
    import imageResize from 'quill-image-resize-module'

    Quill.register('modules/ImageExtend', ImageExtend)
    Quill.register('modules/imageResize', imageResize)


    var toolbarOptions = [
        ['bold', 'italic', 'underline', 'strike'],    //加粗，斜体，下划线，删除线
        ['blockquote', 'code-block'],     //引用，代码块


        [{'header': 1}, {'header': 2}],        // 标题，键值对的形式；1、2表示字体大小
        [{'list': 'ordered'}, {'list': 'bullet'}],     //列表
        [{'script': 'sub'}, {'script': 'super'}],   // 上下标
        [{'indent': '-1'}, {'indent': '+1'}],     // 缩进
        [{'direction': 'rtl'}],             // 文本方向


        [{'size': ['small', false, 'large', 'huge']}], // 字体大小
        [{'header': [1, 2, 3, 4, 5, 6, false]}],     //几级标题


        [{'color': []}, {'background': []}],     // 字体颜色，字体背景颜色
        [{'font': []}],     //字体
        [{'align': []}],    //对齐方式


        ['clean'],    //清除字体样式
        ['image', 'video']    //上传图片、上传视频
    ]
    export default {
        components: {quillEditor},
        data() {
            return {
                uploadList: [],
                showImgUrl: '',
                visible: false,
                category: 1,
                Season: "",
                imgDesc: '',
                wedge: '',
                cityList: [
                    {
                        value: 1,
                        label: "春"
                    },
                    {
                        value: 2,
                        label: "夏"
                    },
                    {
                        value: 3,
                        label: "秋"
                    },
                    {
                        value: 4,
                        label: "冬"
                    }
                ],
                articleTitle: '',
                quillHeight: 800,
                accessToken: {},
                uploadFileUrl: uploadFile,
                loading: false,
                content: ``,
                editorOption: {
                    modules: {
                        ImageExtend: {
                            loading: true,
                            name: 'file',
                            size: 9,
                            action: uploadFile,
                            headers: (xhr) => {
                                xhr.setRequestHeader("accessToken", this.getStore("accessToken"))
                            },
                            response: (res) => {
                                this.updateList(res.result)
                                return res.result
                            }
                        },
                        toolbar: {
                            container: toolbarOptions,
                            handlers: {
                                'image': function (value) {
                                    if (value) {

                                        // document.querySelector('.ivu-upload .ivu-btn').click()
                                        console.log("=========value========")
                                        console.log(value)
                                        QuillWatch.emit(this.quill.id)
                                    } else {
                                        this.quill.format('image', false);
                                    }
                                }
                            }
                        },
                        imageResize: {}
                    },
                    theme: 'snow'
                }
            }
        }, computed: {
            editor() {
                return this.$refs.myQuillEditor.quill;
            },
        },
        methods: {
            init() {
                this.quillHeight = document.body.clientHeight
                this.accessToken = {
                    accessToken: this.getStore("accessToken")
                };
                /*let v = JSON.parse(Cookies.get("userInfo"));
                // 转换null为""
                for (let attr in v) {
                    if (v[attr] == null) {
                        v[attr] = "";
                    }
                }*/
            },
            /*            handleSuccess(res) {
                            alert("44444444444444")
                            // 获取富文本组件实例
                            let quill = this.$refs.myQuillEditor.quill
                            // 如果上传成功
                            if (res.success == true) {
                                // 获取光标所在位置
                                alert(0)
                                let length = quill.getSelection().index;
                                // 插入图片，res为服务器返回的图片链接地址
                                quill.insertEmbed(length, 'image', res.result)
                                // 调整光标到最后
                                quill.setSelection(length + 1)
                            } else {
                                // 提示信息，需引入Message
                                Message.error('图片插入失败')
                            }
                        },*/
            onEditorReady(editor) { // 准备编辑器
            },
            onEditorBlur() {
            }, // 失去焦点事件
            onEditorFocus() {
            }, // 获得焦点事件
            onEditorChange(e) {
            }, // 内容改变事件
            saveHtml: function (event) {
                this.loading = true;
                let article = {
                    title: this.articleTitle,
                    content: this.content,
                    category: this.category,
                    wedge: this.wedge,
                    imgDesc: this.imgDesc,
                    imgs: this.uploadList,
                    // type:1,
                }
                addArticle(article).then(res => {
                    if (res.success == true) {
                        this.$Message.info("保存成功")
                    }
                })
                this.loading = false;
            },
            flushall:function(){
                flushallArticle().then(res => {
                    if (res.success == true) {
                        this.$Message.info("清空成功")
                    }
                })
            },
            chioce: function (item) {
                this.category = item.value
            },

            handleView(url) {
                this.showImgUrl = url;
                this.visible = true;
            },
            handleRemove(file) {
                console.log(file)
                /*const fileList = this.$refs.upload.fileList;
                this.$refs.upload.fileList.splice(fileList.indexOf(file), 1);*/
            },
            updateList(url) {
                this.uploadList.push(url)
            }
        },
        mounted() {
            this.init();
        }
    }
</script>
