import React, {Component} from "react";
import {CopyToClipboard} from 'react-copy-to-clipboard';
import {Button} from "antd";
import {isEmpty} from "../../../utils/HtmlUtils";

export default class CopyBvToClipboard extends Component{

    getLengthByRank = rank => rank > 20? 10
                            : rank > 10? 20
                            : rank > 3? 30: 40;

    render() {
        const videoList = this.props.list.filter(video => video.rank <= 30 && video.rank > 0).reverse();
        const opLength = 19;
        let start = opLength;
        for (const video of videoList) {
            if (video.rank === 1) {
                start += 8
            }
            video.start = start;
            const length = Math.min(video.isLen? 5: this.getLengthByRank(video.rank), video.duration);
            video.videoLength = length - 2;
            start += length;
        }

        const text = `def text t1_1 { content = "↓点击视频标题直接跳转" fontSize = 4% fontFamily = "微软雅黑" x = -1000% y = -1000% alpha = 1 color = 0xff6600 textShadow = 0 bold = 1 rotateX = 0 rotateY = 0 rotateZ = 0 strokeWidth = 1 strokeColor = 0xffffff anchorX = 1 anchorY = 0 zIndex = 1 } set t1_1 {} ${opLength}s then set t1_1 {x = 45.0% y = 70.0%} 0s then set t1_1 {alpha = 1 x = 83%} 5s then set t1_1 {alpha = 0} 1s set t1_1 {color = 0xff0000} 6s`
        + videoList.map((video, index) =>
            `def button BT${index} { text = "　                                                                                                      \\n\\n\\n\\n" x = -1000% y = -1000% fontSize = 2.5% textColor = 0xFF9900 fillColor = 0xFFffff fillAlpha = 0.2 zIndex = 0 target = av { av = ${video.av} } } set BT${index}{} ${video.start}s then set BT${index}{x = 1% y = 77.5%} 0s then set BT${index}{x = 1% y = 77.5%} ${video.videoLength}s`
        ).reduce((a, b) => a+'\n'+b, "");
        return (
            <CopyToClipboard text={text}>
                <Button style={{ marginLeft: 8 }} disabled={isEmpty(this.props.list) || this.props.list[0].rank === 0}>复制跳转脚本</Button>
            </CopyToClipboard>
        )
    }

}