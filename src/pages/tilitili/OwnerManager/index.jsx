import React, {Component} from "react";
import DefaultTable from "../../../components/default-table";
import {getOwnerByCondition} from "../../../api";

export default class OwnerManager extends Component {
    columnsConfig = [
        {title: 'uid', key: 'uid', width: 90, type: 'search', href: uid => 'https://space.bilibili.com/'+uid},
        {title: '作者名', key: 'name', width: 200, type: 'search', ellipsis: true},
        {title: '头像', key: 'face', type: 'image'},
        {title: '简介', key: 'sign', width: 200, ellipsis: true},
        {title: '等级', key: 'level', width: 45},
        {title: '', key: '', width: 800},
    ];

    handleButtonsInit = (that) => {
        // const { state, handleUpdated } = that
        // const { resources, selectedRows } = state
        // const selectedRow = selectedRows[0] || {};
        return <>
        </>
    }

    handleResourcesInit = () => ({
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