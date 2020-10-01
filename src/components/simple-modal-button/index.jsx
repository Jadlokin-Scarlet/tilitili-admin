import React,{Component} from "react"
import { Modal, Button } from 'antd';
import {If, isNull} from "../../utils/htmlUtils";

export default class SimpleModalButton extends Component {
    constructor(props) {
        super(props)
        this.state = {
            visible: false,
        };
    }

    showModal = () => {
        const showModal = this.props.showModal || (() => true);
        const isShowModal = isNull(showModal(), true);
        this.setState({visible: isShowModal})
    };

    handleOk = () => {
        const onOk = this.props.onOk || (() => true);
        const isCloseModal = isNull(onOk(), true);
        this.setState({visible: !isCloseModal})
    };

    handleCancel = () => {
        const onCancel = this.props.onCancel || (() => true);
        const isCloseModal = isNull(onCancel(), true);
        this.setState({visible: !isCloseModal})
    };

    render() {
        const { visible } = this.state;
        return (
            <span>
                <Button style={{ marginLeft: 8 }}
                        type="primary"
                        onClick={this.showModal}
                        disabled={isNull(this.props.disabled, false)}
                        hidden={this.props.hidden}
                        title={this.props.title || '编辑'}
                />
                <Modal
                    title={this.props.title || '标题'}
                    visible={visible}
                    onOk={this.handleOk}
                    onCancel={this.handleCancel}
                    width={this.props.width}
                    footer={this.props.footer}
                    centered={this.props.centered}
                >
                    {If (Array.isArray(this.props.children)).then(() => (
                        this.props.children
                    )).else(() => (
                        <this.props.children.type {...this.props.children.props} visible={this.state.visible}/>
                    ))}
                    {/*{*/}
                    {/*    typeof this.props.children !== "object"*/}
                    {/*        || Array.isArray(this.props.children)*/}
                    {/*        || isNull(this.props.children.type)*/}
                    {/*        || this.props.children.type.name === "Form"*/}
                    {/*        ? this.props.children*/}
                    {/*        : React.cloneElement(this.props.children, {visible: this.state.visible})*/}
                    {/*}*/}
                </Modal>
            </span>
        );
    }
}