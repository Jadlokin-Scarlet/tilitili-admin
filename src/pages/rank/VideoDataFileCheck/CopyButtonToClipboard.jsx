import React, {Component} from "react";
import {CopyToClipboard} from 'react-copy-to-clipboard';
import {Button} from "antd";
import {isEmpty} from "../../../utils/HtmlUtils";

export default class CopyBvToClipboard extends Component{

    getLengthByRank = rank => rank > 20? 10
                            : rank > 10? 20
                            : rank > 3? 30
                            : rank > 1? 40: 45

    render() {
        const videoList = this.props.list.filter(video => video.rank <= 30 && video.rank > 0).reverse();
        let start = 18;
        for (const video of videoList) {
            video.start = start;
            const length = video.copyright? 5: this.getLengthByRank(video.rank);
            start += Math.min(length, video.duration);
        }
        const text = videoList.map((video, index) =>
            `def button b${index} { text = "点我跳转" x = -1000% y = -1000% fontSize = 5% textColor = 0xffffff fillColor = 0xFF9100 fillAlpha = 0.8 zIndex = 1 target = av {av = ${video.av}}} `
                + `set b${index}{} ${video.start + 5}s `
                + `then set b${index} {x = 50% y = 80%} 0s `
                + `then set b${index}{x = 50% y = 80%} 5s `
        ).reduce((a, b) => a+'\n'+b, "");
        return (
            <CopyToClipboard text={text}>
                <Button style={{ marginLeft: 8 }} disabled={isEmpty(this.props.list) || this.props.list[0].rank === 0}>复制跳转脚本</Button>
            </CopyToClipboard>
        )
    }

}