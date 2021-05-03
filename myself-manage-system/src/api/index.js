import http from '../utils/request';

const domain = 'http://localhost:9990';


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
