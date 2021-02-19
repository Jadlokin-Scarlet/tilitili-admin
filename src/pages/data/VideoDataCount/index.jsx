import React, {useEffect, useState} from "react";
import {getVideoDataCount} from "../../../api";
import {If, ignoreError, isNotEmpty} from "../../../utils/HtmlUtils";
import {Card} from "antd";
import VideoDataAddCount from "./VideoDataAddCount";

export default function VideoDataCount() {
    const [data, setData] = useState({});
    const {videoDataAddCountList} = data;

    const reqData = () => {
        setData({});
        getVideoDataCount().then(setData).catch(ignoreError);
    }

    useEffect(() => {
        reqData();
    }, [])

    return <Card>
        {If(isNotEmpty(videoDataAddCountList)).then(() => (
            <VideoDataAddCount videoDataAddCountList={videoDataAddCountList || []}/>
        )).endIf()}
    </Card>
}