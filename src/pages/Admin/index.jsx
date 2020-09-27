import React, { Component } from "react"

import { Link } from "react-router-dom"
import {message, Layout, Icon, Menu, Tabs, Button, Result} from "antd"
import moment from "moment"

import Header from "./header"
import menuList from "../../config/menuConfig"
import './admin.less'

import store from "../../store"
import { getAdminUserRoleMenuList } from "../../api"
import {isNull} from "../../utils/htmlUtils";

const { Footer, Content, Sider } = Layout
const TabPane = Tabs.TabPane;

export default class Index extends Component {
    constructor(props) {
        super(props);
        const selectedKey = ""
        this.state = {
            collapsed: false,
            menuPermissionsList: [],//拥有权限的菜单列表
            activeKey: '',//激活tabPane的key
            panes:[],//打开的pane
            selectedKey,//当前选中菜单的key
            selectedCharacter: '',
        };
    }

    /**
     * 菜单Link点击触发事件
     * @param menuData 菜单数据
     */
    handleClick(menuData) {
        let panes = this.state.panes
        let activeKey = menuData.key
        let flag = true
        panes.length > 0 && panes.map((item) => {
            if (item.key === activeKey) {
                flag = false
            }
            return item;
        })
        if (!panes || panes.length === 0 || flag) {
            panes.push(menuData)
        }
        let selectedKey = activeKey
        this.setState({ panes, activeKey, selectedKey })
    }

    /**
     * Tab切换触发
     * @param activeKey 当前切换到的TabPane的key
     */
    onChange = (activeKey) => {
        // let router
        // const panes = this.state.panes
        // panes.map((item) => {
        //     if (activeKey === item.key) {
        //         router = item.key
        //         return false
        //     }
        //     return item;
        // })
        // history.push(router)
        let selectedKey = activeKey
        this.setState({ selectedKey, activeKey })
    }

    /**
     * tabPane 编辑事件
     * @param targetKey 目标tabPane的key
     * @param action 事件类型
     */
    onEdit = (targetKey, action) => {
        this[action](targetKey)
    }

    /**
     * tabPane 删除事件
     * @param targetKey 目标tabPane的key
     */
    remove = (targetKey) => {
        let activeKey = this.state.activeKey
        let lastIndex
        this.state.panes.forEach((pane, i) => {
            if (pane.key === targetKey) {
                lastIndex = i - 1
            }
        })
        const panes = this.state.panes.filter(pane => pane.key !== targetKey);
        if (lastIndex >= 0 && activeKey === targetKey) {
            activeKey = panes[lastIndex].key
        }
        this.setState({ panes, activeKey })
    }

    /**
     * 组件已经挂载
     */
    componentDidMount() {
        this.user = store.user // 内存中没有存储user，应该返回登录页面
        if (!this.user || !this.user.success) {
            this.props.history.replace("/Login")
        } else {
            this.getMenuList()
                .then(menuPermissionsList => isNull(menuPermissionsList, []))
                .then(menuPermissionsList => menuPermissionsList.filter(menu => menu.split("/").length !== 3))
                .then(menuPermissionsList => menuPermissionsList[0] || '')
                .then(this.handleCharacterChange);
        }
    }

    /**
     * 获取用户的具有权限的菜单列表
     */
    getMenuList = async () => {
        return await getAdminUserRoleMenuList().then((data) => {
            if (typeof data.success !== 'undefined' && data.success === false) {
                message.error("获取权限菜单列表：" + data.message);
                this.props.history.replace("/Login")
            } else {
                if (data.length === 0) {
                    message.error("未分配菜单权限")
                    this.props.history.replace("/Login")
                } else {
                    this.setState({ menuPermissionsList: data })
                    return data;
                }
            }
        }, () => {
            this.props.history.replace("/Login");
        });
    }

    /**
     * 左侧菜单伸缩
     */
    toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    };

    /**
     * 根据菜单配置文件创建节点
     */
    createMenu = () => {
        const menuPermissionsList = this.state.menuPermissionsList;
        let submenuIndex = 0; //累计的每一项展开菜单索引
        let menu = [];
        const create = (menuData, el) => {
            for (let i = 0; i < menuData.length; i++) {
                if (menuData[i].children && menuPermissionsList.indexOf(menuData[i].key) !== -1) {  //如果有子级菜单
                    let children = [];
                    create(menuData[i].children, children);
                    submenuIndex++;
                    el.push(
                        <Menu.SubMenu
                            key={`sub${submenuIndex}`}
                            title={(
                                <span style={{ height: '100%', display: 'block' }}>
                                    <Icon type={menuData[i].icon} />{menuData[i].title}
                                </span>
                            )}>
                            {children}
                        </Menu.SubMenu>
                    )
                } else {   //如果没有子级菜单
                    if (menuPermissionsList.indexOf(menuData[i].key) !== -1) {
                        el.push(
                            <Menu.Item key={menuData[i].key} title={menuData[i].title}>
                                <Link to={menuData[i].key}
                                    onClick={() => this.handleClick(menuData[i])}>
                                    {menuData[i].icon ? <Icon type={menuData[i].icon} /> : null}
                                    <span>{menuData[i].title}</span>
                                </Link>
                            </Menu.Item>
                        )
                    }
                }
            }
        };
        const subMenu = menuList.filter(menu => menu.key === this.state.selectedCharacter)[0] || {};
        create(subMenu.children || [], menu);
        return menu;
    }

    handleCharacterChange = (selectedCharacter) => {
        this.setState({selectedCharacter})
    }

    render() {
        return (
            <Layout style={{ height: '100%' }}>
                <Header menuList={menuList}
                        menuPermissionsList={this.state.menuPermissionsList}
                        selectedCharacter={this.state.selectedCharacter}
                        onCharacterChange={this.handleCharacterChange}/>
                <Layout style={{ height: '100%' }}>
                    <Sider width={200} collapsed={this.state.collapsed}>
                        <div style={{ width: '100%', textAlign: 'right' }}>
                            <button className="link-button" onClick={this.toggleCollapsed} color={'black'}>
                                <Icon type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'} />
                            </button>
                        </div>
                        <Menu
                            mode="inline"
                            theme={"dark"}
                            selectedKeys={this.state.selectedKey}
                            style={{borderRight: 0 }}>
                            {this.createMenu()}
                        </Menu>
                    </Sider>
                    <Layout>
                        <Content>
                            <Tabs
                                style={{marginTop:10,marginLeft:5,marginRight:5,}}
                                hideAdd={true}
                                onChange={this.onChange.bind(this)}
                                activeKey={this.state.activeKey}
                                type="editable-card"
                                onEdit={this.onEdit}>
                                {/*渲染TabPane*/}
                                {
                                    this.state.panes.map((pane) => {
                                    let Component = pane.component;
                                    return (
                                        <TabPane tab={pane.title} key={pane.key} closable={true}>
                                            <Component pane={pane} />
                                        </TabPane>
                                    )})
                                }
                            </Tabs>
                            {/*没有选中菜单显示欢迎页*/}
                            {this.state.panes.length === 0?(
                                <Result
                                    style={{ marginTop: 100 }}
                                    icon={<img alt='' src={'http://img.haihu.com/012003112123b2bfe5ac457d8a26368de3068784.png'}></img>}
                                    title="欢迎来到海狐后台!"
                                    subTitle="Copyright 2020 淘粉吧技术部出品"
                                    extra={<Button type="primary">退出</Button>}
                                />
                            ):(null)}
                        </Content>
                        <Footer className="footer">淘粉吧@{moment(new Date()).format("YYYY")} 海狐技术部出品</Footer>
                    </Layout>
                </Layout>
            </Layout>
        )
    }
}