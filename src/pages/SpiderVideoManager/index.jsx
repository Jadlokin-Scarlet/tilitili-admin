import React, {Component} from "react";
import DefaultTable from "../../components/default-table";
import {getTaskByCondition} from "../../api";
import SpiderVideo from "./SpiderVideo";
import UpdateStatus from "./UpdateStatus";

export default class SpiderVideoManager extends Component {
    columnsConfig = [
        {title: 'id', key: 'id', width: 90, type: 'search'},
        {title: 'av号', key: 'av', width: 90, type: 'search'},
        {title: '状态', key: 'status', width: 100, type: 'choose', chooseMap: 'TaskStatusResource'},
        {title: '类型', key: 'type', width: 140, type: 'choose', chooseMap: 'TaskTypeResource'},
        {title: '备注', key: 'remark', width: 200, ellipsis: true},
        {title: '创建时间', key: 'createTime', width: 200, type: 'sorter'},
        {title: '更新时间', key: 'updateTime', width: 200, type: 'sorter'},
        {title: '批爬id', key: 'batchId', width: 90, type: 'search'},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { resources, selectedRows } = state
        const { TaskReasonResource } = resources;
        return <>
            <SpiderVideo onSuccess={handleUpdated} TaskReasonResource={TaskReasonResource}/>
            <UpdateStatus onSuccess={handleUpdated} selectedRows={selectedRows} resources={resources}/>
        </>
    }

    handleResourcesInit = () => ({
        TaskStatusResource: [{text: '废弃', value: -1, tag: 'no'},{text: '待爬取', value: 0, tag: 'orange'},{text: '爬取中', value: 1, tag: 'orange'},{text: '爬取成功', value: 2, tag: 'green'},{text: '爬取失败', value: 3, tag: 'red'},{text: '爬取超时', value: 4, tag: 'no'},]
    })

    render() {
        return (
            <DefaultTable
                isMultipleSelect={true}
                needResourcesList={['TaskTypeResource', 'TaskReasonResource']}
                defaultPageSize={20}
                defaultFilters={{type: [0]}}
                defaultSorter={{field: 'createTime', order: 'descend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getTaskByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

}