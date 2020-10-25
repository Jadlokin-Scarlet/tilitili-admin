import React, {Component} from "react";
import {Button, Card} from "antd";
import SimpleTable from "../../components/simple-table";
import {downloadDataTxtUrl, getResources, getVideoDataByCondition} from "../../api";
import {
    convertToPrams,
    isNull
} from "../../utils/HtmlUtils";
import ReRank from "./ReRank";

export default class VideoDataManager extends Component {
    constructor(props) {
        super(props);
        this.state = {
            list: [],
            resources: [],

            pagination: {},
            filters: {},
            sorter: {},

            selectedRow: {},
            selectedRowKey: null,

            loading: true,
            visible: false,

            defaultPrams: {
                filters: {issue: 0},
                sorter: {field:"point", order:"descend"},
                pagination: {
                    pageSize: 20,
                    current: 1
                },
            },
        }
    }
    reqData = getVideoDataByCondition;
    needResourcesList = ['videoIssueResource', 'videoTypeResource']
    rowKey = (record, index) => record.av + "-" + record.issue;

    componentDidMount = async () => {
        const { pagination, filters, sorter } = this.state.defaultPrams
        const resources = await this.getResource();
        filters.issue = Math.max(...resources.videoIssueResource.map(resources => resources.value));
        const data = await this.getDataByCondition(convertToPrams(pagination, filters, sorter));
        this.setState({ pagination, filters, sorter, resources })
        this.setDataState(data)
    }

    getResource = async () => {
        return  await getResources({needResourcesList: this.needResourcesList});
    }

    getDataByCondition = async (params) => {
        this.setState({loading: true})
        const data = await this.reqData(params)
        this.setState({loading: false})
        return data;
    }

    setDataState = data => {
        this.setState({
            list: data.list,
            pagination: {
                total: data.total,
                pageSize: data.pageSize,
                current: data.current
            },
        })
    }

    clearSelectedRows = () => {
        this.setState({
            selectedRow: {},
            selectedRowKey: null,
        })
    }

    updateSelectedRows = () => {
        const list = isNull(this.state.list, [])
        const selectedRow = list.filter((record, index) => this.rowKey(record,index) === this.state.selectedRowKey);
        this.setState({
            selectedRow: selectedRow || {},
        })
    }

    refresh = (
        pagination = this.state.defaultPrams.pagination,
        filters = this.state.defaultPrams.filters,
        sorter = this.state.defaultPrams.sorter
    ) => {
        const params = convertToPrams(pagination, filters, sorter)
        this.setState({pagination, filters, sorter})
        this.getDataByCondition(params).then(this.setDataState);
    }

    refreshWithFilters = (
        pagination = this.state.pagination,
        filters = this.state.filters,
        sorter = this.state.sorter
    ) => {
        const params = convertToPrams(pagination, filters, sorter)
        this.setState({pagination, filters, sorter})
        this.getDataByCondition(params).then(this.setDataState);
    }

    handleRefreshButtonClick = () => {
        this.refresh();
        this.clearSelectedRows();
    }

    handleSelectRow = (selectedRowKey, selectedRow) => {
        this.setState({selectedRowKey, selectedRow})
    }

    handleTableChange = (pagination, filters, sorter) => {
        this.refreshWithFilters(undefined, filters, sorter);
        this.clearSelectedRows();
    }

    handleFilteredInfoChange = (filters) => {
        this.setState({filters})
    }

    handleFilteredInfoChanged = (filters) => {
        this.refreshWithFilters(undefined, filters, undefined);
        this.clearSelectedRows();
    }

    handleUpdated = () => {
        this.refreshWithFilters();
        this.setState({visible: false})
        this.updateSelectedRows();
    }

    handleAdded = () => {
        this.refreshWithFilters();
        this.setState({visible: false})
        this.clearSelectedRows();
    }

    handleKeyUp = (e) => {
        if (e.keyCode === 17) {
            this.refreshWithFilters();
            this.clearSelectedRows();
        }
    }
    render() {
        const { pagination, filters, sorter, loading, resources, list, selectedRow } = this.state;
        const { videoIssueResource, videoTypeResource } = resources;
        const chooseIssue = isNull(filters.issue)? selectedRow.issue: filters.issue;
        const columns = [
            {title: 'av号', key: 'av', width: 100, type: 'search'},
            {title: '期数', key: 'issue', width: 70, type: 'choose', chooseMap: videoIssueResource},
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
            {title: '类型', key: 'type', width: 90, type: 'choose', chooseMap: videoTypeResource},
            {title: '作者', key: 'owner', width: 100, type: 'search', ellipsis: true},
            {title: '发布日期', key: 'pubTime', width: 170},
            {title: '简介', key: 'description', width: 200, ellipsis: true},
            {title: '状态', key: 'isDelete', width: 70, type: 'choose', chooseMap: [
                    {value: false, text: '正常'}, {value: true, text: '已删除'}
                ]},
            {title: 'bv号', key: 'bv', width: 140, type: 'search'},
            {title: '搬运', key: 'copyright', width: 70, type: 'choose', chooseMap: [
                    {value: false, text: '原创'}, {value: true, text: '搬运'}
                ]},
        ];
        return (
            <Card onKeyUp={this.handleKeyUp} tabIndex="-1">
               <SimpleTable
                    title={() => (
                        <div>
                            <Button type="primary" onClick={this.handleRefreshButtonClick}>刷新</Button>
                            <ReRank issue={chooseIssue} onSuccess={this.handleUpdated}/>
                            <Button type="primary" href={downloadDataTxtUrl(chooseIssue)} target="_Blank" style={{marginLeft: "8px"}}>
                                检查data文件
                            </Button>
                        </div>
                    )}
                    {...{pagination, filters, sorter, loading, list, columns}}
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