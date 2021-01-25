import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmpty} from "../../../utils/HtmlUtils";
import {updateTaskStatus} from "../../../api";

const formConfig = [
    {key: 'idList', hidden: true},
    {key: 'status', label: '状态', type: 'select', resource: 'TaskStatusResource'},
]

export default class UpdateStatus extends Component {
    render() {
        const selectedRow = {};
        selectedRow.idList = this.props.selectedRows.map(item => item.id).join(",")
        return <>
            <DefaultModalButton
                {...this.props}
                title={`编辑任务条数: ${this.props.selectedRows.length}`}
                value='编辑'
                selectedRow={selectedRow}
                formConfig={formConfig}
                disabled={isEmpty(this.props.selectedRows)}
                updateApi={updateTaskStatus}
            />
        </>
    }
}