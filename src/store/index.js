
// 使用原生的 localStorage 有些浏览器可能不支持，所以使用store库，支持所有常见浏览器，且代码更简洁
import store from 'store'

const USER_KEY="current_user"

export default {
    // 保存user
    saveUser(user){
        // localStorage.setItem(USER_KEY,JSON.stringify(user))
        store.set(USER_KEY,user)
    },
    // 读取user
    getUser(){
        // return JSON.parse(localStorage.getItem(USER_KEY)||'{}')
        return store.get(USER_KEY) || {}
    },

    // 删除user
    delUser(){
        // localStorage.removeItem(USER_KEY)
        store.remove(USER_KEY)
    }
}