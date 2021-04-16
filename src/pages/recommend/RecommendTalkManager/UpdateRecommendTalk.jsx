import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommendTalk} from "../../../api";
import {isEmptyObject} from "../../../utils/HtmlUtils";

const formConfig = [
    {key: 'id', hidden: true},
    {label: '主持人', key: 'speaker', type: 'select', resource: 'speakerList'},
    {label: '表情', key: 'expression', type: 'input'},
    {label: '状态', key: 'status', type: 'select', resource: 'statusList'},
    {label: '台本', key: 'text', type: 'textArea', autoSize: true, col: 3},
]

export default class UpdateRecommendTalk extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                col={3}
                title='编辑'
                value='编辑'
                disabled={isEmptyObject(this.props.selectedRow)}
                formConfig={formConfig}
                updateApi={updateRecommendTalk}
            />
        )
    }
}