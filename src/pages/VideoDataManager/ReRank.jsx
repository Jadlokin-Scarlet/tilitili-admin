import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {reRank} from "../../api";
import {isNull} from "../../utils/HtmlUtils";

export default class ReRank extends Component{
    reRank = async () => {
        await reRank(this.props.issue).then(() => this.props.onSuccess())
    }
    render() {
        return (
            <SimpleModalButton buttonStyle={{marginLeft: "8px"}} title="重排排名" onOk={this.reRank} disabled={isNull(this.props.issue)}>
                <span>确定重排排名吗?将根据得分修改第{this.props.issue}期的排名。</span>
            </SimpleModalButton>
        )
    }

}