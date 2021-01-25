import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {updateStartTime} from "../../../api";

const formConfig = [
    {label: '节选开始点', key: 'startTime', type: 'inputNumber', suffix: '秒'},
    {key: 'av',type: 'bilibiliVideo'},
]

export default class UpdateStartTime extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title={`编辑av: ${this.props.selectedRow.av}`}
                value='编辑节选开始时间'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={updateStartTime}
            />
        )
    }
}