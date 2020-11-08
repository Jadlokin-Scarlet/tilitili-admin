import React, {Component} from "react";
import {Button} from "antd";
import {downloadDataTxtUrl, getVideoDataByCondition} from "../../api";
import {
    defineProperty,
    isNull
} from "../../utils/HtmlUtils";
import ReRank from "./ReRank";
import DefaultTable from "../../components/default-table";

export default class VideoDataManager extends Component {

    columns = [
        {title: 'av号', key: 'av', width: 100, type: 'search'},
        {title: '期数', key: 'issue', width: 70, type: 'choose', chooseMap: 'videoIssueResource'},
        {title: '播放量', key: 'view', width: 100, type: 'order'},
        {title: '评论', key: 'reply', width: 100, type: 'order'},
        {title: '收藏', key: 'favorite', width: 100, type: 'order'},
        {title: '硬币', key: 'coin', width: 100, type: 'order'},
        {title: '分P数', key: 'page', width: 100},
        {title: '得分', key: 'point', width: 100, type: 'order'},
        {title: '排名', key: 'rank', width: 70},
        {title: '弹幕', key: 'danmaku', width: 100, type: 'order'},
        {title: '分享', key: 'share', width: 100, type: 'order'},
        {title: '点赞', key: 'like', width: 100, type: 'order'},
        {title: '踩', key: 'dislike', width: 60, type: 'order'},
        {title: '标题', key: 'name', width: 200, type: 'search', ellipsis: true},
        {title: '封面', key: 'img', type: 'image', href: row => "https://www.bilibili.com/video/av" + row.av},
        {title: '类型', key: 'type', width: 70, type: 'choose', chooseMap: 'videoTypeResource', ellipsis: true},
        {title: '作者', key: 'owner', width: 100, type: 'search', ellipsis: true},
        {title: '发布日期', key: 'pubTime', width: 180},
        {title: '简介', key: 'description', width: 200, ellipsis: true},
        {title: '状态', key: 'isDelete', width: 70, type: 'choose', chooseMap: [
                {value: false, text: '正常'}, {value: true, text: '已删除'}
        ]},
        {title: 'bv号', key: 'bv', width: 140, type: 'search'},
        {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: [
                {value: false, text: '原创'}, {value: true, text: '搬运'}
        ]},
    ];

    handleParamsInit = (resources) => {
        const { videoIssueResource } = resources;
        return defineProperty({}, 'issue', Math.max(...videoIssueResource.map(resources => resources.value)))
    }

    handleTitleInit = (props, handleUpdated) => {
        const { filters, selectedRow } = props
        const chooseIssue = isNull(filters.issue)? selectedRow.issue: filters.issue;
        return (() =>
            <span>
                <ReRank issue={chooseIssue} onSuccess={handleUpdated}/>
                <Button type="primary" href={downloadDataTxtUrl(chooseIssue)} target="_Blank" style={{marginLeft: "8px"}}>
                    检查data文件
                </Button>
            </span>
        )
    }

    render() {
        return (
            <DefaultTable
                isShowRowSelection={true}
                needResourcesList={['videoIssueResource', 'videoTypeResource']}
                defaultSorter={{field:"point", order:"descend"}}
                rowKey={(record, index) => record.av + "-" + record.issue}
                columns={this.columns}
                onParamsInit={this.handleParamsInit}
                onReqData={getVideoDataByCondition}
                onTitleInit={this.handleTitleInit}
            />
        )
    }

}