<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item>
                    <i class="el-icon-lx-cascades"></i> 微习惯 ：简单到不可能失败的任务——微目标 !
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
                <el-select size="medium" v-model="query.state" placeholder="状态" class="handle-select mr10">
                    <el-option key="1" label="" value="" v-show="false"></el-option>
                    <el-option key="2" label="进行中" value="0"></el-option>
                    <el-option key="3" label="完成" value="1"></el-option>
                </el-select>
                <el-input size="medium" v-model="query.taskName" placeholder="任务名" class="handle-input mr10"></el-input>
                <el-button size="medium" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            </div>
            <el-table :data="tinyHabitData" border class="table" ref="tinyHabitData"
                header-cell-class-name="table-header" @selection-change="handleSelectionChange"
                :row-class-name="forgetCurveStyle" :max-height="400">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="taskName" label="任务名称" sortable align="center"></el-table-column>
                <el-table-column prop="description" label="描述" align="center"></el-table-column>

                <el-table-column label="状态" width="100" align="center">
                    <template #default="scope">
                        <el-tag :type="
                                scope.row.punchCardState === 2 
                                    ? 'success'
                                    : scope.row.punchCardState === 1
                                    ? 'danger'
                                    : ''
                            ">{{ scope.row.punchCardStateText }}</el-tag>
                    </template>
                </el-table-column>

                <el-table-column label="操作" width="260" align="center">
                    <template #default="scope">
                        <el-button v-if="scope.row.punchCardState === 2" type="text" icon="el-icon-coin"
                            @click="executeCount(scope.row.id)">
                            打卡+1
                        </el-button>
                        <el-button v-if="scope.row.punchCardState === 0" type="text" icon="el-icon-coin"
                            @click="punchCard(scope.row.id)">
                            开始打卡
                        </el-button>
                        <el-button v-if="scope.row.punchCardState === 1" type="text" icon="el-icon-coin"
                            @click="punchCard(scope.row.id)">
                            完成打卡
                        </el-button>
                        <el-button type="text" icon="el-icon-s-order" class="tiny-item" @click="getTinyHabitLog(scope.row.id)">
                            查看日志
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
            <el-form ref="forgetForm" :model="forgetForm" label-width="70px" class="forget-form">
                <el-form-item label="id" v-show="false">
                    <el-input v-model="forgetForm.id"></el-input>
                </el-form-item>
                <el-form-item label="任务名称">
                    <el-input v-model="forgetForm.taskName"></el-input>
                </el-form-item>
                <el-form-item label="描述">
                    <el-input type="textarea" rows="5" v-model="forgetForm.description"></el-input>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button size="medium" @click="editVisible = false">取 消</el-button>
                    <el-button size="medium" type="primary" @click="saveEdit('forgetForm')">确 定</el-button>
                </span>
            </template>
        </el-dialog>

        <!-- 弹出任务明细 -->
        <el-dialog title="任务日志" :before-close="handleClose" v-model="forgetItemVisible" width="70%">
            <el-table :data="tinyHabitLogData" border :row-class-name="tableRowClassName" height="460"
                class="forget-form">
                <el-table-column label="状态" align="center">
                    <template #default="scope">
                        <el-tag :type="
                                scope.row.punchCardState === 2 
                                    ? 'success'
                                    : scope.row.punchCardState === 1
                                    ? 'danger'
                                    : ''
                            ">{{ scope.row.punchCardStateText }}</el-tag>
                    </template>
                </el-table-column>
                <el-table-column property="startTime" label="开始时间" align="center"></el-table-column>
                <el-table-column property="endTime" label="结束时间" align="center"></el-table-column>
                <el-table-column label="学习时间" width="100" align="center">
                    <template #default="scope">
                        {{scope.row.timeInterval}} {{scope.row.unitName}}
                    </template>
                </el-table-column>
                <el-table-column property="executeCount" label="学习次数" align="center"></el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background layout="total, prev, pager, next" :current-page="pageIndex"
                    :page-size="pageSize" :total="logPageTotal" @current-change="handlePageChange"></el-pagination>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import * as forgetApi from "../api/index";
    export default {
        name: "tinyHabit",
        data() {
            return {
                query: {
                    state: "",
                    target: "",
                    pageIndex: 1,
                    pageSize: 5
                },
                tinyHabitData: [],
                tinyHabitLogData: [],
                multipleSelection: [],
                delList: [],
                editVisible: false,
                forgetItemVisible: false,
                editTitle: '新增',
                pageTotal: 0,
                forgetForm: {
                    'state': 1
                },
                idx: -1,
                id: -1,
                pageIndex: 1,
                pageSize: 5,
                logPageTotal: 0
            };
        },
        created() {
            this.getTinyHabitData();
        },
        methods: {
            // 获取 easy-mock 的模拟数据
            getTinyHabitData() {
                this.tinyHabitData = [];
                forgetApi.getTinyHabitData('1', '10', this.query).then(res => {
                    console.log(res);
                    this.pageTotal = res.data.pageTotal;
                    for (var i = 0; i < res.data.data.length; i++) {
                        var obj = res.data.data[i];
                        this.tinyHabitData.push(obj);
                    }
                });

            },
            // 触发搜索按钮
            handleSearch() {
                // localStorage.removeItem("ms_username");
                // window.location.href = "/";
                this.getTinyHabitData();
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
                        forgetApi.delTinyHabit(ids).then(res => {
                            this.$message.success(res.msg);
                            this.getTinyHabitData();
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
                        forgetApi.delTinyHabit(ids).then(res => {
                            this.$message.success(res.msg);
                            that.getTinyHabitData();
                        });
                        that.multipleSelection = [];
                    })
                    .catch(() => {});
            },
            handleSave() {
                this.editVisible = true;
                this.forgetForm = {
                    'state': 1
                };
            },
            // 编辑操作
            handleEdit() {
                const length = this.multipleSelection.length;
                if (length != 1) {
                    this.$message.error(`请选择一条要编辑的数据`);
                    return;
                }
                this.editTitle = '编辑';
                this.forgetForm = {
                    ...this.multipleSelection[0]
                };
                this.editVisible = true;
            },
            // 保存编辑
            saveEdit(formName) {
                if (this.forgetForm.id === undefined) {
                    forgetApi.addTinyHabit(this.forgetForm).then(res => {
                        this.$message.success(res.msg);
                        this.$refs[formName].resetFields();
                        this.editVisible = false;
                        this.getTinyHabitData();
                    });
                } else {
                    forgetApi.editTinyHabit(this.forgetForm).then(res => {
                        this.$message.success(res.msg);
                        this.$refs[formName].resetFields();
                        this.editVisible = false;
                        this.getTinyHabitData();
                    });
                }

            },
            // 分页导航
            handlePageChange(val) {
                this.query.pageIndex = val;
                this.getTinyHabitData();
            }, // 日志分页导航
            handleLogPageChange(val) {
                this.pageIndex = val;
                this.getTinyHabitData();
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
            punchCard(id) {

                forgetApi.punchCard(id).then(res => {
                    this.$message.success(res.msg);
                    this.getTinyHabitData();
                });

            },
            executeCount(id) {

                forgetApi.executeCount(id).then(res => {
                    this.$message.success(res.msg);
                    this.getTinyHabitData();
                });
            },
            getTinyHabitLog(id) {
                this.forgetItemVisible = true;
                forgetApi.getTinyHabitLogData(this.pageIndex, this.pageSize, id).then((res) => {
                    this.tinyHabitLogData = res.data.data;
                })
            },
            tableRowClassName({
                row
            }) {
                if (row.status === 1) {
                    return 'success-row';
                } else if (row.status === 2) {
                    return 'warning-row';
                }
                return '';
            },
            forgetCurveStyle({
                row
            }) {
                if (row.status === 1) {
                    return 'warning-row';
                }
                return '';
            },
            handleClose() {
                this.forgetItemVisible = false;
                this.getTinyHabitData();
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
</style>
