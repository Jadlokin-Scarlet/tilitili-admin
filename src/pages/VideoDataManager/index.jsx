import React, {Component} from "react";
import {Button, Card, message} from "antd";
import SimpleTable from "../../components/simple-table/SimpleTable";
import {getResources, getVideoDataByCondition} from "../../api";
import {
    convertToPrams,
    isNull
} from "../../utils/htmlUtils";
import ReRank from "./ReRank";

export default class VideoDataManager extends Component {
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
                filters: {issue: 0},
                sorter: {field:"point", order:"descend"},
                pagination: {
                    total: 0,
                    pageSize: 20,
                    current: 1
                },
            },
        }
    }

    componentDidMount() {
        this.setState(this.state.defaultPrams)
        this.getDataByCondition();
        this.getResource();
    }

    getResource = () => {
        const params = {
            needResourcesList: 'videoIssueResource'
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
        getVideoDataByCondition(params).then(data => {
            if (typeof data.success !== 'undefined' && data.success === false ) {
                message.error("获取信息：" + data.message);
            } else {
                this.setState({data})
            }
        }).finally(() => this.setState({loading: false}));
    }

    rowKey = (record, index) => record.av + "-" + record.issue;

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
        const { filters, sorter, loading, resources, data, selectedRow } = this.state;
        const { videoIssueResource } = resources;
        const columns = [
            {title: 'av号', key: 'av', width: 100, type: 'search'},
            {title: '期数', key: 'issue', width: 70, type: 'choose', chooseMap: videoIssueResource},
            {title: '播放量', key: 'view', width: 100},
            {title: '评论', key: 'reply', width: 100},
            {title: '收藏', key: 'favorite', width: 100},
            {title: '硬币', key: 'coin', width: 100},
            {title: '分P数', key: 'page', width: 100},
            {title: '得分', key: 'point', width: 100, type: 'order'},
            {title: '排名', key: 'rank', width: 70},
            {title: '弹幕', key: 'danmaku', width: 100},
            {title: '分享', key: 'share', width: 100},
            {title: '点赞', key: 'like', width: 100},
            {title: '踩', key: 'dislike', width: 60},
        ];
        return (
            <Card onKeyUp={this.handleKeyUp} tabIndex="-1">
                <Button type="primary" style={{marginBottom: 8}} onClick={this.handleRefreshButtonClick}>刷新</Button>
                <ReRank issue={isNull(filters.issue)? selectedRow.issue: filters.issue} onSuccess={this.handleUpdated}/>
                <SimpleTable
                    {...{filters, sorter, loading, data, columns}}
                    config={{
                        rowKey: (row) => row.av + '-' + row.issue,
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