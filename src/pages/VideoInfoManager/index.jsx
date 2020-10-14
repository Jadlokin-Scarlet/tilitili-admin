import React, {Component} from "react";
import {Button, Card, message} from "antd";
import SimpleTable from "../../components/simple-table/SimpleTable";
import {getResources, getVideoInfoByCondition} from "../../api";
import {
    convertToPrams,
    isNull
} from "../../utils/htmlUtils";
import DeleteVideo from "./DeleteVideo";
import RecoveryVideo from "./RecoveryVideo";

export default class VideoInfoManager extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: {},
            resources: [],

            pagination: {},
            filters: {},
            sorter: {},

            selectedRow: {},
            selectedRowKey: null,

            loading: false,
            visible: false,

            defaultPrams: {
                filters: {},
                sorter: {field:"av", order:"descend"},
                pagination: {
                    total: 0,
                    pageSize: 20,
                    current: 1
                },
            },
        }
    }

    componentDidMount() {
        this.setState({...this.state.defaultPrams})
        this.getDataByCondition();
        this.getResource();
    }

    getResource = () => {
        const params = {
            needResourcesList: 'videoTypeResource'
        }
        getResources(params).then((data) => {
            if (typeof data.success !== 'undefined' && data.success === false ) {
                message.error("获取资源列表：" + data.message);
            } else {
                this.setState({resources:data})
            }
        })
    }

    getDataByCondition = (params) => {
        if (isNull(params)) {
            const { pagination, filters, sorter } = this.state.defaultPrams;
            params = convertToPrams(pagination, filters, sorter)
        }
        this.setState({loading: true})
        getVideoInfoByCondition(params).then(data => {
            if (typeof data.success !== 'undefined' && data.success === false ) {
                message.error("获取信息：" + data.message);
            } else {
                this.setState({data})
            }
        }).finally(() => this.setState({loading: false}));
    }

    rowKey = (record, index) => record.av;

    clearFilters = () => {
        this.setState({
            pagination: {},
            filters: {},
            sorter: {},
        })
    }

    clearSelectedRows = () => {
        this.setState({
            selectedRow: {},
            selectedRowKey: null,
        })
    }

    updateSelectedRows = () => {
        const list = isNull(this.data.list, [])
        const selectedRow = list.filter((record, index) => this.rowKey(record,index) === this.state.selectedRowKey);
        this.setState({
            selectedRow: selectedRow || {},
        })
    }

    handleRefreshButtonClick = () => {
        this.getDataByCondition();
        this.clearFilters();
        this.clearSelectedRows();
    }

    handleSelectRow = (selectedRowKey, selectedRow) => {
        this.setState({selectedRowKey, selectedRow})
    }

    handleTableChange = (pagination, filters, sorter) => {
        this.setState({pagination, filters, sorter});
        const params = convertToPrams(pagination, filters, sorter)
        this.getDataByCondition(params);
        this.clearSelectedRows();
    }

    handleFilteredInfoChange = (filters) => {
        this.setState({filters})
    }

    handleFilteredInfoChanged = (filters) => {
        const {pagination, sorter} = this.state;
        const params = convertToPrams(pagination, filters, sorter);
        this.getDataByCondition(params);
        this.clearSelectedRows();
    }

    handleUpdated = () => {
        const {pagination, filters, sorter} = this.state;
        const params = convertToPrams(pagination, filters, sorter);
        this.getDataByCondition(params);
        this.setState({visible: false})
        this.updateSelectedRows();
    }

    handleAdded = () => {
        const {pagination, filters, sorter} = this.state;
        const params = convertToPrams(pagination, filters, sorter);
        this.getDataByCondition(params);
        this.setState({visible: false})
        this.clearSelectedRows();
    }

    handleKeyUp = (e) => {
        if (e.keyCode === 17) {
            const { pagination, filters, sorter } = this.state;
            this.getDataByCondition(convertToPrams(pagination, filters, sorter));
            this.clearSelectedRows();
        }
    }
    render() {
        const { filters, loading, resources, data } = this.state;
        const { videoTypeResource } = resources;
        const columns = [
            {title: 'av号', key: 'av', width: 90, type: 'search'},
            {title: '标题', key: 'name', width: 200, type: 'search', ellipsis: true},
            {title: '封面', key: 'img', type: 'image', href: row => "https://www.bilibili.com/video/av" + row.av},
            {title: '类型', key: 'type', width: 90, type: 'choose', chooseMap: videoTypeResource},
            {title: '作者', key: 'owner', width: 100, ellipsis: true},
            {title: '发布日期', key: 'pubTime', width: 170},
            {title: '简介', key: 'description', width: 200, ellipsis: true},
            {title: '状态', key: 'isDelete', width: 70, type: 'choose', chooseMap: [
                {value: false, text: '正常'}, {value: true, text: '已删除'}
            ]},
            {title: 'bv号', key: 'bv', width: 130},
            {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: [
                {value: false, text: '原创'}, {value: true, text: '搬运'}
            ]},
        ];
        return (
            <Card onKeyUp={this.handleKeyUp} tabIndex="-1">
                <Button type="primary" style={{marginBottom: 8}} onClick={this.handleRefreshButtonClick}>刷新</Button>
                <DeleteVideo selectedRow={this.state.selectedRow} onSuccess={this.handleUpdated}/>
                <RecoveryVideo selectedRow={this.state.selectedRow} onSuccess={this.handleUpdated}/>
                <SimpleTable
                    {...{filters, loading, data, columns}}
                    config={{
                        rowKey: this.rowKey,
                        event: {
                            onChange: this.handleTableChange,
                            onDoubleClick: ()=>this.setState({visible: true}),
                            onFilteredInfoChanged: this.handleFilteredInfoChanged,
                            onFilteredInfoChange: this.handleFilteredInfoChange
                        }
                    }}
                    selectConfig={{
                        isShowRowSelection: true,
                        selectedRowKey: this.state.selectedRowKey,
                        onSelectRow: this.handleSelectRow,
                    }}
                />
            </Card>
        )
    }


}