import React, {Component} from "react";
import {listVideoDataTxt} from "../../../api";
import DefaultTable from "../../../components/default-table";
import UpdateStartTime from "./UpdateStartTime";
import CopyBvToClipboard from "./CopyBvToClipboard";
import UpdateExternalOwner from "../VideoDataManager/UpdateExternalOwner";
import {dateFormat, isBlank} from "../../../utils/HtmlUtils";
import CheckPoint from "./CheckPoint";
import CopyButtonToClipboard from "./CopyButtonToClipboard";
import ChooseIssue from "./ChooseIssue";

export default class VideoDataFileCheck extends Component {

    constructor(props) {
        super(props);
        this.state = {
            defaultFilters: {},
        }
    }

    handleResourceLoaded = (resources, callback) => {
        const { videoIssueResource } = resources;
        const newIssue = Math.max(...videoIssueResource.map(resources => resources.value));
        this.setState({defaultFilters: {issue: [newIssue]}}, callback)
    }

    handleButtonsInit = (that) => {
        const { state, updateFilters, handleUpdated } = that
        const { selectedRows, data, resources, filters, dataLoading } = state
        const selectedRow = selectedRows[0] || {};
        const list = data.list || []
        return (<>
            <ChooseIssue disabled={dataLoading} onChange={filters => updateFilters(filters, handleUpdated)} filters={filters}/>
            <CopyBvToClipboard list={list}/>
            <CopyButtonToClipboard list={list}/>
            <UpdateStartTime selectedRow={selectedRow} onSuccess={handleUpdated}/>
            <UpdateExternalOwner selectedRow={selectedRow} onSuccess={handleUpdated} resources={resources}/>
        </>)
    }

    handleResourcesInit = () => ({
        isLenList: [{value: true, text: '长期'}, {value: false, text: '非长期', renderHidden: true}],
        copyrightList: [{value: false, text: '原创', renderHidden: true}, {value: true, text: '搬运'}],
        isUpList: [{value: true, text: '↑'}, {value: false, text: '↓'}],
    })

    pointRender = (point, record) => {
        if (record.isPointWarning === true) {
            return <CheckPoint record={record}/>
        }else {
            return point;
        }
    }
    // 主榜中还没填原作者的搬运视频标红
    handleRowClassName = (row, index) => row.rank <= 30 && row.copyright && isBlank(row.externalOwner)? " row-error ": "";

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
                onRowClassName={this.handleRowClassName}
            />
        )
    }

    columnsConfig = [
        {title: 'av槽', key: 'avStr', width: 120, href: (value, row) => "https://www.bilibili.com/video/av" + row.av},
        {title: '标题槽', key: 'nameStr', width: 200, ellipsis: true},
        {title: '封面', key: 'img', type: 'image', href: (value, row) => "https://www.bilibili.com/video/av" + row.av},
        {title: '类型槽', key: 'typeStr', width: 120},
        {title: '作者槽', key: 'ownerStr', width: 125, ellipsis: true},
        {title: '副作者槽', key: 'subOwnerStr', width: 150, ellipsis: true},
        // {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: 'copyrightList'},
        {title: '发布日期槽', key: 'pubTimeStr', width: 180},
        {title: '节选', key: 'startTime', width: 100, afterRender: dateFormat},
        {title: '播放量槽', key: 'viewStr', width: 120},
        {title: '评论槽', key: 'replyStr', width: 100},
        {title: '收藏槽', key: 'favoriteStr', width: 100},
        {title: '硬币槽', key: 'coinStr', width: 110},
        {title: '得分槽', key: 'pointStr', width: 100, afterRender: this.pointRender},
        {title: '排名槽', key: 'rankStr', width: 65},
        {title: '历史排名槽', key: 'hisRankStr', width: 90, afterRender: value => value === 0? '': value},
        {title: '是否提升排名', key: 'isUp', width: 110, type: 'chooseRender', chooseMap: 'isUpList'},
        {title: '是否长期槽', key: 'isLen', width: 90, type: 'chooseRender', chooseMap: 'isLenList'},
    ];

}
