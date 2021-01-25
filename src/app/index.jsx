import React, {Component} from 'react';
import './index.less';
import {Button, Col, Icon, Layout, Menu, Result, Row, Tabs, Typography} from "antd";
import {For, If, isEmpty, isNotNull, isNull} from "../utils/HtmlUtils";
import LoginModal from "./LoginModal";
import {isLogin, reqLogin, reqLoginOut} from "../api";
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
            isLogin: null,
            selectedKeyList: [],
            selectedFirstKey: "/rank",
        }
    }

    componentDidMount() {
        this.checkIsLogin();
    }

    checkIsLogin = () => {
        isLogin().then(this.login).catch(this.loginOut)
    }

    login = () => {
        this.setState({isLogin: true});
    }

    loginOut = () => {
        this.setState({isLogin: false});
    }

    reqLoginOut = () => {
        reqLoginOut().then(this.loginOut)
    }

    reqLogin = (username, password) => {
        reqLogin(username, password).then(this.login).catch(this.loginOut)
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

    handleSelectedFirstKey = (selectedFirstKey) => {
        this.setState({selectedFirstKey})
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
                        <div style={{float: "left", width: "150px"}}>
                            <Title level={2} strong={false} style={{margin: "0px"}} className="primary-color">
                                管理后台
                            </Title>
                        </div>
                        <Menu mode="horizontal" className="header-menu" selectedKeys={this.state.selectedFirstKey} style={{width: '40%', float: 'left'}}>
                            {If(isNotNull(this.state.isLogin) && this.state.isLogin).then(()=>(
                                For (menuList).then(menu => (
                                    <Menu.Item key={menu.key} title={menu.title} onClick={()=>this.handleSelectedFirstKey(menu.key)}>
                                        <span>{menu.title}</span>
                                    </Menu.Item>
                                ))
                            )).endIf()}
                        </Menu>
                        <Menu mode="horizontal" style={{float: "right"}} selectable={false}>
                            {If(this.state.isLogin).then(() => (
                                <Menu.Item onClick={this.reqLoginOut}>
                                    <Icon type="logout" />退出登录
                                </Menu.Item>
                            )).endIf()}
                        </Menu>
                    </Header>
                    {If(isNotNull(this.state.isLogin)).then(() => (
                        <Layout>
                        <Sider width={200} collapsed={this.state.collapsed} theme="light">
                            {If(this.state.isLogin).then(() => <>
                                <div style={{ width: '100%', textAlign: 'right' }}>
                                    <Button onClick={this.toggleCollapsed} type="link">
                                        <Icon type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'} />
                                    </Button>
                                </div>
                                <Menu
                                    mode="inline"
                                    selectedKeys={this.state.selectedKeyList[0]}
                                    style={{borderRight: 0 }}>
                                    {this.createMenu(menuList.filter(menu => menu.key === this.state.selectedFirstKey)[0].children)}
                                </Menu>
                            </>).endIf()}
                        </Sider>
                        <Layout>
                            <Content>
                                {If(this.state.isLogin).then(() => <>
                                    <Tabs type="editable-card"
                                          style={{marginTop:10,marginLeft:5,marginRight:5,}}
                                          hideAdd={true}
                                          onChange={this.handleSelected.bind(this)}
                                          activeKey={this.state.selectedKeyList[0]}
                                          onEdit={this.onEdit}>
                                        {For (selectedList).then((selected, index) => (
                                            <Tabs.TabPane tab={selected.title} key={selected.key} closable={true}>
                                                <selected.component selectedTab={selected} isHidden={index !== 0} />
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
                                </>).else(() => <>
                                    <LoginModal onLogin={this.reqLogin} onRegister={this.reqRegister}/>
                                </>)}
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
                    )).endIf()}
                </Layout>
            </HashRouter>
        )
    }
}
