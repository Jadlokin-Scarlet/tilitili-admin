import React, {Component} from "react";
import SimpleModalButton from "../components/simple-modal-button";
import {Form, Input} from "antd";

export default class LoginModal extends Component{
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        }
    }



    render() {
        return (
            <SimpleModalButton title="登陆">
                <Form labelCol={{ span: 6 }} wrapperCol={{ span: 12 }} layout="horizontal">
                    <Form.Item label="用户名">
                        <Input value={this.state.username} onChange={event => this.setState({username: event.target.value})}/>
                    </Form.Item>
                    <Form.Item label="密码">
                        <Input value={this.state.username} onChange={event => this.setState({username: event.target.value})}/>
                    </Form.Item>
                </Form>
            </SimpleModalButton>
        )
    }

}