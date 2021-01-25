import React, {Component} from "react";
import SimpleModalButton from "../../../components/simple-modal-button";
import {emptyFunc } from "../../../utils/HtmlUtils";
import {testBatchSpiderVideo} from "../../../api";

export default class TestBatchSpiderVideo extends Component{

    batchSpiderVideo = async () => {
        const { onSuccess=emptyFunc } = this.props;
        await testBatchSpiderVideo()
        onSuccess()
    }

    render() {
        return (
            <SimpleModalButton title="发起测试批量爬取" onOk={this.batchSpiderVideo}>
                <span>确定？</span>
            </SimpleModalButton>
        )
    }

}