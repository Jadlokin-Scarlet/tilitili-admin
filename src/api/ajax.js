/*
发送异步ajax请求函数模块
    |- 封装axios库
    |- 函数返回promise对象
 */

import axios from 'axios'
// import {hashHistory} from "react-router-dom"


import {message} from "antd"
import  Qs from 'qs';
import store from "../store";

// 响应拦截
axios.interceptors.response.use((response) => {
    if(response.data.message === '请重新登录' || response.data.message === '用户未登录'){
        store.delUser()
        //未登录返回到登录页面
        // hashHistory.push('/Login')
    }
    return Promise.resolve(response.data)
}, (err) => {
    return Promise.reject(err)
})

export default function ajax(url,data={},type='GET',option) {
    return new Promise((resolve,reject)=>{
        const config = {
            url,
            method: type,
            params: data,
        }
        if (option === 'download') {
            Object.assign(config, {
                responseType: 'blob',
                paramsSerializer: params => {
                    return Qs.stringify(params,{ arrayFormat: 'repeat' })
                }
            })
        } else if (option === 'JSON') {
            Object.assign(config, {
                headers:{"Content-Type": "application/json"},
            })
        } else if (option === 'image') {
            Object.assign(config, {
                headers:{"Content-Type": "multipart/form-data"},
            })
        } else if (type === 'GET') {
            Object.assign(config, {
                headers:{"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                paramsSerializer: params => {
                    return Qs.stringify(params,{ arrayFormat: 'repeat' })
                }
            })
        }
        axios(config).then(response=>{
            resolve(response)
        }).catch(error=>{
            message.error("请求失败:"+error.message);
            reject(error);
        })
    })

}
