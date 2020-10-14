import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {recoveryVideo} from "../../api";
import {isNull} from "../../utils/htmlUtils";

export default class RecoveryVideo extends Component {
    recoveryVideo = async () => {
        await recoveryVideo(this.props.selectedRow.av).then(() => this.props.onSuccess())
    }

    render() {
        const { av, name, isDelete } = this.props.selectedRow;
        const isNotDelete = !isDelete
        return (
            <SimpleModalButton buttonStyle={{marginLeft: "8px"}} title="恢复视频" onOk={this.recoveryVideo} disabled={isNull(isDelete) || isNotDelete}>
                <span>
                    确定恢复视频：
                    <a href={"https://www.bilibili.com/video/av" + av} target="_Blank" rel="noopener noreferrer">
                        {name}
                    </a>
                </span>
            </SimpleModalButton>
        )
    }
}