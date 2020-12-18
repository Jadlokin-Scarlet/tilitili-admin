import React, {Component} from "react";
import {Button, Col, Form, Icon, Input, Row} from "antd";

export default class LoginModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            userName: null,
            password: null,
        }
    }

    login = () => {
        const { userName, password } = this.state;
        this.props.onLogin(userName, password)
        this.refresh();
    }

    register = () => {
        const { userName, password } = this.state;
        this.props.onRegister(userName, password);
        this.refresh();
    }

    refresh = () => {
        this.setState({
            userName: null,
            password: null,
        })
    }

    render() {
        return <Row type="flex" align="middle" className="max-height">
            <Col span={24}>
                <Row type="flex" justify="space-around">
                    <Col>
                        <Icon type="lock" className="primary-color font-size-100px"/>
                    </Col>
                </Row>
                <Row type="flex" justify="space-around">
                    <Col span={6}>
                        <Form layout="horizontal" onSubmit={this.login}>
                            <Form.Item label="用户名" labelCol={{ span: 6 }} wrapperCol={{ span: 12 }}>
                                <Input value={this.state.userName} onChange={event => this.setState({userName: event.target.value})} type="userName"/>
                            </Form.Item>
                            <Form.Item label="密码" labelCol={{ span: 6 }} wrapperCol={{ span: 12 }}>
                                <Input value={this.state.password} onChange={event => this.setState({password: event.target.value})} type="password"/>
                            </Form.Item>
                            <Form.Item>
                                <Row type="flex" justify="space-around" align="middle">
                                    <Col>
                                        <Button type="primary" htmlType="submit">
                                            登陆
                                        </Button>
                                    </Col>
                                    <Col>
                                        <Button type="primary" onClick={this.register} disabled>
                                            注册
                                        </Button>
                                    </Col>
                                </Row>
                            </Form.Item>
                        </Form>
                    </Col>
                </Row>
            </Col>
        </Row>
    }

}