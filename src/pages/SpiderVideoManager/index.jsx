import React, {Component} from "react";
import DefaultTable from "../../components/default-table";
import {getTaskByCondition} from "../../api";
import SpiderVideo from "./SpiderVideo";

export default class SpiderVideoManager extends Component {
    constructor(props) {
        super(props);
    }

    handleColumnsInit = (resources) => {
        return [
            {title: 'id', key: 'id', width: 90, type: 'search'},
            {title: 'av号', key: 'av', width: 90, type: 'search'},
            {title: '状态', key: 'status', width: 70, type: 'choose', chooseMap: [
                    {value: -1, text: '废弃'},
                    {value: 0, text: '待爬取'},
                    {value: 1, text: '爬取中'},
                    {value: 2, text: '爬取成功'},
                    {value: 3, text: '爬取失败'},
                    {value: 4, text: '爬取超时'},
            ]},
            {title: '类型', key: 'type', width: 70, type: 'choose', chooseMap: [
                    {value: 0, text: '自定义爬取'},
                    {value: 1, text: '批量发起的自定义爬取'},
                    {value: 2, text: '周期扫描发起的爬取'},
            ]},
            {title: '备注', key: 'remark', width: 200, ellipsis: true},
        ];
    }

    handleTitleInit = (handleUpdated) => {
        return (() =>
            <SpiderVideo onSuccess={handleUpdated}/>
        )
    }

    render() {
        return (
            <DefaultTable
                onReqData={getTaskByCondition}
                onColumnsInit={this.handleColumnsInit}
                onTitleInit={this.handleTitleInit}
            />
        )
    }

}