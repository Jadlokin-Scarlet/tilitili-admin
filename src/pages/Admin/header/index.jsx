import React,{Component} from "react"
import {Menu, Modal, Avatar} from "antd"

import {withRouter} from 'react-router-dom'

import './index.less'

import store from "../../../store"
import {reqLoginOut} from '../../../api'

const { confirm } = Modal

class Header extends Component{
    // 退出登录
    logout = ()=>{
        confirm({
            title: '你确定要退出登录嘛?',
            content: '',
            okText: '确认',
            cancelText: '取消',
            onOk: () =>{
                store.delUser()
                reqLoginOut();
                this.props.history.replace("/Login")
            }
        })
    }

    handleCharacterChange = (selectedCharacter) => {
        if (this.props.onCharacterChange){
            this.props.onCharacterChange(selectedCharacter)
        }
    }

    render() {
        return (
            <div className="header">
                <div className="header-icon">
                    <Avatar shape="square" src="http://img.haihu.com/012002035ef95bda0a9a409cb6549b67bdaf6a68.png" />
                    TiliTili管理后台
                </div>
                <Menu
                    mode="horizontal"
                    theme='dark'
                    className="header-menu"
                    selectedKeys={this.props.selectedCharacter}
                >
                    {
                        this.props.menuList
                            .filter(menu => this.props.menuPermissionsList.indexOf(menu.key) !== -1)
                            .map(menu => (
                                <Menu.Item key={menu.key} title={menu.title} onClick={()=>this.handleCharacterChange(menu.key)}>
                                    <span>{menu.title}</span>
                                </Menu.Item>
                            ))
                    }
                </Menu>
                <div className="header-div">
                    <span className="header-account">
                        <button className="link-botton" onClick={this.logout}>退出</button>
                    </span>
                </div>

            </div>

        )
    }
}

export default withRouter(Header)