import React, {Component} from "react";
import {CopyToClipboard} from 'react-copy-to-clipboard';
import {Button} from "antd";
import {isEmpty} from "../../../utils/HtmlUtils";

export default class CopyBvToClipboard extends Component{

    render() {
        const text =
`主榜30-21：
${this.props.list.filter(item => 20 < item.rank && item.rank <= 30).reverse().map(item => `${item.rank}：${item.bv}`).join("\n")}
主榜20-11：
${this.props.list.filter(item => 10 < item.rank && item.rank <= 20).reverse().map(item => `${item.rank}：${item.bv}`).join("\n")}
主榜10-4：
${this.props.list.filter(item => 3 < item.rank && item.rank <= 10).reverse().map(item => `${item.rank}：${item.bv}`).join("\n")}
主榜前三：
${this.props.list.filter(item => 0 < item.rank && item.rank <= 3).reverse().map(item => `${item.rank}：${item.bv}`).join("\n")}
`
        return (
            <CopyToClipboard text={text}>
                <Button style={{ marginLeft: 8 }} disabled={isEmpty(this.props.list) || this.props.list[0].rank === 0}>复制导航评论</Button>
            </CopyToClipboard>
        )
    }

}