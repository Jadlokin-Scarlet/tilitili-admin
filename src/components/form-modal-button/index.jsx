import React, {Component} from "react";
import SimpleModalButton from "../simple-modal-button";
import {Form, Input} from "antd";
import {If} from "../../utils/htmlUtils";

export default class FormModalButton extends Component{
    constructor(props) {
        super(props);
        this.state = this.props.itemConfigList
            .map(itemConfig => Object.defineProperty({}, itemConfig.key, ''));
    }

    render() {
        return (
            <SimpleModalButton>
                <Form>
                    {this.props.itemConfigList.map(itemConfig => (
                        <Form.Item label={itemConfig.label}>
                            {If (itemConfig.type === 'input').then(() => (
                                <Input value={this.state[itemConfig.key]} onChange={event => this.setState(Object.defineProperty({}, itemConfig.key, event.target.value))}/>
                            ))}
                        </Form.Item>
                    ))}
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