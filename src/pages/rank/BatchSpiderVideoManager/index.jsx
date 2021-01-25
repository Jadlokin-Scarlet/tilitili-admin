import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getBatchTaskByCondition} from "../../../api";
import TestBatchSpiderVideo from "./TestBatchSpiderVideo";
import BatchSpiderAllVideo from "./BatchSpiderAllVideo";
import BatchSpiderAllVideoTag from "./BatchSpiderAllVideoTag";
import DeleteBatchTask from "./DeleteBatchTask";

export default class BatchSpiderVideoManager extends Component{

    columnsConfig = [
        {title: 'id', key: 'id', width: 90, type: 'search'},
        {title: '类型', key: 'type', width: 140, type: 'choose', chooseMap: 'TaskTypeResource'},
        {title: '原因', key: 'reason', width: 200, type: 'choose', chooseMap: 'TaskReasonResource'},
        {title: '总任务数', key: 'totalTaskNumber', width: 120, type: 'sorter'},
        {title: '等待任务数', key: 'waitTaskNumber', width: 120, type: 'sorter'},
        {title: '成功任务数', key: 'successTaskNumber', width: 120, type: 'sorter'},
        {title: '失败任务数', key: 'failTaskNumber', width: 120, type: 'sorter'},
        {title: '备注', key: 'remark', width: 200, ellipsis: true},
        {title: '创建时间', key: 'createTime', width: 200, type: 'sorter'},
        {title: '更新时间', key: 'updateTime', width: 200, type: 'sorter'},
    ];

    handleButtonsInit = (that) => {
        const { handleUpdated, state } = that
        const { selectedRows } = state;
        const selectedRow = selectedRows[0] || {};
        return (
            <>
                <DeleteBatchTask selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <TestBatchSpiderVideo onSuccess={handleUpdated}/>
                <BatchSpiderAllVideo onUpdated={handleUpdated}/>
                <BatchSpiderAllVideoTag onUpdated={handleUpdated}/>
            </>
        )
    }

    render() {
        return (
            <DefaultTable
                needResourcesList={['TaskTypeResource', 'TaskReasonResource']}
                defaultPageSize={20}
                defaultFilters={{type: [1]}}
                defaultSorter={{field: 'createTime', order: 'descend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getBatchTaskByCondition}
                onButtonsInit={this.handleButtonsInit}
            />
        )
    }

}