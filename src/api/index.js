/*
应用中所有接口请求模块
    |- 每个接口返回的都是promise对象
 */

import {del, get, patch, post} from "./ajax"


// 登录
export const isLogin = () => get('/admin/isLogin')
export const reqLogin = (userName, password) => post('/admin/login', { userName, password })
export const reqLoginOut = () => post('/admin/loginOut')

// 获取资源列表
export const getResources = (data) => get('/resources', data)

//视频信息
export const getVideoInfoByCondition = (data) => get('/video/info', data)
export const deleteVideo = (av) => del(`/video/info/${av}/isDelete/true`)
export const recoveryVideo = (av) => patch(`/video/info/${av}/isDelete/false`)

//视频数据
export const getVideoDataByCondition = (data) => get('/video/data', data)
export const reRank = (data) => patch('/video/data/rank', data)