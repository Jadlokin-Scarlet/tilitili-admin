import React, {Component} from "react";
import {Button} from "antd";
import {downloadDataTxtUrl, getVideoDataByCondition} from "../../../api";
import {
    dateFormat,
    isNull
} from "../../../utils/HtmlUtils";
import ReRank from "./ReRank";
import DefaultTable from "../../../components/default-table";
import UpdateStartTime from "../VideoDataFileCheck/UpdateStartTime";
import UpdateExternalOwner from "./UpdateExternalOwner";
import DeleteVideo from "../VideoInfoManager/DeleteVideo";

export default class VideoDataManager extends Component {

    constructor(props) {
        super(props);
        this.state = {
            defaultFilters: {status: [0], isDelete: [false]},
        }
    }

    columnsConfig = [
        {title: 'av号', key: 'av', width: 100, type: 'search', href: av => "https://www.bilibili.com/video/av" + av},
        {title: 'bv号', key: 'bv', width: 140, type: 'search'},
        {title: '期数', key: 'issue', width: 75, type: 'chooseInput', chooseMap: 'videoIssueResource'},
        {title: '播放量', key: 'view', width: 100, type: 'sorter'},
        {title: '评论', key: 'reply', width: 100, type: 'sorter'},
        {title: '收藏', key: 'favorite', width: 100, type: 'sorter'},
        {title: '硬币', key: 'coin', width: 100, type: 'sorter'},
        {title: '分P数', key: 'page', width: 100},
        {title: '得分', key: 'point', width: 100, type: 'sorter'},
        {title: '排名', key: 'rank', width: 70},
        {title: '标题', key: 'name', width: 200, type: 'search', ellipsis: true},
        {title: '封面', key: 'img', type: 'image', href: row => "https://www.bilibili.com/video/av" + row.av},
        {title: '类型', key: 'type', width: 120, type: 'choose', chooseMap: 'videoTypeResource', ellipsis: true},
        {title: '作者', key: 'owner', width: 100, type: 'search', ellipsis: true},
        {title: '原作者', key: 'externalOwner', width: 100, ellipsis: true},
        {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: 'copyrightList'},
        {title: '节选', key: 'startTime', width: 100, afterRender: dateFormat},
        {title: '发布日期', key: 'pubTime', width: 180},
        {title: '弹幕', key: 'danmaku', width: 100, type: 'sorter'},
        {title: '分享', key: 'share', width: 100, type: 'sorter'},
        {title: '点赞', key: 'like', width: 100, type: 'sorter'},
        {title: '踩', key: 'dislike', width: 60, type: 'sorter'},
        {title: '简介', key: 'description', width: 200, ellipsis: '\n'},
        {title: '是否已删除', key: 'isDelete', width: 110, type: 'choose', chooseMap: 'isDeleteList'},
        {title: '状态', key: 'status', width: 100, type: 'choose', chooseMap: 'statusList'},
    ];

    handleResourceLoaded = (resources, callback) => {
        const { videoIssueResource } = resources;
        const newIssue = Math.max(...videoIssueResource.map(resources => resources.value));
        this.setState({defaultFilters: Object.assign({issue: [newIssue]}, this.state.defaultFilters)}, callback)
    }

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { selectedRows, filters, resources } = state
        const selectedRow = selectedRows[0] || {}
        const chooseIssue = isNull(filters.issue)? selectedRow.issue: filters.issue[0];
        return (
            <>
                <ReRank issue={chooseIssue} onSuccess={handleUpdated}/>
                <UpdateStartTime selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <UpdateExternalOwner selectedRow={selectedRow} onSuccess={handleUpdated} resources={resources}/>
                <DeleteVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <Button href={downloadDataTxtUrl(chooseIssue)} target="_Blank" style={{marginLeft: "8px"}}>
                    检查data文件
                </Button>
            </>
        )
    }

    handleResourcesInit = () => ({
        isDeleteList: [ {value: false, text: '正常', renderHidden: true}, {value: true, text: '已删除'}],
        statusList: [{value: 0, text: '正常', renderHidden: true}, {value: 62002, text: '稿件不可见'}, {value: -404, text: '啥都木有'}],
        copyrightList: [{value: false, text: '原创', renderHidden: true}, {value: true, text: '搬运'}],
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['videoIssueResource', 'videoTypeResource']}
                defaultPageSize={20}
                defaultSorter={{field:"point", order:"descend"}}
                rowKey={record => record.av + "-" + record.issue}
                defaultFilters={this.state.defaultFilters}
                columnsConfig={this.columnsConfig}
                getDataApi={getVideoDataByCondition}
                onResourceLoaded={this.handleResourceLoaded}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

}