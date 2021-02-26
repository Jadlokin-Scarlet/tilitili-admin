import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getUseRecommendByCondition} from "../../../api";
import SpiderVideo from "./SpiderVideo";

export default class UseRecommendManager extends Component {
    columnsConfig = [
        {title: 'av号', key: 'av', width: 90, type: 'search', href: av => "https://www.bilibili.com/video/av" + av},
        {title: '期数', key: 'issue', width: 90, type: 'choose', chooseMap: 'RecommendIssueResource'},
        {title: '作品名', key: 'name', width: 300, ellipsis: true},
        {title: '推荐人', key: 'operator', width: 100, ellipsis: true},
        {title: 'UP主', key: 'owner', width: 100, ellipsis: true},
        {title: '原作者', key: 'externalOwner', width: 100, ellipsis: true},
        {title: '投稿时间', key: 'pubTime', width: 180},
        {title: '视频类型', key: 'type', width: 130, ellipsis: true},
        {title: '推荐语', key: 'text', width: 300, ellipsis: true},
        {title: '推荐时间', key: 'createTime', width: 180},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows } = state
        const selectedRow = selectedRows[0] || {};
        return (
            <>
                <SpiderVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
            </>
        )
    }
    handleResourcesInit = () => ({
        // statusList: [{text: '正常', value: 0, renderHidden: true}, {text: '废弃', value: -1}, {text: '已使用', value: 1}],
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['RecommendIssueResource']}
                defaultPageSize={20}
                defaultSorter={{field: 'id', order: 'descend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getUseRecommendByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }
}