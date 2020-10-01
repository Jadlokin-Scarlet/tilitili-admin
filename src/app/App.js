import React, {Component} from 'react';
import 'antd/dist/antd.less';
import './App.less';
import {Col, Layout, Row, Typography} from "antd";
import {If, isEmptyObject} from "../utils/htmlUtils";
import LoginModal from "./LoginModal";
const { Header, Content, Footer, Sider } = Layout;
const { Title, Text } = Typography;


export default class App extends Component{

    constructor(props) {
        super(props);
        this.state = {
            user: {}
        }
    }

    login = user => {
        this.setState({user})
    }

    loginOut = () => {
        this.setState({user: {}})
    }

    showLoginModal = () => {

    }


    render() {
        const { user } = this.state;
        return (
            <Layout>
                <Header>
                    <Row type="flex" justify="space-between">
                        <Col span={4}>
                            <Row type="flex" align="middle">
                                <Col span={6}>
                                    <img style={{maxWidth: "60%"}} src="/min-logo.png" alt=""/>
                                </Col>
                                <Col span={18}>
                                    <Title className="primary-color" level={2} strong={false} style={{margin: "0px"}}>管理后台</Title>
                                </Col>
                            </Row>
                        </Col>
                        <Col span={2}>
                            {If(isEmptyObject(user)).then(() => (
                                <LoginModal/>
                            )).else(() => (
                                <Text>Hi! {user.name}</Text>
                            ))}
                        </Col>
                    </Row>
                </Header>
            </Layout>
        )
    }
}
