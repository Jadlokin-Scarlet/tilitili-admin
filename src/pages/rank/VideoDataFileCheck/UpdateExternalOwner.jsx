import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {updateExternalOwner} from "../../../api";

const formConfig = [
    {label: '是否搬运', key: 'copyright', type: 'select', resource: 'copyrightList', disabled: true},
    {label: '原作者', key: 'externalOwner', type: 'input'},
]

export default class UpdateExternalOwner extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title={`编辑av: ${this.props.selectedRow.av}`}
                value='编辑原作者'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={updateExternalOwner}
            />
        )
    }
}