import http from '../utils/request';

const domain = 'http://localhost:9990';
// const domain = 'http://120.78.67.40:9990';

export const login = (username, password) => {
    return http.requestQuickGet(domain + "/yuman/account/login?username=" + username + "&password=" + password)
}

// ================记忆任务 开始==================
export const generateTarget = (param) => {
    return http.requestPost(domain + "/yuman/forgetCurve/generateTarget", param)
}

export const edit = (params) => {
    return http.requestPost(domain + "/yuman/forgetCurve/edit", params)
}

export const del = (ids) => {
    return http.requestPost(domain + "/yuman/forgetCurve/del", ids)
}

export const getForgetData = (page, size, params) => {
    return http.requestPost(domain + "/yuman/forgetCurve/list", params);
}

export const getForgetItemData = (id) => {
    return http.requestQuickGet(domain + "/yuman/forgetCurve/listItem?id=" + id)
}

export const finish = (id) => {
    return http.requestQuickGet(domain + "/yuman/forgetCurve/finish?id=" + id)
}

// ================记忆任务 结束==================

// ================微习惯 开始==================

//查询
export const getTinyHabitData = (page, size, params) => {
    return http.requestPost(domain + "/yuman/tinyHabit/list", params);
}

//新增
export const addTinyHabit = (param) => {
    return http.requestPost(domain + "/yuman/tinyHabit/add", param)
}

//编辑
export const editTinyHabit = (params) => {
    return http.requestPost(domain + "/yuman/tinyHabit/edit", params)
}

//删除
export const delTinyHabit = (ids) => {
    return http.requestPost(domain + "/yuman/tinyHabit/del", ids);
}

//查询任务日志
export const getTinyHabitLogData = (pageIndex, pageSize, id) => {
    return http.requestQuickGet(domain + "/yuman/tinyHabit/listLog?id=" + id + "&pageIndex=" + pageIndex +
        "&pageSize=" + pageSize);
}

//打卡
export const punchCard = (id) => {
    return http.requestQuickGet(domain + "/yuman/tinyHabit/punchCard?id=" + id)
}

//打卡 +1
export const executeCount = (id) => {
    return http.requestQuickGet(domain + "/yuman/tinyHabit/executeCount?id=" + id)
}

// ================微习惯 结束==================

// ================学习时长 开始==================

//查询
export const getLearnTimeData = (page, size, params) => {
    return http.requestPost(domain + "/yuman/learnTime/list", params);
}

//新增
export const addLearnTime = (param) => {
    return http.requestPost(domain + "/yuman/learnTime/add", param)
}

//编辑
export const editLearnTime = (params) => {
    return http.requestPost(domain + "/yuman/learnTime/edit", params)
}

//删除
export const delLearnTime = (ids) => {
    return http.requestPost(domain + "/yuman/learnTime/del", ids);
}