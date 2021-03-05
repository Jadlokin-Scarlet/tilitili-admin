import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {addRecommendVideo} from "../../../api";
import {isNotNull} from "../../../utils/HtmlUtils";

const formConfig = [
    {label: '类型', key: 'type', type: 'select', resource: 'typeList', value: 0},
    {label: '期数', key: 'issue', type: 'input', disabled: true, groupBy: {type: 0}},
    {label: '刊名', key: 'name', type: 'input'},
]

export default class AddRecommendVideo extends Component {

    render() {
        const {resources} = this.props;
        const {recommendIssueResource=[]} = resources;
        const newIssue = recommendIssueResource.filter(item => isNotNull(item.issue))[0]?.issue + 1;
        const selectedRow = {
            issue: newIssue,
            name: '东方推荐刊 ☯'+newIssue,
        }
        return (
            <DefaultModalButton
                {...this.props}
                title='新增'
                value='新增'
                formConfig={formConfig}
                selectedRow={selectedRow}
                updateApi={addRecommendVideo}
            />
        )
    }

}