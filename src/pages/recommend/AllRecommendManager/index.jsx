import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getRecommendByCondition} from "../../../api";
import RecommendVideo from "./RecommendVideo";
import DeleteRecommend from "./DeleteRecommend";

export default class AllRecommendManager extends Component {
    columnsConfig = [
        {title: 'av号', key: 'av', width: 90, type: 'search'},
        {title: '作品名', key: 'name', width: 300, ellipsis: true},
        {title: '推荐人', key: 'operator', width: 100, ellipsis: true},
        {title: 'UP主', key: 'owner', width: 100, ellipsis: true},
        {title: '原作者', key: 'externalOwner', width: 100, ellipsis: true},
        {title: '投稿时间', key: 'pubTime', width: 180},
        {title: '视频类型', key: 'type', width: 130, ellipsis: true},
        {title: '推荐语', key: 'text', width: 300, ellipsis: true},
        {title: '状态', key: 'status', width: 70, type: 'choose', chooseMap: 'statusList'},
        {title: '推荐时间', key: 'createTime', width: 180},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows } = state
        const selectedRow = selectedRows[0] || {};
        return (
            <>
                <RecommendVideo onSuccess={handleUpdated}/>
                <DeleteRecommend selectedRow={selectedRow} onSuccess={handleUpdated}/>
            </>
        )
    }
    handleResourcesInit = () => ({
        statusList: [{text: '正常', value: 0, renderHidden: true}, {text: '废弃', value: -1}],
    })

    render() {
        return (
            <DefaultTable
                defaultPageSize={20}
                defaultFilters={{status: [0]}}
                defaultSorter={{field: 'id', order: 'descend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getRecommendByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

}