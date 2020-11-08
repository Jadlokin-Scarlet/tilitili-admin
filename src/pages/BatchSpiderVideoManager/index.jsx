import React, {Component} from "react";
import DefaultTable from "../../components/default-table";
import {getBatchTaskByCondition} from "../../api";
import TestBatchSpiderVideo from "./TestBatchSpiderVideo";

export default class BatchSpiderVideoManager extends Component{

    columns = [
        {title: 'id', key: 'id', width: 90, type: 'search'},
        {title: '类型', key: 'type', width: 140, type: 'choose', chooseMap: 'TaskTypeResource'},
        {title: '原因', key: 'reason', width: 140, type: 'choose', chooseMap: 'TaskReasonResource'},
        {title: '总任务数', key: 'totalTaskNumber', width: 120, type: 'order'},
        {title: '等待任务数', key: 'waitTaskNumber', width: 120, type: 'order'},
        {title: '成功任务数', key: 'successTaskNumber', width: 120, type: 'order'},
        {title: '失败任务数', key: 'failTaskNumber', width: 120, type: 'order'},
        {title: '备注', key: 'remark', width: 200, ellipsis: true},
        {title: '创建时间', key: 'createTime', width: 200, type: 'order'},
        {title: '更新时间', key: 'updateTime', width: 200, type: 'order'},
    ];

    handleTitleInit = (props, handleUpdated) => {
        return (() =>
                <TestBatchSpiderVideo onSuccess={handleUpdated}/>
        )
    }

    render() {
        return (
            <DefaultTable
                needResourcesList={['TaskTypeResource', 'TaskReasonResource']}
                onReqData={getBatchTaskByCondition}
                columns={this.columns}
                onTitleInit={this.handleTitleInit}
                defaultSorter={{field: 'createTime', order: 'descend'}}
            />
        )
    }

}