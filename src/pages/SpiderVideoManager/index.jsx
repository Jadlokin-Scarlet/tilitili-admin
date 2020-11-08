import React, {Component} from "react";
import DefaultTable from "../../components/default-table";
import {getTaskByCondition} from "../../api";
import SpiderVideo from "./SpiderVideo";
import {Tag} from "antd";

export default class SpiderVideoManager extends Component {
    columns = [
        {title: 'id', key: 'id', width: 90, type: 'search'},
        {title: 'av号', key: 'av', width: 90, type: 'search'},
        {title: '状态', key: 'status', width: 100, type: 'choose', chooseMap: 'TaskStatusResource', afterRender: (status) => {
                if (['爬取成功'].includes(status)) {
                    return <Tag color="green">{status}</Tag>
                }
                if (['爬取失败'].includes(status)) {
                    return <Tag color="red">{status}</Tag>
                }
                if (['待爬取', '爬取中'].includes(status)) {
                    return <Tag color="orange">{status}</Tag>
                }
                if (['爬取超时', '废弃'].includes(status)) {
                    return <Tag>{status}</Tag>
                }
        }},
        {title: '类型', key: 'type', width: 140, type: 'choose', chooseMap: 'TaskTypeResource'},
        {title: '备注', key: 'remark', width: 200, ellipsis: true},
        {title: '创建时间', key: 'createTime', width: 200, type: 'order'},
        {title: '更新时间', key: 'updateTime', width: 200, type: 'order'},
        {title: '批爬id', key: 'batchId', width: 90, type: 'search'},
    ];

    handleTitleInit = (props, handleUpdated) => {
        const { TaskReasonResource } = props.resources;
        return (() =>
            <SpiderVideo onSuccess={handleUpdated} TaskReasonResource={TaskReasonResource}/>
        )
    }

    render() {
        return (
            <DefaultTable
                needResourcesList={['TaskTypeResource', 'TaskStatusResource', 'TaskReasonResource']}
                onReqData={getTaskByCondition}
                columns={this.columns}
                onTitleInit={this.handleTitleInit}
                defaultFilters={{type: 0}}
                defaultSorter={{field: 'createTime', order: 'descend'}}
            />
        )
    }

}