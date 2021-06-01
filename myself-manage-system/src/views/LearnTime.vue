<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 学习时长 ：输入的知识是营养，输出的知识才是你自己的！
                </el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-dropdown class="tags-close-box-tiny" style="margin-right: 11px;" @command="handleTags">
                    <el-button size="medium" type="primary">
                        操作菜单<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="add" icon="el-icon-document-add">新增</el-dropdown-item>
                            <el-dropdown-item command="edit" icon="el-icon-edit">编辑</el-dropdown-item>
                            <el-dropdown-item command="del" icon="el-icon-delete">批量删除</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
                <el-input size="medium" v-model="query.learnName" placeholder="学习名称" class="handle-input mr10">
                </el-input>
                <el-button size="medium" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <el-table :data="learnTimeData" border class="table" ref="learnTimeData"
                header-cell-class-name="table-header" @selection-change="handleSelectionChange" :max-height="400">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="learnName" label="学习名称" sortable align="center"></el-table-column>
                <el-table-column prop="learnTime" label="学习时长" v-show="false" align="center"></el-table-column>
                <el-table-column prop="learnTimeText" label="学习时长" align="center"></el-table-column>
                <el-table-column label="操作" width="260" align="center">
                    <template #default="scope">
                        <el-button type="text" icon="el-icon-s-order" class="tiny-item"
                            @click="learnTimeStart(scope.row)">
                            开始学习
                        </el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"
                            @click="handleDelete(scope.$index, scope.row)">
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="query.pageIndex"
                    :page-size="query.pageSize" :total="pageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog :title="editTitle" v-model="editVisible" width="30%" center>
            <el-form ref="learnTimeForm" :model="learnTimeForm" label-width="70px" class="forget-form">
                <el-form-item label="id" v-show="false">
                    <el-input v-model="learnTimeForm.id"></el-input>
                </el-form-item>
                <el-form-item label="id" v-show="false">
                    <el-input v-model="learnTimeForm.learnTime"></el-input>
                </el-form-item>
                <el-form-item label="学习名称">
                    <el-input v-model="learnTimeForm.learnName"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button size="medium" @click="editVisible = false">取 消</el-button>
                    <el-button size="medium" type="primary" @click="saveEdit('learnTimeForm')">确 定</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 计时器 -->
        <el-dialog title="计时开始" v-model="centerDialogVisible" width="38%" center>
            <div class="learn-time-div">
                <span class="learn-time-span">{{hour != 0 && (hour / 10) >= 1 ? hour : '0'+hour }}</span> <span
                    class="learn-time-colon">:</span>
                <span class="learn-time-span">{{minute != 0 && (minute / 10) >= 1 ? minute : '0'+minute }}</span> <span
                    class="learn-time-colon">:</span>
                <span class="learn-time-span">{{second != 0 && (second / 10) >= 1 ? second : '0'+ second }}</span>

            </div>
            <template #footer>
                <span class="dialog-footer">
                    <!-- <el-button @click="centerDialogVisible = false">取 消</el-button> -->
                    <el-button type="primary" @click="closeInterval">学习完成</el-button>
                </span>
            </template>

        </el-dialog>

    </div>
</template>

<script>
    import * as forgetApi from "../api/index";
    export default {
        name: "learnTime",
        data() {
            return {
                query: {
                    learnName: "",
                    pageIndex: 1,
                    pageSize: 5
                },
                learnTimeData: [],
                learnTimeForm: {},
                multipleSelection: [],
                delList: [],
                editVisible: false,
                editTitle: '新增',
                pageTotal: 0,
                idx: -1,
                id: -1,
                pageIndex: 1,
                pageSize: 10,
                centerDialogVisible: false,
                hour: 0,
                minute: 0,
                second: 0,
                time: 0,
                row : {}
            };
        },
        created() {
            this.getLearnTimeData();
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getLearnTimeData() {
                this.learnTimeData = [];
                forgetApi.getLearnTimeData('1', '10', this.query).then(res => {
                    this.pageTotal = res.data.pageTotal;
                    for (var i = 0; i < res.data.data.length; i++) {
                        var obj = res.data.data[i];
                        this.learnTimeData.push(obj);
                    }
                });

            },
            // 触发搜索按钮
            handleSearch() {
                this.getLearnTimeData();
            },
            // 删除操作
            handleDelete(index, row) {
                // 二次确认删除
                this.$confirm("确定要删除吗？", "提示", {
                        type: "warning"
                    })
                    .then(() => {
                        var ids = [];
                        ids.push(row.id)
                        forgetApi.delLearnTime(ids).then(res => {
                            this.$message.success(res.msg);
                            this.getLearnTimeData();
                        });

                    })
                    .catch(() => {});
            },
            // 多选操作
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            delAllSelection() {
                const length = this.multipleSelection.length;
                if (length == 0) {
                    this.$message.error(`请选择要删除的数据`);
                    return;
                }
                var ids = [];
                for (var i = 0; i < length; i++) {
                    ids.push(this.multipleSelection[i].id);
                }
                var that = this;
                this.$confirm("确定要删除吗？", "提示", {
                        type: "warning"
                    })
                    .then(() => {
                        forgetApi.delLearnTime(ids).then(res => {
                            this.$message.success(res.msg);
                            that.getLearnTimeData();
                        });
                        that.multipleSelection = [];
                    })
                    .catch(() => {});
            },
            handleSave() {
                this.editVisible = true;
            },
            // 编辑操作
            handleEdit() {
                const length = this.multipleSelection.length;
                if (length != 1) {
                    this.$message.error(`请选择一条要编辑的数据`);
                    return;
                }
                this.editTitle = '编辑';
                this.learnTimeForm = {
                    ...this.multipleSelection[0]
                };
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit(formName) {
                if (this.learnTimeForm.id === undefined) {
                    this.learnTimeForm.learnTime = 0;
                    forgetApi.addLearnTime(this.learnTimeForm).then(res => {
                        this.$message.success(res.msg);
                        this.$refs[formName].resetFields();
                        this.editVisible = false;
                        this.getLearnTimeData();
                    });
                } else {
                    forgetApi.editLearnTime(this.learnTimeForm).then(res => {
                        this.$message.success(res.msg);
                        this.$refs[formName].resetFields();
                        this.editVisible = false;
                        this.getLearnTimeData();
                    });
                }

            },
            // 分页导航
            handlePageChange(val) {
                this.query.pageIndex = val;
                this.getLearnTimeData();
            },
            handleTags(command) {
                if (command === 'add') {
                    this.handleSave();
                } else if (command === 'edit') {
                    this.handleEdit();
                } else if (command === 'del') {
                    this.delAllSelection();
                }
            },
            learnTimeStart(row) {
                this.row = row;
                this.centerDialogVisible = true;
                this.time = self.setInterval(this.clock, 1000);
            },
            clock() {
                this.second++;
                if (this.second === 60) {
                    this.second = 0;
                    this.minute++;
                }
                if (this.minute === 60) {
                    this.minute = 0;
                    this.hour++;
                }
                
            },
            closeInterval() {
                this.time = window.clearInterval(this.time);
                this.row.learnTime = this.row.learnTime + this.hour * 60 + this.minute
                forgetApi.editLearnTime(this.row).then(res => {
                    this.$message.success(res.msg);
                    this.getLearnTimeData();
                    this.row = {};
                    this.centerDialogVisible = false;
                    this.second = 0;
                    this.minute = 0;
                    this.hour = 0;
                });
                
            }
        }
    };
</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .red {
        color: #ff0000;
    }

    .tiny-item {
        color: #996633;
    }

    .mr10 {
        margin-right: 10px;
    }

    .table-td-thumb {
        display: block;
        margin: auto;
        width: 40px;
        height: 40px;
    }

    .tags-close-box-tiny {
        position: absolute;
        right: 0;
        top: 0;
        box-sizing: border-box;
        padding-top: 1px;
        margin-right: 15px;
        text-align: center;
        width: 110px;
        height: 30px;
        background: #fff;
        box-shadow: -3px 0 15px 3px rgba(0, 0, 0, 0.1);
        z-index: 10;
    }

    .forget-form {
        margin-right: 2%;
    }

    /deep/ .el-table .warning-row {
        background: oldlace;
    }

    /deep/ .el-table .success-row {
        background: #f0f9eb;
    }

    .tiny-log-label {
        margin-bottom: 10px;
        font-size: 16px;

    }

    .tiny-log-label span {
        margin-right: 15px;
    }

    .tiny-log-tag {
        font-size: 16px;
    }

    .learn-time-div {
        text-align: center;
        background-color: black;
        font-size: 1000%;
        color: white;
        border-radius: 10px;
        padding: 4% 3% 4% 3%;

    }

    .learn-time-span {
        background-color: white;
        color: black;
        border-radius: 10px;
        padding-left: 2%;
        padding-right: 2%;
    }

    .learn-time-colon {
        margin-right: 4%;
    }
</style>
