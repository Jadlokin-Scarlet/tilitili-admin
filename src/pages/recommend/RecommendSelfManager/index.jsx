import React, {Component} from "react";
import {dateFormat} from "../../../utils/HtmlUtils";
import DefaultTable from "../../../components/default-table";
import {getRecommendSelfByCondition} from "../../../api";
import SpiderVideo from "./SpiderVideo";
import RecommendVideoToNow from "./RecommendVideoToNow";
import UnUseRecommend from "./UnUseRecommend";
import UpdateRecommend from "./UpdateRecommend";

export default class RecommendSelfManager extends Component {
    constructor(props) {
        super(props);
        this.state = {
            defaultFilters: {},
        }
    }
    columnsConfig = [
        {title: 'av号', key: 'av', width: 90, type: 'search', href: av => "https://www.bilibili.com/video/av" + av},
        {title: 'BV号', key: 'bv', width: 140, type: 'search'},
        {title: '所在刊', key: 'issueId', width: 150, ellipsis: true, type: 'chooseInput', chooseMap: 'recommendIssueResource'},
        {title: '作品名', key: 'name', width: 300, ellipsis: true},
        {title: '推荐人', key: 'operator', width: 100, type: 'search', ellipsis: true},
        {title: 'UP主', key: 'owner', width: 100, ellipsis: true},
        {title: '原作者', key: 'externalOwner', width: 100, ellipsis: true},
        {title: '投稿时间', key: 'pubTime', width: 180},
        {title: '视频类型', key: 'videoType', width: 130, ellipsis: true},
        {title: '推荐语', key: 'text', width: 300, ellipsis: true},
        {title: '选区开始', key: 'startTime', width: 100, afterRender: dateFormat},
        {title: '选区结束', key: 'endTime', width: 100, afterRender: dateFormat},
        {title: '排序值', key: 'sortNum', width: 65},
        {title: '推荐时间', key: 'createTime', width: 180},
    ];

    handleResourceLoaded = (resources, callback) => {
        const { recommendIssueResource=[] } = resources;
        const newIssue = Math.max(...recommendIssueResource.map(resources => resources.value));
        this.setState({defaultFilters: Object.assign({issueId: [newIssue]}, this.state.defaultFilters)}, callback)
    }

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows, resources } = state
        const selectedRow = selectedRows[0] || {};
        return (
            <>
                <UpdateRecommend selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <SpiderVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <RecommendVideoToNow {...this.props} resources={resources} onSuccess={handleUpdated}/>
                <UnUseRecommend selectedRow={selectedRow} onSuccess={handleUpdated}/>
            </>
        )
    }
    handleResourcesInit = () => ({
        // statusList: [{text: '正常', value: 0, renderHidden: true}, {text: '废弃', value: -1}, {text: '已使用', value: 1}],
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['recommendIssueResource']}
                defaultFilters={this.state.defaultFilters}
                defaultPageSize={20}
                defaultSorter={{field: 'sortNum', order: 'descend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getRecommendSelfByCondition}
                onResourceLoaded={this.handleResourceLoaded}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }
}