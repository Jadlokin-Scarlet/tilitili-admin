import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {emptyFunc } from "../../utils/HtmlUtils";
import {batchSpiderAllVideoTag} from "../../api";

export default class BatchSpiderAllVideoTag extends Component{

    batchSpiderAllVideoTag = async () => {
        const { onUpdated=emptyFunc } = this.props;
        batchSpiderAllVideoTag().then()
        onUpdated()
    }

    render() {
        return (
            <SimpleModalButton title="重爬所有视频标签" onOk={this.batchSpiderAllVideoTag}>
                <span>确定？</span>
            </SimpleModalButton>
        )
    }

}