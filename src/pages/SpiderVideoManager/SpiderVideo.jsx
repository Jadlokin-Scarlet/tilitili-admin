import React, {Component} from "react";
import SimpleModalButton from "../../components/simple-modal-button";
import {Form, Input, message, Typography} from "antd";
import {emptyFunc, If, isBlank, isNotBlank, isNotNull, isNull} from "../../utils/HtmlUtils";
import {bvToAv} from "../../utils/BilibiliUtil";
import {spiderVideo} from "../../api";

export default class SpiderVideo extends Component{
    constructor(props) {
        super(props);
        this.state = {
            choose: null,
            av: "",
            bv: "",
            url: "",
        }
    }

    spiderVideo = async () => {
        const { onSuccess=emptyFunc } = this.props;
        let { av, bv, url } = this.state;

        av = If (isNotBlank(av)).then(() =>
            av
        ).elseIf(isNotBlank(bv)).then(() =>
            bvToAv(bv)
        ).elseIf(url.includes("BV")).then(() =>
            url.split("/video/")[1]
        ).elseIf(url.includes("av")).then(() =>
            url.split("av")[1]
        ).endIf()

        if (isBlank(av)) {
            message.error("参数有误");
            return;
        }

        await spiderVideo(av)
        onSuccess()
    }

    render() {
        const { choose, av, bv, url } = this.state;
        return (
            <SimpleModalButton buttonText="发起自定义爬取" title="选择输入以下之一发起自定义爬取" buttonStyle={{marginLeft: "8px"}} onOk={this.spiderVideo}>
                <Form labelCol={{ span: 6 }} wrapperCol={{ span: 12 }} layout="horizontal">
                    <Form.Item label="av">
                        <Input disabled={isNotNull(choose) && choose !== "av"} value={av} onChange={event => this.setState({av: event.target.value, choose: isNotBlank(event.target.value)? "av": null})}/>
                    </Form.Item>
                    <Form.Item label="bv">
                        <Input disabled={isNotNull(choose) && choose !== "bv"} value={bv} onChange={event => this.setState({bv: event.target.value, choose: isNotBlank(event.target.value)? "bv": null})}/>
                    </Form.Item>
                    <Form.Item label="链接(不支持短链)">
                        <Input disabled={isNotNull(choose) && choose !== "url"} value={url} onChange={event => this.setState({url: event.target.value, choose: isNotBlank(event.target.value)? "url": null})}/>
                    </Form.Item>
                </Form>
            </SimpleModalButton>
        )
    }

}