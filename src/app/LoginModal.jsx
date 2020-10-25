import React, {Component} from "react";
import SimpleModalButton from "../components/simple-modal-button";
import {Form, Input} from "antd";
import {reqLogin} from "../api";
import {isNull} from "../utils/HtmlUtils";

export default class LoginModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            userName: '',
            password: '',
        }
    }

    login = async () => {
        const { userName, password } = this.state;
        const data = await reqLogin(userName, password)
        if (isNull(data.success)) {
            this.props.onLoginSuccess(userName);
        }else {
            return false;
        }

    }

    refresh = () => {
        this.setState({
            userName: '',
            password: '',
        })
    }

    render() {
        return (
            <SimpleModalButton title="登陆" onOk={this.login} onShowModal={this.refresh}>
                <Form labelCol={{ span: 6 }} wrapperCol={{ span: 12 }} layout="horizontal">
                    <Form.Item label="用户名">
                        <Input value={this.state.userName} onChange={event => this.setState({userName: event.target.value})}/>
                    </Form.Item>
                    <Form.Item label="密码">
                        <Input value={this.state.password} onChange={event => this.setState({password: event.target.value})}/>
                    </Form.Item>
                </Form>
            </SimpleModalButton>
        )
    }

}