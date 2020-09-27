import React,{Component} from "react"
import {Button, Card, Col, Form, Icon, Input, Row} from "antd";
export class Body extends Component{


    constructor(props) {
        super(props);
        this.state = {
        };
    }

    render() {
        return (
            <Card style={{margin: "1%"}}>
                <div style={{width: "60%"}}>
                    <Row type="flex" justify="space-around" align="middle">
                        <Col>
                            <h2><Icon type="user" /> 用户登录</h2>
                            <Form>
                                <Form.Item>
                                    <Input
                                        value={this.state.username}
                                        onChange={e => {this.setState({username: e.target.value})}}
                                        prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                    />
                                </Form.Item>
                                <Form.Item>
                                    <Input
                                        value={this.state.password}
                                        onChange={e => {this.setState({password: e.target.value})}}
                                        prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                    />
                                </Form.Item>
                                <Form.Item>
                                    <Button type="primary" htmlType="submit" className="login-form-button">
                                        登 录
                                    </Button>
                                </Form.Item>
                            </Form>
                        </Col>
                    </Row>
                </div>
            </Card>
        )
    }
}
