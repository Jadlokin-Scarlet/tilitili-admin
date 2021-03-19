import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {addRecommend} from "../../../api";
import {Input, InputNumber} from "antd";


export default class RecommendSelfVideoToNow extends Component {
    render() {
        const {resources} = this.props;
        const {recommendIssueResource=[]} = resources;
        const issueId = recommendIssueResource[0]?.value;
        const name = recommendIssueResource[0]?.text;
        const selectedRow = {issueId}
        return (
            <DefaultModalButton
                {...this.props}
                col={2}
                title={`推荐视频至 ${name}`}
                value='推荐视频'
                formConfig={this.formConfig}
                selectedRow={selectedRow}
                updateApi={addRecommend}
            />
        )
    }

    timeRender = (startTime, handleChange) => {
        const oldStartTimeMinute = Number.isInteger(startTime)? Math.floor(startTime / 60): null
        const oldStartTimeSecond = Number.isInteger(startTime)? (startTime % 60): null
        function setStartTimeMinute(value) {
            handleChange(value * 60 + oldStartTimeSecond)
        }
        function setStartTimeSecond(value) {
            handleChange(oldStartTimeMinute * 60 + value)
        }
        return (
            <Input.Group compact>
                <InputNumber style={{ width: '40%' }} value={oldStartTimeMinute} onChange={setStartTimeMinute} formatter={a=>`${a}分`} min={0}/>
                <InputNumber style={{ width: '60%' }} value={oldStartTimeSecond} onChange={setStartTimeSecond} formatter={a=>`${a}秒`} min={0} max={60}/>
            </Input.Group>
        )
    }

    formConfig = [
        {label: 'issueId', key: 'issueId', type: 'select', resource: 'recommendIssueResource'},
        {key: 'type', value: 1, hidden: true},
        {label: 'av*', key: 'av', type: 'input', groupBy: {bv: null}},
        {label: 'bv*', key: 'bv', type: 'input', groupBy: {av: null}},
        {label: '开始展示时间', key: 'startTime', type: 'inputGroup', value: 0, render: this.timeRender},
        {label: '结束展示时间', key: 'endTime', type: 'inputGroup', value: 0, render: this.timeRender},
        {label: '原作者', key: 'externalOwner', type: 'input'},
        {label: '推荐语*', key: 'text', type: 'textArea', col: 2, autoSize: {minRows: 5}},
    ];
}