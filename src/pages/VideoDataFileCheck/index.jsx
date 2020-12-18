import React, {Component} from "react";
import {listVideoDataTxt} from "../../api";
import DefaultTable from "../../components/default-table";
import UpdateStartTime from "./UpdateStartTime";

export default class VideoDataFileCheck extends Component {

    constructor(props) {
        super(props);
        this.state = {
            defaultFilters: {},
        }
    }

    columnsConfig = [
        {title: 'av号', key: 'av', width: 100},
        {title: '标题', key: 'name', width: 200, ellipsis: true},
        {title: '封面', key: 'img', type: 'image'},
        {title: '类型', key: 'type', width: 120},
        {title: '作者', key: 'owner', width: 100},
        {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: 'copyrightList'},
        {title: '发布日期', key: 'pubTime', width: 180},
        {title: '节选开始时间(秒)', key: 'startTime', width: 180},
        {title: '播放量', key: 'view', width: 100},
        {title: '评论', key: 'reply', width: 100},
        {title: '收藏', key: 'favorite', width: 100},
        {title: '硬币', key: 'coin', width: 100},
        {title: '得分', key: 'point', width: 100},
        {title: '排名', key: 'rank', width: 70},
        {title: '历史排名', key: 'hisRank', width: 100},
        {title: '是否长期', key: 'isLen', width: 100, type: 'choose', chooseMap: 'isLenList'},
    ];

    handleResourceLoaded = (resources, callback) => {
        const { videoIssueResource } = resources;
        const newIssue = Math.max(...videoIssueResource.map(resources => resources.value));
        this.setState({defaultFilters: {issue: [newIssue]}}, callback)
    }

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows } = state
        const selectedRow = selectedRows[0] || {};
        return (<>
            <UpdateStartTime selectedRow={selectedRow} onSuccess={handleUpdated}/>
        </>)
    }

    handleResourcesInit = () => ({
        isLenList: [{value: 1, text: '是'}, {value: 0, text: '否'}],
        copyrightList: [{value: false, text: '原创'}, {value: true, text: '搬运'}],
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['videoIssueResource']}
                defaultPageSize={100}
                defaultFilters={this.state.defaultFilters}
                rowKey={record => record.av}
                columnsConfig={this.columnsConfig}
                getDataApi={listVideoDataTxt}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
                onResourceLoaded={this.handleResourceLoaded}
            />
        )
    }

}
