import React, {useEffect, useState} from "react";
import {getResources, getVideoInfoCount} from "../../../api";
import {checkResp, For, ignoreError} from "../../../utils/HtmlUtils";
import NewVideoCount from "./NewVideoCount";
import {Button, Card, Form, Row, Select} from "antd";
import {converseToForm} from "../../../components/default-modal-button/DefaultModalButton";

const formConfig = [
    {label: '时间区间', key: 'time', type: 'select', resource: 'timeList'},
    {label: '分区', key: 'videoType', type: 'select', resource: 'videoTypeResource'},
]

export default function VideoInfoCount(props) {

    const [data, setData] = useState({});
    const [params, setParams] = useState({
        time: 14,
        videoType: '全部'
    })
    // const [time, setTime] = useState(14);
    // const [videoType, setVideoType] = useState('全部');
    const [resources, setResources] = useState({});

    resources.timeList = [{value: 7, text: '一周'},{value: 14, text: '两周'},{value: 30, text: '三十天'},{value: 60, text: '六十天'},]

    const {time, videoType} = params

    const reqData = (time, type) => {
        setData({});
        getVideoInfoCount({time, type}).then(setData).catch(ignoreError);
    }

    useEffect(() => {
        reqData(time, videoType);
    }, [time, videoType])

    useEffect(() => {
        const needResourcesList = ['videoTypeResource'];
        getResources({needResourcesList}).then(checkResp).then(setResources).catch(ignoreError);
    }, []);

    const handleChange = filters => {
        setParams(Object.assign(params, filters));
    }

    const handleRefresh = () => {
        reqData(time, videoType);
    }

    return (
        <Card>
            {converseToForm({formConfig,params, resources, col:2, onChange:handleChange})}
            <Button type="primary" onClick={handleRefresh} className="margin-left-8 margin-bottom-8">刷新</Button>
            <NewVideoCount newVideoCountList={data?.newVideoCountList || []}/>
        </Card>
    )

}