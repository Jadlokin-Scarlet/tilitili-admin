import React,{Component} from "react"
import { Modal, Button } from 'antd';
import {If, isNull} from "../../utils/htmlUtils";

export default class SimpleModalButton extends Component {
    constructor(props) {
        super(props)
        this.state = {
            visible: false,
            loading: false,
        };
    }

    showModal = () => {
        const onShowModal = this.props.onShowModal || (() => true);
        const isShowModal = isNull(onShowModal(), true);
        this.setState({visible: isShowModal})
    };

    handleOk = () => {
        this.setState({loading: true})
        const onOk = this.props.onOk || (async () => true);
        onOk().then((isCloseModal = true) => {
            this.setState({
                visible: !isCloseModal,
                loading: false,
            })
        })
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
                <Button style={this.props.buttonStyle}
                        type="primary"
                        onClick={this.showModal}
                        disabled={isNull(this.props.disabled, false)}
                        hidden={this.props.hidden}
                >{this.props.title || '编辑'}</Button>
                <Modal
                    title={this.props.title || '标题'}
                    visible={visible}
                    onOk={this.handleOk}
                    onCancel={this.handleCancel}
                    width={this.props.width}
                    footer={this.props.footer}
                    centered={this.props.centered}
                    confirmLoading={this.state.loading}
                >
                    {this.props.children}
                </Modal>
            </span>
        );
    }
}
// {If (Array.isArray(this.props.children) || ['span'].includes(this.props.children.type) || ['Form'].includes(this.props.children.type.name)).then(() => (
//     this.props.children
// )).else(() => (
//     <this.props.children.type {...this.props.children.props} visible={this.state.visible}/>
// ))}
// {
//     typeof this.props.children !== "object"
//     || Array.isArray(this.props.children)
//     || isNull(this.props.children.type)
//     || this.props.children.type.name === "Form"
//         ? this.props.children
//         : React.cloneElement(this.props.children, {visible: this.state.visible})
// }