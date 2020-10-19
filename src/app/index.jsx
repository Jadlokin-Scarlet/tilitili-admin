import React, {Component} from 'react';
import './App.less';
import {Button, Card, Col, Icon, Layout, Menu, Result, Row, Tabs, Typography} from "antd";
import {For, If, isEmpty, isNotNull, isNull} from "../utils/htmlUtils";
import LoginModal from "./LoginModal";
import {isLogin, reqLoginOut} from "../api";
import {HashRouter, Link} from "react-router-dom";
import {menuList} from "../config/menuConfig";
const { Header, Content, Sider, Footer } = Layout;
const { Title} = Typography;

const menuMap = {};
(function converseToMap (list) {
    list.forEach(item => {
        if (item.children) {
            converseToMap(item.children)
        }else {
            menuMap[item.key] = item;
        }
    })
})(menuList)

export default class App extends Component{

    constructor(props) {
        super(props);
        this.state = {
            collapsed: false,
            userName: undefined,
            selectedKeyList: [],
        }
    }

    componentDidMount() {
        this.checkIsLogin();
    }

    checkIsLogin = () => {
        isLogin().then(this.login).catch(this.loginOut)
    }

    login = userName => {
        this.setState({userName});
    }

    loginOut = () => {
        this.setState({userName: null});
    }

    reqLoginOut = () => {
        reqLoginOut().then(() => this.setState({userName: null}))
    }

    toggleCollapsed = () => {
        this.setState({
            collapsed: !this.state.collapsed,
        });
    }

    handleSelected = (selectedKey) => {
        let selectedKeyList = this.state.selectedKeyList;
        if (selectedKeyList.includes(selectedKey)) {
            selectedKeyList = this.state.selectedKeyList.filter(key => key !== selectedKey);
        }
        selectedKeyList.unshift(selectedKey);
        this.setState({selectedKeyList})
    }

    remove = (targetKey) => {
        const selectedKeyList = this.state.selectedKeyList.filter(selectedKey => selectedKey !== targetKey);
        this.setState({ selectedKeyList })
    }

    onEdit = (targetKey, action) => {
        this[action](targetKey)
    }

    createMenu = (menuList) => {
        return menuList.map((menu) => {
            if (isNull(menu.children)) {
                return (
                    <Menu.Item key={menu.key} title={menu.title}>
                        <Link to={menu.key} onClick={this.handleSelected.bind(this, menu.key)}>
                            {If (isNotNull(menu.icon)).then(() => (
                                <Icon type={menu.icon} />
                            )).endIf()}
                            <span>{menu.title}</span>
                        </Link>
                    </Menu.Item>
                )
            }else {
                return (
                    <Menu.SubMenu
                        key={menu.key}
                        title={(
                            <span style={{ height: '100%', display: 'block' }}>
                                {If (isNotNull(menu.icon)).then(() => (
                                    <Icon type={menu.icon} />
                                )).endIf()}
                            </span>
                        )}>
                        {this.createMenu(menu.children)}
                    </Menu.SubMenu>
                )
            }
        })
    }

    render() {
        const selectedList = this.state.selectedKeyList.map(selectedKey => menuMap[selectedKey]);
        return (
            <HashRouter>
                <Layout style={{minHeight: "100vh"}}>
                    <Header style={{background: "white", height: 'auto'}}>
                        <Menu mode="horizontal" style={{float: "left"}} selectable={false}>
                            <Menu.Item>
                                <Title level={2} strong={false} style={{margin: "0px"}} className="primary-color">
                                    管理后台
                                </Title>
                            </Menu.Item>
                        </Menu>
                        <Menu mode="horizontal" style={{float: "right"}} selectable={false}>
                            {If(this.state.userName !== undefined).then(() => (
                                If(isNull(this.state.userName)).then(() => (
                                    <Menu.Item>
                                        <LoginModal onLoginSuccess={this.login}/>
                                    </Menu.Item>
                                )).else(() => (
                                    <Menu.SubMenu style={{minWidth: "10px"}} title={<Title level={3} className="primary-color" style={{margin: "0px"}}>Hi! {this.state.userName}</Title>}>
                                        <Menu.Item onClick={this.reqLoginOut}>
                                            <Icon type="logout" />退出登录
                                        </Menu.Item>
                                    </Menu.SubMenu>
                                ))
                            )).endIf()}
                        </Menu>
                    </Header>
                    <Layout>
                        <Sider width={200} collapsed={this.state.collapsed}>
                            <div style={{ width: '100%', textAlign: 'right' }}>
                                <Button onClick={this.toggleCollapsed} type="link" ghost>
                                    <Icon type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'} />
                                </Button>
                            </div>
                            <Menu
                                mode="inline"
                                theme={"dark"}
                                selectedKeys={this.state.selectedKeyList[0]}
                                style={{borderRight: 0 }}>
                                {this.createMenu(menuList)}
                            </Menu>
                        </Sider>
                        <Layout>
                            <Content>
                                <div>
                                    <Tabs type="editable-card"
                                          style={{marginTop:10,marginLeft:5,marginRight:5,}}
                                          hideAdd={true}
                                          onChange={this.handleSelected.bind(this)}
                                          activeKey={this.state.selectedKeyList[0]}
                                          onEdit={this.onEdit}>
                                        {For (selectedList).then(selected => (
                                            <Tabs.TabPane tab={selected.title} key={selected.key} closable={true}>
                                                <selected.component selectedTab={selected} />
                                            </Tabs.TabPane>
                                        ))}
                                    </Tabs>
                                    {If (isEmpty(this.state.selectedKeyList)).then(() => (
                                        <Result
                                            style={{ marginTop: 100 }}
                                            icon={<img alt='' src='/min-logo.png'/>}
                                            title="欢迎来到TiliTili后台!"
                                        />
                                    )).endIf()}
                                </div>
                            </Content>
                            <Footer style={{backgroundColor: "white"}}>
                                <Row type="flex" justify="space-around" align="middle">
                                    <Col>
                                        <a href="http://beian.miit.gov.cn/" target="_Blank" rel="noopener noreferrer">浙ICP备2020037336号</a>
                                    </Col>
                                </Row>
                            </Footer>
                        </Layout>
                    </Layout>
                </Layout>
            </HashRouter>
        )
    }
}
