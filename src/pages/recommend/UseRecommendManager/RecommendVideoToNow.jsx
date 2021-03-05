import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {addRecommendToNow} from "../../../api";

const formConfig = [
    {key: 'issue', hidden: true},
    {label: 'av*', key: 'av', type: 'input', groupBy: {bv: null}},
    {label: 'bv*', key: 'bv', type: 'input', groupBy: {av: null}},
    {label: '开始展示时间', key: 'startTime', type: 'input', suffix: '秒'},
    {label: '原作者', key: 'externalOwner', type: 'input'},
    {label: '推荐语*', key: 'text', type: 'textArea', col: 2, autoSize: {minRows: 5}},
];

export default class RecommendVideoToNow extends Component {
    render() {
        const {resources} = this.props;
        const {recommendIssueResource=[]} = resources;
        const issue = recommendIssueResource[0]?.issue;
        const name = recommendIssueResource[0]?.text;
        const selectedRow = {issue}
        return (
            <DefaultModalButton
                {...this.props}
                col={2}
                title={`推荐视频至 ${name}`}
                value='推荐视频'
                formConfig={formConfig}
                selectedRow={selectedRow}
                updateApi={addRecommendToNow}
            />
        )
    }
}