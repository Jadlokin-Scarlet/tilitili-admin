import React, {Component} from "react";
import {Button, Col, DatePicker, Form, Input, InputNumber, message, Row, Select} from "antd";
import {defineProperty, emptyFunc, For, If, InputSelect, isNotNull, isNull, toString} from "../../utils/HtmlUtils";
import DragModal from "../drag-modal/DragModal";
import {bilibiliVideoIframeUrlConverse} from "../../utils/BilibiliUtil";
const { TextArea } = Input;

/*
 * form config配置
 *
 * label：表单字段中文名
 * type：类型，有input,inputSelect,select,datePicker,textArea,inputNumber
 * key：表单字段名
 * disabled：是否可编辑
 * placeholder：没有输入值时的提示语
 * resources：用于各种select的资源，text为中文，value为key
 * min：用于inputNumber，限制数字最小值
 * max：用于inputNumber，限制数字最大值
 *
 * hidden：是否隐藏
 * submit：是否随表单提交
 * selectedIndex：如果提交的key和selectedRow的key不一样，则key为提交的key，selectedIndex为selectedRow的key
 */
export default class DefaultModalButton extends Component {
    constructor(props) {
        super(props);
        this.state = {
            visible: isNull(this.props.visible, false),
            loading: false,
        }
        for (const config of this.props.formConfig || []) {
            this.state[config.key] = config.value;
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.visible !== this.props.visible && this.props.visible) {
            this.handleShowModal();
        }
    }

    setVisible = visible => {
        if (this.props.onVisibleChange) {
            this.props.onVisibleChange(visible);
        }else {
            this.setState({visible});
        }
    }

    getVisible = () => {
        return isNull(this.props.visible, this.state.visible);
    }

    handleShowModal = () => {
        const { selectedRow={}, formConfig=[], onShowModal=emptyFunc } = this.props;
        const [isShow=true, error] = onShowModal() || [];
        if (isShow) {
            const newState = {};
            for (const config of formConfig) {
                const selectedIndex = isNull(config.selectedIndex)? config.key: config.selectedIndex;
                newState[config.key] = isNull(config.value)? selectedRow[selectedIndex]: config.value;
            }
            this.showModal();
            this.setState(newState);
        }else {
            message.error(error)
        }
    }

    handleOk = async () => {
        const { onSuccess=emptyFunc, formConfig=[] } = this.props;
        const params = {};
        for (const config of formConfig) {
            if (config.submit !== false) {
                if (config.type === 'datePicker') {
                    if (isNotNull(params[config.key] = this.state[config.key])) {
                        params[config.key] = this.state[config.key].format('YYYY-MM-DD hh:mm:ss');
                    }
                }else {
                    params[config.key] = this.state[config.key];
                }
            }
        }
        // 如果不是表单模态框，就是点击确定直接关闭模态框，否则设置按钮loading，等后台返回成功再关
        if (isNotNull(this.props.modalRender)) {
            this.closeModal();
        }
        try {
            this.setState({loading: true});
            const data = await this.props.updateApi(params);
            onSuccess(data);
            if (isNull(this.props.modalRender)) {
                this.closeModal();
            }
        }finally {
            this.setState({loading: false});
        }
    }

    handleCancel = () => {

    }

    showModal = () => {
        this.setVisible(true);
    }

    closeModal = () => {
        this.setVisible(false);
    }

    handleChange = state => {
        this.setState(state)
    }

    render() {
        const { resources={}, formConfig=[], col=1 } = this.props
        const params = this.state;
        const onChange = this.handleChange;
        return (
            <span>
                <Button style={this.props.buttonStyle || { marginLeft: 8 }}
                        onClick={this.handleShowModal}
                        disabled={isNull(this.props.disabled, false)}
                        hidden={this.props.hidden}
                        loading={this.state.loading}>
                    {this.props.value || '编辑'}
                </Button>
                <DragModal
                    confirmLoading={this.props.confirmLoading || this.state.loading}
                    isHidden={this.props.isHidden}
                    title={this.props.title || '标题'}
                    visible={this.getVisible()}
                    onOk={this.handleOk}
                    onCancel={this.closeModal}
                    width={this.props.width || (col * 400)}
                    footer={this.props.footer}
                    centered={this.props.centered}>
                    {If(isNotNull(this.props.modalRender)).then(() => (
                        this.props.modalRender(this.state)
                    )).else(() => (
                        converseToForm({formConfig, params, resources, col, onChange})
                    ))}
                </DragModal>
            </span>
        );
    }
}

export const converseToForm = ({formConfig=[], params={}, resources={}, col=1, onChange=emptyFunc}) => (
    <Form labelCol={{ span: 6 }} wrapperCol={{ span: 14 }} layout="horizontal" size='small'>
        <Row gutter={24}>
            {For(formConfig).if(config => !config.hidden).then(({label,type,key,disabled,placeholder,resource,min,max,suffix}, index) => (
                <Col span={24 / col} key={index} style={{ display: index < formConfig.length ? 'block' : 'none' }}>
                    <Form.Item label={label} key={index} style={formConfig.length > 5? {margin: 0}: {}}>
                        {If(type === 'input').then(() => (
                            <Input value={params[key]} disabled={disabled} onChange={e => onChange(defineProperty({}, key, e.target.value))} placeholder={placeholder}/>
                        )).elseIf(type === 'inputSelect').then(() => (
                            <InputSelect resource={resources[resource]} value={params[key]} disabled={disabled} onChange={value => onChange(defineProperty({}, key, value))}/>
                        )).elseIf(type === 'select').then(() => (
                            <Select value={toString(params[key])} disabled={disabled} onChange={value => onChange(defineProperty({}, key, value))}>
                                {For(resources[resource]).then((item, index) => (
                                    <Select.Option key={index} value={toString(item.value)}>{item.text}</Select.Option>
                                ))}
                            </Select>
                        )).elseIf(type === 'datePicker').then(() => (
                            <DatePicker value={params[key]} showTime={true} onChange={value => onChange(defineProperty({}, key, value))} />
                        )).elseIf(type === 'textArea').then(() => (
                            <TextArea value={params[key]} disabled={disabled} onChange={e => onChange(defineProperty({}, key, e.target.value))} placeholder={placeholder}/>
                        )).elseIf(type === 'inputNumber').then(() => (
                            <InputNumber value={params[key]} disabled={disabled} onChange={value => onChange(defineProperty({}, key, value))} placeholder={placeholder} min={min} max={max} suffix={suffix}/>
                        )).elseIf(type === 'bilibiliVideo' && isNotNull(params.av)).then(() => (
                            <iframe src={bilibiliVideoIframeUrlConverse(params.av)}
                                title="video"
                                allowFullScreen="allowfullscreen"
                                width="492"
                                height={window.innerHeight * 492 / window.innerWidth}
                                sandbox="allow-top-navigation allow-same-origin allow-forms allow-scripts">
                            </iframe>
                        )).endIf()}
                    </Form.Item>
                </Col>
            ))}
        </Row>
    </Form>
)