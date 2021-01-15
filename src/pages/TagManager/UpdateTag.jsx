import React, {Component} from "react";
import DefaultModalButton from "../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../utils/HtmlUtils";
import {updateTag} from "../../api";

const formConfig = [
    {key: 'id', hidden: true},
    {key: 'name', submit: false},
    {label: '标签类型', key: 'type', type: 'select', resource: 'TagTypeResource'},
]

export default class UpdateTag extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title={`编辑标签: ${this.props.selectedRow.name}`}
                value='编辑标签'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={updateTag}
            />
        )
    }
}