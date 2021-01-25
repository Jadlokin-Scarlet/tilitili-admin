import React, {Component} from "react";
import SimpleModalButton from "../../../components/simple-modal-button";
import {deleteBatchTag} from "../../../api";
import {isEmptyObject} from "../../../utils/HtmlUtils";

export default class DeleteBatchTask extends Component {
    deleteBatchTag = async () => {
        await deleteBatchTag(this.props.selectedRow.id).then(() => this.props.onSuccess())
    }

    render() {
        return (
            <SimpleModalButton buttonStyle={{marginLeft: "8px"}} title="删除批量任务" onOk={this.deleteBatchTag} disabled={isEmptyObject(this.props.selectedRow)}>
                <span>确定删除该批量爬取任务</span>
            </SimpleModalButton>
        )
    }
}