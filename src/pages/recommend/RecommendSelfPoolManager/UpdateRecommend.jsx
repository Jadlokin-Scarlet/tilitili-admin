import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommend} from "../../../api";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {Input, InputNumber} from "antd";

export default class UpdateRecommend extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                col={2}
                title='编辑'
                value='编辑'
                disabled={isEmptyObject(this.props.selectedRow)}
                formConfig={this.formConfig}
                updateApi={updateRecommend}
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
        {key: 'id', hidden: true},
        {label: 'av号', key: 'av', type: 'input', disable: true},
        {label: '推荐人', key: 'operator', type: 'input'},
        {label: '开始展示时间', key: 'startTime', type: 'inputGroup', render: this.timeRender},
        {label: '结束展示时间', key: 'endTime', type: 'inputGroup', render: this.timeRender},
        {label: '原作者', key: 'externalOwner', type: 'input'},
        {label: '排序值', key: 'sortNum', type: 'inputNumber'},
        {label: '推荐语', key: 'text', type: 'textArea', col: 2, autoSize: {minRows: 5}}
    ]
}