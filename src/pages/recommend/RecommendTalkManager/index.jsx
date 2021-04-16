import React, {Component} from "react";
import {getRecommendTalkByCondition} from "../../../api";
import DefaultTable from "../../../components/default-table";
import UpdateRecommendTalk from "./UpdateRecommendTalk";
import BatchUpdateRecommendTalk from "./BatchUpdateRecommendTalk";

export default class RecommendTalkManager extends Component {
    columnsConfig = [
        {title: '模块', key: 'type', width: 70, type: 'choose', chooseMap: 'typeList'},
        {title: '主持人', key: 'speaker', width: 70, type: 'chooseRender', chooseMap: 'speakerList'},
        {title: '台本', key: 'text', width: 500},
        {title: '表情', key: 'expression', width: 70},
        {title: '所在刊', key: 'issueId', width: 150, ellipsis: true, type: 'chooseInput', chooseMap: 'recommendIssueResource'},
        {title: '状态', key: 'status', width: 70, type: 'choose', chooseMap: 'statusList'},
    ];

    handleResourcesInit = () => ({
        typeList: [{text: 'OP', value: 1}, {text: 'ED', value: 3}],
        statusList: [{text: '正常', value: 0, renderHidden: true}, {text: '废弃', value: -1}],
        speakerList: [{text: '灵梦', value: '灵梦', tag: 'red'}, {text: '早苗', value: '早苗', tag: 'green'}],
    })

    constructor(props) {
        super(props);
        this.state = {
            defaultFilters: {status: [0], type: [1]},
        }
    }

    handleResourceLoaded = (resources, callback) => {
        const { recommendIssueResource } = resources;
        const newIssueId = Math.max(...recommendIssueResource.map(resources => resources.value));
        this.setState({defaultFilters: Object.assign({issueId: [newIssueId]}, this.state.defaultFilters)}, callback)
    }

    handleButtonsInit = (that) => {
        const { state, handleUpdated, handleVisibleChange } = that
        const { selectedRows, resources, visible, data } = state
        const buttonData = data?.data || {};
        const selectedRow = selectedRows[0] || {};
        return (
            <>
                <UpdateRecommendTalk selectedRow={selectedRow} onSuccess={handleUpdated} resources={resources} visible={visible} onVisibleChange={handleVisibleChange}/>
                <BatchUpdateRecommendTalk buttonData={buttonData} onSuccess={handleUpdated} resources={resources}/>
            </>
        )
    }

    render() {
        return (
            <DefaultTable
                needResourcesList={['recommendIssueResource']}
                defaultSorter={{field: 'id', order: 'ascend'}}
                defaultFilters={this.state.defaultFilters}
                defaultPageSize={100}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getRecommendTalkByCondition}
                onResourceLoaded={this.handleResourceLoaded}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }
}