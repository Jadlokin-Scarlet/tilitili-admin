import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getOwnerByCondition} from "../../../api";
import UpdateOwner from "./UpdateOwner";

export default class OwnerManager extends Component {
    columnsConfig = [
        {title: 'uid', key: 'uid', width: 90, type: 'search', href: uid => 'https://space.bilibili.com/'+uid},
        {title: '作者名', key: 'name', width: 200, type: 'search', ellipsis: true},
        {title: '头像', key: 'face', type: 'image'},
        {title: '简介', key: 'sign', width: 200, ellipsis: true},
        {title: '等级', key: 'level', width: 45},
        {title: '状态', key: 'status', width: 80, type: 'choose', chooseMap: 'statusList'},
        {title: '', key: '', width: 800},
    ];

    handleButtonsInit = (that) => {
        const { state, handleUpdated } = that
        const { resources, selectedRows } = state
        const selectedRow = selectedRows[0] || {};
        return <>
            <UpdateOwner selectedRow={selectedRow} resources={resources} onSuccess={handleUpdated}/>
        </>
    }

    handleResourcesInit = () => ({
        statusList: [{value: -1, text: '废弃', tag: 'no'}, {value: 0, text: '正常', tag: 'green'}, {value: 1, text: '小黑屋', tag: 'red'}]
    })

    render() {
        return (
            <DefaultTable
                defaultPageSize={20}
                rowKey={record => record.uid}
                columnsConfig={this.columnsConfig}
                getDataApi={getOwnerByCondition}
                onButtonsInit={this.handleButtonsInit}
                onResourcesInit={this.handleResourcesInit}
            />
        )
    }

}