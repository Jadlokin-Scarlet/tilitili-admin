/*
发送异步ajax请求函数模块
    |- 封装axios库
    |- 函数返回promise对象
 */

import axios from 'axios'
// import {hashHistory} from "react-router-dom"


import {message} from "antd"
import  Qs from 'qs';
import {isNull} from "../utils/HtmlUtils";

let BASE = '/api'
if (process.env.NODE_ENV !== "development") {
    BASE = 'http://api.admin.tilitili.club'
}

// 响应拦截
// axios.interceptors.response.use((response) => {
//     if(response.data.message === '请重新登录' || response.data.message === '用户未登录'){
//         store.delUser()
//         //未登录返回到登录页面
//         // hashHistory.push('/Login')
//     }
//     return Promise.resolve(response.data)
// }, (err) => {
//     return Promise.reject(err)
// })

export function get(url,data={}) {
    return handleGet(axios.get(BASE + url, {
        params: data,
        headers:{"Content-Type": "application/json"},
        paramsSerializer: function(params) {
            return Qs.stringify(params, {arrayFormat: 'repeat'})
        },
    }))
}

export function post(url, data = {}) {
    return handlePost(axios.post(BASE + url, data, {
        headers:{"Content-Type": "application/json"}
    }))
}

export function del(url, data = {}) {
    return handlePost(axios.delete(BASE + url, data, {
        headers:{"Content-Type": "application/json"}
    }))
}

export function patch(url, data = {}) {
    return handlePost(axios.patch(BASE + url, data, {
        headers:{"Content-Type": "application/json"}
    }))
}
function handleGet(promise) {
    return new Promise((resolve, reject) => {
        promise.then(req => {
            if (isNull(req.data)) {
                message.error( "请求失败")
                reject(req)
            }
            const data = req.data;
            if (data.success) {
                resolve(data.data)
            } else if (data.message === "请重新登录") {
                window.location.reload();
            } else {
                message.error(isNull(data.message, "请求失败"));
                reject(req)
            }
        }).catch(reason => {
            message.error(isNull(reason.message, "请求异常2"));
            reject(reason)
        });
    })
}

function handlePost(promise) {
    return new Promise((resolve, reject) => {
        promise.then(req => {
            if (isNull(req.data)) {
                message.error( "请求异常")
                reject(req)
            }
            const data = req.data;
            if (data.success) {
                message.success(isNull(data.message, "请求成功"))
                resolve(data.data)
            } else if (data.message === "请重新登录") {
                window.location.reload();
            } else {
                message.error(isNull(data.message, "请求失败"));
                reject(req)
            }
        }).catch(reason => {
            message.error(isNull(reason.message, "请求异常"));
            reject(reason)
        });
    })
}

// export function ajax(url,data={},type='GET',option) {
//     return new Promise((resolve, reject) => {
//         const config = {
//             url: '/api' + url,
//             method: type,
//             params: data,
//         }
//         return axios(config)
//             .then(req => {
//                 if (req.success) {
//                     resolve(req.data)
//                 }else {
//                     message.error(req.message);
//                     reject(req)
//                 }
//             })
//             .catch(reason => {
//                 message.error(reason.message);
//                 reject(reason)
//             });
//     })
// }

// if (option === 'download') {
//     Object.assign(config, {
//         responseType: 'blob',
//         paramsSerializer: params => {
//             return Qs.stringify(params,{ arrayFormat: 'repeat' })
//         }
//     })
// } else if (option === 'JSON') {
//     Object.assign(config, {
//         headers:{"Content-Type": "application/json"},
//     })
// } else if (option === 'image') {
//     Object.assign(config, {
//         headers:{"Content-Type": "multipart/form-data"},
//     })
// } else if (type === 'GET') {
//     Object.assign(config, {
//         headers:{"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
//         paramsSerializer: params => {
//             return Qs.stringify(params,{ arrayFormat: 'repeat' })
//         }
//     })
// }