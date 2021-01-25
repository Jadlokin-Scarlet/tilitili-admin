import React, {Component} from "react";
import SimpleModalButton from "../../../components/simple-modal-button";
import {Form, Input, message, Select} from "antd";
import {emptyFunc, For, If, isBlank, isNotNull} from "../../../utils/HtmlUtils";
import {bvToAv} from "../../../utils/BilibiliUtil";
import {spiderVideo} from "../../../api";

export default class SpiderVideo extends Component{
    constructor(props) {
        super(props);
        this.state = {
            choose: "url",
            av: null,
            bv: null,
            url: null,
            reason: null,
        }
    }

    spiderVideo = async () => {
        const { onSuccess=emptyFunc } = this.props;
        let { choose, av, bv, url, reason } = this.state;

        if (choose === "av" && isNotNull(av)) {
            if (isNaN(Number(av))) {
                message.error("识别失败");
                return;
            }
            av = Number.parseInt(av);
        }

        if (choose === "bv" && isNotNull(bv)) {
            if (bv.includes("/")) {
                message.error("识别失败");
                return;
            }
            av = bvToAv(bv)
        }

        if (choose === "url" && isNotNull(url) && url.includes("BV")) {
            if (!url.includes("/video/")) {
                message.error("识别失败");
                return;
            }
            bv = url.split("/video/")[1];
            if (bv.includes("/")) {
                bv = bv.split("/")[0];
            }
            av = bvToAv(bv)
        }

        if (choose === "url" && isNotNull(url) && url.includes("av")) {
            if (!url.includes("/video/")) {
                message.error("识别失败");
                return;
            }
            av = url.split("av")[1]
            if (av.includes("/")) {
                av = av.split("/")[0];
            }
        }

        if (isBlank(av)) {
            message.error("识别失败");
            return;
        }

        await spiderVideo({av, reason})
        onSuccess()
    }

    handleShowModal = () => {
        this.setState({
            choose: "url",
            av: null,
            bv: null,
            url: null,
            reason: 6,
        })
    }

    render() {
        const { choose, av, bv, url, reason } = this.state;
        const { TaskReasonResource } = this.props.resources;
        return (
            <SimpleModalButton buttonText="发起自定义爬取" onShowModal={this.handleShowModal} onOk={this.spiderVideo}>
                <Form labelCol={{ span: 6 }} wrapperCol={{ span: 12 }} layout="horizontal">
                    <Form.Item label="输入方式">
                        <Select value={choose} onChange={choose => this.setState({choose})}>
                            <Select.Option value="av">av</Select.Option>
                            <Select.Option value="bv">bv</Select.Option>
                            <Select.Option value="url">url</Select.Option>
                        </Select>
                    </Form.Item>
                    {If (choose === "av").then(() => (
                        <Form.Item label="av号">
                            <Input value={av} onChange={event => this.setState({av: event.target.value})}/>
                        </Form.Item>
                    )).elseIf(choose === "bv").then(() => (
                        <Form.Item label="bv号">
                            <Input value={bv} onChange={event => this.setState({bv: event.target.value})}/>
                        </Form.Item>
                    )).else(() => (
                        <Form.Item label="链接（不支持短链）">
                            <Input value={url} onChange={event => this.setState({url: event.target.value})}/>
                        </Form.Item>
                    ))}
                    <Form.Item label="原因">
                        <Select value={reason} disabled>
                            {For(TaskReasonResource).then((taskReason, index) => (
                                <Select.Option key={index} value={taskReason.value}>{taskReason.text}</Select.Option>
                            ))}
                        </Select>
                    </Form.Item>
                </Form>
            </SimpleModalButton>
        )
    }

}