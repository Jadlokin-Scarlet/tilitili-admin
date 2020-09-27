/*
应用中所有接口请求模块
    |- 每个接口返回的都是promise对象
 */

import ajax from "./ajax"

const BASE = '/api'

// 登录
export const reqLogin = (username, password) => ajax(BASE + '/Login', { username, password },"POST")
export const reqLoginOut = () => ajax(BASE + '/logout')

//  获取资源列表
export const getResources = (data) => ajax(BASE + '/getResources', data)

// 获取菜单权限
export const getAdminUserRoleMenuList = () => ajax(BASE + '/getAdminUserRoleMenuList',{})

export const getTrafficCard = () => ajax(BASE+'/traffic/getTrafficCard')
export const getUserTraffic = (dayBegin,dayEnd) => ajax(BASE+'/traffic/getUserTraffic',{dayBegin,dayEnd})
export const getTrafficLineCard = (dayBegin,dayEnd) => ajax(BASE+'/traffic/getTrafficLineCard', {dayBegin,dayEnd})
