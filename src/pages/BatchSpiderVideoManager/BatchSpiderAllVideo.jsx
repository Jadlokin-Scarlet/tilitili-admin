import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {emptyFunc } from "../../utils/HtmlUtils";
import {batchSpiderAllVideo} from "../../api";

export default class BatchSpiderAllVideo extends Component{

    batchSpiderAllVideo = async () => {
        const { onUpdated=emptyFunc } = this.props;
        batchSpiderAllVideo().then()
        onUpdated()
    }

    render() {
        return (
            <SimpleModalButton title="重爬所有视频" onOk={this.batchSpiderAllVideo}>
                <span>确定？</span>
            </SimpleModalButton>
        )
    }

}