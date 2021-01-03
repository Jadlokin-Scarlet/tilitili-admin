import React, {Component} from "react";
import {listVideoDataTxt} from "../../api";
import DefaultTable from "../../components/default-table";
import UpdateStartTime from "./UpdateStartTime";
import CopyToClipboard from "./CopyToClipboard";

export default class VideoDataFileCheck extends Component {

    constructor(props) {
        super(props);
        this.state = {
            defaultFilters: {},
        }
    }

    columnsConfig = [
        {title: 'av号', key: 'av', width: 110},
        {title: '标题', key: 'name', width: 200, ellipsis: true},
        {title: '封面', key: 'img', type: 'image', href: row => "https://www.bilibili.com/video/av" + row.av},
        {title: '类型', key: 'type', width: 120},
        {title: '作者', key: 'owner', width: 125, ellipsis: true},
        {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: 'copyrightList'},
        {title: '发布日期', key: 'pubTime', width: 110},
        {title: '节选开始时间(秒)', key: 'startTime', width: 160},
        {title: '播放量', key: 'view', width: 100},
        {title: '评论', key: 'reply', width: 90},
        {title: '收藏', key: 'favorite', width: 90},
        {title: '硬币', key: 'coin', width: 90},
        {title: '得分', key: 'point', width: 90},
        {title: '排名', key: 'rank', width: 65},
        {title: '历史排名', key: 'hisRank', width: 90},
        {title: '是否长期', key: 'isLen', width: 90, type: 'chooseRender', chooseMap: 'isLenList'},
    ];

    handleResourceLoaded = (resources, callback) => {
        const { videoIssueResource } = resources;
        const newIssue = Math.max(...videoIssueResource.map(resources => resources.value));
        this.setState({defaultFilters: {issue: [newIssue]}}, callback)
    }

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows, data } = state
        const selectedRow = selectedRows[0] || {};
        const list = data.list || []
        return (<>
            <UpdateStartTime selectedRow={selectedRow} onSuccess={handleUpdated}/>
            <CopyToClipboard list={list}/>
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
