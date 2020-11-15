import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {emptyFunc } from "../../utils/HtmlUtils";
import {batchSpiderHiddenVideo} from "../../api";

export default class BatchSpiderHiddenVideo extends Component{

    batchSpiderHiddenVideo = async () => {
        const { onSuccess=emptyFunc } = this.props;
        await batchSpiderHiddenVideo()
        onSuccess()
    }

    render() {
        return (
            <SimpleModalButton title="重爬不可见视频" onOk={this.batchSpiderHiddenVideo}>
                <span>确定？</span>
            </SimpleModalButton>
        )
    }

}