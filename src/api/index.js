/*
应用中所有接口请求模块
    |- 每个接口返回的都是promise对象
 */

import {del, get, patch, post} from "./ajax"


let BASE_URL = 'http://localhost:8083/api'
if (process.env.NODE_ENV !== "development") {
    BASE_URL = 'http://47.100.66.36:8083/api'
}

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
export const updateStartTime = data => patch(`/video/info`, data)

//视频数据
export const getVideoDataByCondition = (data) => get('/video/data', data)
export const reRank = (data) => patch('/video/data/rank', data)

//视频数据文件
export const downloadDataTxtUrl = (issue) => (BASE_URL + `/video/issue/${issue}/data.txt`)
export const listVideoDataTxt = (data) => get(`/video/issue/${data.issue}/data`, data)

//自定义爬取
export const getTaskByCondition = (data) => get('/task', data)
export const spiderVideo = (data) => post('/task', data)

//批量爬取
export const getBatchTaskByCondition = (data) => get('/batchTask', data);
export const testBatchSpiderVideo = (data) => post('/batchTask/testBatchSpiderVideo', data)
export const batchSpiderAllVideo = (data) => post('/batchTask/batchSpiderAllVideo', data)
export const batchSpiderAllVideoTag = (data) => post('/batchTask/batchSpiderAllVideoTag', data)
export const deleteBatchTag = (id) => del(`/batchTask/${id}`)
