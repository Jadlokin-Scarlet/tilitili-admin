import React, {Component} from "react";
import DragModal from "../../components/drag-modal/DragModal";
import {List, Popover, Typography} from "antd";
import {isNull} from "../../utils/HtmlUtils";
const { Text } = Typography;

const configList = [
    {key: 'view', text: '播放量差'},
    {key: 'reply', text: '评论差'},
    {key: 'favorite', text: '收藏差'},
    {key: 'coin', text: '硬币差'},
    {key: 'page', text: '分页'},
    {key: 'viewPoint', text: '播放得分'},
    {key: 'a', text: '修正a'},
    {key: 'b', text: '修正b'},
    {key: 'checkPoint', text: '计算得分'},
    {key: 'point', text: '原得分'},
]

export default class CheckPoint extends Component {

    constructor(props) {
        super(props);
        this.state = {
            visible: false,
        }
        for (const config of configList) {
            this.state[config.key] = isNull(config.value, null);
        }
    }

    handleShow = () => {
        const state = {visible: true};
        for (const config of configList) {
            state[config.key] = isNull(config.value, this.props.record[config.key]);
        }
        this.setState(state)
    }

    handleClose = () => {
        const state = {visible: false};
        for (const config of configList) {
            state[config.key] = isNull(config.value, null);
        }
        this.setState(state)
    }

    render() {
        const {record} = this.props;
        const data = configList.map(config => ({
            text: config.text,
            value: this.state[config.key],
        }));

        return <>
            <Popover placement="right" content="分数计算检查异常，点击检查计算过程">
                <Text type="danger" onClickCapture={this.handleShow}>{record.point}</Text>
            </Popover>
            <DragModal
                visible={this.state.visible}
                onOk={this.handleShow}
                onCancel={this.handleClose}
            >
                <List
                    size="small"
                    bordered
                    dataSource={data}
                    renderItem={item => <List.Item>{`${item.text}：${item.value}`}</List.Item>}
                />
            </DragModal>
        </>
    }

}