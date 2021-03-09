import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommendVideo} from "../../../api";
import {isEmptyObject, isNotNull} from "../../../utils/HtmlUtils";

const formConfig = [
    {key: 'id', hidden: true},
    {label: '刊名', key: 'name', type: 'input', disable: true},
    {label: '类型', key: 'type', type: 'select', resource: 'typeList'},
    {label: '期数', key: 'issue', type: 'input', disabled: true, groupBy: {type: 0}},
]

export default class UpdateRecommendVideo extends Component {
    render() {
        const {resources, selectedRow} = this.props;
        const {recommendIssueResource=[]} = resources;
        selectedRow.issue = recommendIssueResource.filter(item => isNotNull(item.issue))[0]?.issue + 1;
        return (
            <DefaultModalButton
                {...this.props}
                title='编辑'
                value='编辑'
                disabled={isEmptyObject(this.props.selectedRow)}
                formConfig={formConfig}
                updateApi={updateRecommendVideo}
            />
        )
    }
}