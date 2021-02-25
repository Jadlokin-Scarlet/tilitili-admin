/*
应用中所有接口请求模块
    |- 每个接口返回的都是promise对象
 */

import {del, get, patch, post} from "./ajax"


let BASE_URL = 'http://localhost:8083/api'
if (process.env.NODE_ENV !== "development") {
    BASE_URL = 'http://api.admin.tilitili.club'
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
export const updateStartTime = data => patch(`/video/info/startTime`, data)
export const updateExternalOwner = data => patch(`/video/info/externalOwner`, data)
export const updateIsCopyWarning = data => patch(`/video/info/isCopyWarning`, data)

//视频数据
export const getVideoDataByCondition = (data) => get('/video/data', data)
export const reRank = (data) => patch('/video/data/rank', data)

//视频数据文件
export const downloadDataTxtUrl = () => (BASE_URL + `/video/data/file`)
export const listVideoDataTxt = (data) => get(`/video/data/adminFile`, data)

//自定义爬取
export const getTaskByCondition = (data) => get('/task', data)
export const spiderVideo = (data) => post('/task', data)
export const updateTaskStatus = (data) => patch('/task', data)

//批量爬取
export const getBatchTaskByCondition = (data) => get('/batchTask', data);
export const testBatchSpiderVideo = (data) => post('/batchTask/testBatchSpiderVideo', data)
export const batchSpiderAllVideo = (data) => post('/batchTask/batchSpiderAllVideo', data)
export const batchSpiderAllVideoTag = (data) => post('/batchTask/batchSpiderAllVideoTag', data)
export const deleteBatchTag = (id) => del(`/batchTask/${id}`)

//标签管理
export const getTagByCondition = (data) => get('/tag', data)
export const updateTag = (data) => patch('/tag', data)

//视频标签管理
export const getVideoTagByCondition = (data) => get('/video/tag', data)

//视频下发资源管理
export const getFlag = (data) => get('/resources/adminFlag', data)
export const updateFlag = (data) => patch('/resources/flag', data);

//作者管理
export const getOwnerByCondition = (data) => get('/owner', data)

//视频信息数据
export const getVideoInfoCount = (data) => get('/video/info/count', data);

//视频tag数据
export const getTagCount = (data) => get('/video/tag/count', data);

//视频数据统计
export const getVideoDataCount = (data) => get('/video/data/count', data);

//推荐池
export const getRecommendPoolByCondition = data => get('/recommend/pool', data);
export const addRecommend = data => post('/recommend', data);
export const updateDeleteRecommend = data => patch('/recommend', data);
export const useRecommend = data => patch('/recommend/status', data);

//推荐归档
export const getUseRecommendByCondition = data => get('/recommend', data);
