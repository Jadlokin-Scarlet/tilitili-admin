import React, {Component} from "react";
import {Button, Card, Col, Row, Spin} from "antd";
import {getFlag, updateFlag} from "../../../api";
import {checkResp} from "../../../utils/HtmlUtils";
import {converseToForm} from "../../../components/default-modal-button/DefaultModalButton";

const formConfig = [
    {label: 'staff1是否展示', key: 'isStaffShow1', type: 'select', resource: 'showList'},
    {label: 'staff2是否展示', key: 'isStaffShow2', type: 'select', resource: 'showList'},
    {label: 'ed名', key: 'musicName', type: 'input'},
    {label: 'ed社团', key: 'musicOwner', type: 'input'},
    {label: 'ed专辑', key: 'musicCard', type: 'input'},
    {label: 'ed图片', key: 'musicImage', type: 'input'},
    {label: 'ed原曲', key: 'musicSource', type: 'input'},
    {label: '小标题', key: 'tips', type: 'textArea'},
    {label: '期数', key: 'v', type: 'input', disabled: true},
    {label: '制作时间', key: 'markTime', type: 'input', disabled: true},
    {label: '统计时间', key: 'countTime', type: 'input', disabled: true},
]

const resources = {
    showList: [{value: 1, text: "展示"}, {value: 0, text: "不展示"}],
}

export default class VideoResourceManager extends Component  {
    constructor(props) {
        super(props);
        this.state = {
            isStaffShow1: null,
            isStaffShow2: null,
            musicName: null,
            musicOwner: null,
            musicCard: null,
            musicImage: null,
            musicSource: null,
            loading: false,
        }
    }

    componentDidMount = () => {
        this.handleRefresh();
    }

    handleChange = (filters) => {
        this.setState(filters)
    }

    handleSubmit = () => {
        const {isStaffShow1,isStaffShow2,musicName,musicOwner,musicCard,musicImage,musicSource,tips} = this.state;
        const params = {isStaffShow1,isStaffShow2,musicName,musicOwner,musicCard,musicImage,musicSource,tips};
        this.setState({loading: true});
        updateFlag(params).then(checkResp).finally(() => {
            this.setState({loading: false});
        });
    }

    handleRefresh = async () => {
        this.setState({loading: true});
        this.setState(await getFlag().then(checkResp))
        this.setState({loading: false});
    }

    render() {
        const params = this.state;
        const onChange = this.handleChange;
        return (
            <Card style={{width: '90%'}}>
                <Spin spinning={this.state.loading}>
                    {converseToForm({formConfig, params, resources, onChange, col: 2})}
                    <Row type="flex" justify="space-around" align="middle" >
                        <Col span={3}>
                            <Button type="primary" onClick={this.handleRefresh}>刷新</Button>
                        </Col>
                        <Col span={3}>
                            <Button type="primary" onClick={this.handleSubmit}>保存</Button>
                        </Col>
                    </Row>
                </Spin>
            </Card>
        )
    }
}