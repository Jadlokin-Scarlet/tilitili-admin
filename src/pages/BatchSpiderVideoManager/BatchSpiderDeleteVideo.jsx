import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {emptyFunc } from "../../utils/HtmlUtils";
import {batchSpiderDeleteVideo} from "../../api";

export default class BatchSpiderDeleteVideo extends Component{

    batchSpiderDeleteVideo = async () => {
        const { onSuccess=emptyFunc } = this.props;
        await batchSpiderDeleteVideo()
        onSuccess()
    }

    render() {
        return (
            <SimpleModalButton title="重爬已删除视频" onOk={this.batchSpiderVideo}>
                <span>确定？</span>
            </SimpleModalButton>
        )
    }

}