import React, {Component} from "react";
import {getVideoInfoByCondition} from "../../api";
import DeleteVideo from "./DeleteVideo";
import RecoveryVideo from "./RecoveryVideo";
import DefaultTable from "../../components/default-table";

export default class VideoInfoManager extends Component {

    columns = [
        {title: 'av号', key: 'av', width: 90, type: 'search'},
        {title: '标题', key: 'name', width: 200, type: 'search', ellipsis: true},
        {title: '封面', key: 'img', type: 'image', href: row => "https://www.bilibili.com/video/av" + row.av},
        {title: '类型', key: 'type', width: 120, type: 'choose', chooseMap: "videoTypeResource"},
        {title: '作者', key: 'owner', width: 100, type: 'search', ellipsis: true},
        {title: '发布日期', key: 'pubTime', width: 180, type: 'order'},
        {title: '简介', key: 'description', width: 200, ellipsis: true},
        {title: '是否已删除', key: 'isDelete', width: 110, type: 'choose', chooseMap: [
            {value: false, text: '正常'}, {value: true, text: '已删除'}
        ]},
        {title: '状态', key: 'status', width: 100, type: 'choose', chooseMap: [
                {value: 0, text: '正常'}, {value: 62002, text: '稿件不可见'}, {value: -404, text: '啥都木有'}
        ]},
        {title: 'bv号', key: 'bv', width: 140, type: 'search'},
        {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: [
            {value: false, text: '原创'}, {value: true, text: '搬运'}
        ]},
    ];

    handleTitleInit = (props, handleUpdated) => {
        const { selectedRows } = props;
        const selectedRow = selectedRows[0] || {}
        return (() =>
            <span>
                <DeleteVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
                <RecoveryVideo selectedRow={selectedRow} onSuccess={handleUpdated}/>
            </span>
        )
    }

    render() {
        return (
            <DefaultTable
                isShowRowSelection={true}
                needResourcesList={['videoTypeResource']}
                defaultSorter={{field:"pubTime", order:"descend"}}
                rowKey={(record, index) => record.av}
                onReqData={getVideoInfoByCondition}
                columns={this.columns}
                onTitleInit={this.handleTitleInit}
            />
        )
    }
}