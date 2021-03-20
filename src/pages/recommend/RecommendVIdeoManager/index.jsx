import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getRecommendVideoByCondition} from "../../../api";
import AddRecommendVideo from "./AddRecommendVideo";
import UpdateRecommendVideo from "./UpdateRecommendVideo";
import UpdateDeleteRecommendVideo from "./UpdateDeleteRecommendVideo";

export default class RecommendVideoManager extends Component {
    columnsConfig = [
        {title: '刊名', key: 'name', width: 300, editing: true, inputType: 'input'},
        {title: '期数', key: 'issue', width: 100},
        {title: '推荐数', key: 'recommendNumber', width: 100},
        {title: '自荐数', key: 'selfRecommendNumber', width: 100},
        {title: '类型', key: 'type', width: 100, type: 'choose', chooseMap: 'typeList'},
        {title: '状态', key: 'status', width: 100, type: 'choose', chooseMap: 'statusList'},
        {title: '创建时间', key: 'createTime', width: 200, type: 'sorter'},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { resources, selectedRows } = state
        const selectedRow = selectedRows[0] || {};
        return <>
            <UpdateDeleteRecommendVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
            <AddRecommendVideo resources={resources} onSuccess={handleUpdated}/>
            <UpdateRecommendVideo resources={resources} selectedRow={selectedRow} onSuccess={handleUpdated}/>
        </>
    }

    handleResourcesInit = () => ({
        typeList: [{text: '正常', value: 0, renderHidden: true},{text: '特别刊', value: 1},],
        statusList: [{text: '废弃', value: -1, tag: 'no'},{text: '正常', value: 0, renderHidden: true},]
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['recommendIssueResource']}
                defaultPageSize={20}
                defaultFilters={{status: [0]}}
                defaultSorter={{field: 'createTime', order: 'descend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getRecommendVideoByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }


}