import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommendTalk} from "../../../api";

export default class BatchUpdateRecommendTalk extends Component {
    render() {
        const {resources, buttonData} = this.props;
        const {recommendIssueResource=[]} = resources;
        const name = recommendIssueResource[0]?.text;
        const selectedRow = {op: buttonData.op,ed: buttonData.ed}
        return (
            <DefaultModalButton
                {...this.props}
                col={3}
                title={`当前期： ${name}`}
                value='批量编辑最新期对话'
                formConfig={this.formConfig}
                selectedRow={selectedRow}
                updateApi={updateRecommendTalk}
            />
        )
    }

    formConfig = [
        {label: 'OP对话', key: 'op', type: 'textArea', col: 3, autoSize: {minRows: 5}},
        {label: 'ED对话', key: 'ed', type: 'textArea', col: 3, autoSize: {minRows: 5}},
    ];
}