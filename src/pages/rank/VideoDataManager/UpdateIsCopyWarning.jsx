import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {updateIsCopyWarning} from "../../../api";

const formConfig = [
    {key: 'av', hidden: true},
    {label: '是否搬运', key: 'copyright', type: 'select', resource: 'copyrightList', disabled: true},
    {label: '是否疑似搬运', key: 'isCopyWarning', type: 'select', resource: 'isCopyWarningList'},
]

export default class UpdateIsCopyWarning extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title={`编辑av: ${this.props.selectedRow.av}`}
                value='标记为疑似搬运'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={updateIsCopyWarning}
            />
        )
    }
}