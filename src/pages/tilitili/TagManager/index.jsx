import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getTagByCondition} from "../../../api";
import UpdateTag from "./UpdateTag";

export default class TagManager extends Component {
    columnsConfig = [
        {title: 'id', key: 'id', width: 90, type: 'search', href: id => 'https://t.bilibili.com/topic/'+id},
        {title: 'Tag名', key: 'name', width: 200, type: 'search', ellipsis: true},
        {title: '标签封面', key: 'cover', type: 'image'},
        {title: '介绍', key: 'content', width: 700, ellipsis: '\n'},
        {title: '标签创建时间', key: 'externalCreateTime', width: 200},
        {title: '状态', key: 'status', width: 100, type: 'choose', chooseMap: 'TagStatusList'},
        {title: '类型', key: 'type', width: 140, type: 'choose', chooseMap: 'TagTypeResource'},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { resources, selectedRows } = state
        const selectedRow = selectedRows[0] || {};
        return (
            <UpdateTag selectedRow={selectedRow} onSuccess={handleUpdated} resources={resources}/>
        )
    }

    handleResourcesInit = () => ({
        TagStatusList: [{text: '正常', value: 0, renderHidden: true}, {text: '废弃', value: -1}],
    })

    render() {
        return (
            <DefaultTable
                needResourcesList={['TagTypeResource']}
                defaultPageSize={20}
                defaultFilters={{type: [1]}}
                defaultSorter={{field: 'id', order: 'ascend'}}
                rowKey={record => record.id}
                columnsConfig={this.columnsConfig}
                getDataApi={getTagByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

}