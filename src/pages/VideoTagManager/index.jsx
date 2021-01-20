import React, {Component} from "react";
import DefaultTable from "../../components/default-table";
import {getVideoTagByCondition} from "../../api";
import {Tag} from "antd";
import SpiderVideo from "./SpiderVideo";

export default class VideoTagManager extends Component {

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { resources } = state
        return <>
            <SpiderVideo resources={resources} onSuccess={handleUpdated}/>
        </>
    }

    handleResourcesInit = () => ({
    })

    tagRender = (tagList) => {
        return tagList.map((tag, index) => (
            <Tag key={index} color={tag.type === 1? "green": "cyan"}>{tag.name}</Tag>
        ))
    }

    render() {
        return (
            <DefaultTable
                needResourcesList={['TaskReasonResource']}
                defaultPageSize={20}
                rowKey={record => record.av}
                columnsConfig={this.columnsConfig}
                getDataApi={getVideoTagByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

    columnsConfig = [
        {title: 'av号', key: 'av', width: 90, type: 'search', href: av => "https://www.bilibili.com/video/av" + av},
        {title: '视频名', key: 'name', width: 300, ellipsis: true},
        {title: 'tag列表', key: 'tag', dataIndex: 'tagList', width: 800, type: 'search', afterRender: this.tagRender},
    ];
}