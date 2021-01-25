import React, {Component} from "react";
import SimpleModalButton from "../../../components/simple-modal-button";
import {deleteVideo} from "../../../api";

export default class DeleteVideo extends Component {
    deleteVideo = async () => {
        await deleteVideo(this.props.selectedRow.av).then(() => this.props.onSuccess())
    }

    render() {
        const { av, name, isDelete = true} = this.props.selectedRow;
        return (
            <SimpleModalButton buttonStyle={{marginLeft: "8px"}} title="删除视频" onOk={this.deleteVideo} disabled={isDelete}>
                <span>
                    确定删除视频：
                    <a href={"https://www.bilibili.com/video/av" + av} target="_Blank" rel="noopener noreferrer">{name}</a>
                </span>
            </SimpleModalButton>
        )
    }
}