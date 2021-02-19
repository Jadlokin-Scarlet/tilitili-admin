import React, {useEffect, useState} from "react";
import {Card} from "antd";
import {getTagCount} from "../../../api";
import {If, ignoreError, isNotEmpty} from "../../../utils/HtmlUtils";
import TopTagCount from "./TopTagCount";

export default function TagCount() {
    const [data, setData] = useState({});
    const {topTagList} = data;

    const reqData = () => {
        setData({});
        getTagCount({pageSize: 100}).then(setData).catch(ignoreError);
    }

    useEffect(() => {
        reqData();
    }, [])

    return <Card>
        {If(isNotEmpty(topTagList)).then(() => (
            <TopTagCount topTagList={topTagList || []}/>
        )).endIf()}
    </Card>
}