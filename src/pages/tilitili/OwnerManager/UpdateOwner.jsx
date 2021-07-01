import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {updateOwner} from "../../../api";

const formConfig = [
    {key: 'uid', hidden: true},
    {key: 'name', submit: false},
    {label: '状态', key: 'status', type: 'select', resource: 'statusList'},
]

export default class UpdateOwner extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title={`编辑作者状态: ${this.props.selectedRow.name}`}
                value='编辑作者状态'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={updateOwner}
            />
        )
    }
}