import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getRecommendPoolByCondition} from "../../../api";
import RecommendVideo from "./RecommendVideo";
import DeleteRecommend from "./DeleteRecommend";
import UseRecommend from "./UseRecommend";
import SpiderVideo from "./SpiderVideo";
import UpdateRecommend from "./UpdateRecommend";
import {dateFormat} from "../../../utils/HtmlUtils";

export default class RecommendPoolManager extends Component {
    columnsConfig = [
        {title: 'av号', key: 'av', width: 90, type: 'search', href: av => "https://www.bilibili.com/video/av" + av},
        {title: '作品名', key: 'name', width: 300, ellipsis: true},
        {title: '推荐人', key: 'operator', width: 100, ellipsis: true},
        {title: '推荐语', key: 'text', width: 300, ellipsis: true},
        {title: '选区', key: 'startTime', width: 100, afterRender: dateFormat},
        {title: 'UP主', key: 'owner', width: 100, ellipsis: true},
        {title: '原作者', key: 'externalOwner', width: 100, ellipsis: true},
        {title: '视频类型', key: 'type', width: 130, ellipsis: true},
        {title: '投稿时间', key: 'pubTime', width: 180},
        {title: '推荐时间', key: 'createTime', width: 180},
        {title: '视频状态', key: 'videoStatus', width: 100, type: 'choose', chooseMap: 'videoStatusList'},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows, resources } = state
        const selectedRow = selectedRows[0] || {};
        return (
            <>
                <UpdateRecommend selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <SpiderVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <RecommendVideo onSuccess={handleUpdated}/>
                <DeleteRecommend selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <UseRecommend selectedRow={selectedRow} onSuccess={handleUpdated} resources={resources}/>
            </>
        )
    }
    handleResourcesInit = () => ({
        videoStatusList: [{value: 0, text: '正常', renderHidden: true}, {value: 62002, text: '稿件不可见'}, {value: -404, text: '啥都木有'}],
        // statusList: [{text: '正常', value: 0, renderHidden: true}, {text: '废弃', value: -1}, {text: '已使用', value: 1}],
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['recommendIssueResource']}
                defaultPageSize={20}
                defaultSorter={{field: 'id', order: 'descend'}}
                defaultFilters={{videoStatus: [0]}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getRecommendPoolByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

}