import React, {Component} from "react";
import {Button, Card, message} from "antd";
import {convertToPrams, getColumnChooseProps, getSmallImg, isNull} from "../../utils/HtmlUtils";
import {getResources, listVideoDataList} from "../../api";
import SimpleTable from "../../components/simple-table";

export default class VideoDataFileCheck extends Component {

    constructor(props) {
        super(props)
        this.state = {
            data: {},
            resource: [],

            pagination: {},
            filteredInfo: {},
            sorter: {},

            selectedRows: [],
            selectedRowKeys: [],

            loading: true,
            visible: false,

            defaultPrams: {
                filteredInfo: {},
                sorter: {},
                pagination: {
                    total: 0,
                    pageSize: 10,
                    current: 1
                },
            },
        }
    }

    componentDidMount = async () => {
        const { defaultPrams } = this.state;
        const {videoIssueResource} = await this.getResource();
        defaultPrams.filters = {issue:  Math.max(...videoIssueResource.map(resources => resources.value))}
        this.setState(defaultPrams)
        const { pagination, filters, sorter } = defaultPrams
        this.getDataByCondition(convertToPrams(pagination, filters, sorter));
    }

    getResource = () => {
        const params = {
            needResourcesList: ['videoIssueResource'],
        }
        return getResources(params)
    }

    getDataByCondition = (params) => {
        if (isNull(params)) {
            const { pagination, filteredInfo, sorter } = this.state.defaultPrams;
            params = convertToPrams(pagination, filteredInfo, sorter)
        }
        this.setState({loading: true})
        listVideoDataList(params).then(data => {
            if (typeof data.success !== 'undefined' && data.success === false ) {
                message.error("获取信息：" + data.message);
            } else {
                this.setState({data})
            }
        }).finally(() => this.setState({loading: false}));
    }

    clearFilters = () => {
        this.setState({
            pagination: {},
            filteredInfo: {},
            sorter: {},
        })
    }

    clearSelectedRows = () => {
        this.setState({
            selectedRows: [],
            selectedRowKeys: [],
        })
    }

    handleRefreshButtonClick = () => {
        this.getDataByCondition();
        this.clearFilters();
        this.clearSelectedRows();
    }

    handleSelectRows = (selectedRows, selectedRowKeys) => {
        this.setState({selectedRows, selectedRowKeys})
    }

    handleTableChange = (pagination, filteredInfo, sorter) => {
        this.setState({pagination, filteredInfo, sorter});
        const params = convertToPrams(pagination, filteredInfo, sorter)
        this.getDataByCondition(params);
        this.clearSelectedRows();
    }

    handleFilteredInfoChange = (filteredInfo) => {
        this.setState({filteredInfo})
    }

    handleFilteredInfoChanged = (filteredInfo) => {
        const {pagination, sorter} = this.state;
        const params = convertToPrams(pagination, filteredInfo, sorter);
        this.getDataByCondition(params);
        this.clearSelectedRows();
    }

    handleUpdated = () => {
        const {pagination, filteredInfo, sorter} = this.state;
        const params = convertToPrams(pagination, filteredInfo, sorter);
        this.getDataByCondition(params);
        this.setState({visible: false})
        this.clearSelectedRows()
    }

    handleKeyUp = (e) => {
        if (e.keyCode === 18) {
            const { pagination, filteredInfo, sorter } = this.state;
            this.getDataByCondition(convertToPrams(pagination, filteredInfo, sorter));
            this.clearSelectedRows();
        }
    }

    render() {
        const { filteredInfo:filters, loading } = this.state;
        const columns = [
            {title: 'av号', align: "center", key: 'av', dataIndex: 'av', width: 100},
            {title: '标题', align: "center", key: 'name', dataIndex: 'name', width: 200},
            {title: '封面', align: "center", key: 'img', dataIndex: 'img', ...getSmallImg(loading)},
            {title: '类型', align: "center", key: 'type', dataIndex: 'type', width: 120},
            {title: '作者', align: "center", key: 'owner', dataIndex: 'owner', width: 100},
            {title: '搬运', align: "center", key: 'copyright', dataIndex: 'copyright', width: 70, ...getColumnChooseProps(filters, 'copyright', [
                    {value: false, text: '原创'}, {value: true, text: '搬运'}
            ])},
            {title: '发布日期', align: "center", key: 'pubTime', dataIndex: 'pubTime', width: 180},
            {title: '节选开始时间(秒)', align: "center", key: 'startTime', dataIndex: 'startTime', width: 180},
            {title: '播放量', align: "center", key: 'view', dataIndex: 'view', width: 100},
            {title: '评论', align: "center", key: 'reply', dataIndex: 'reply', width: 100},
            {title: '收藏', align: "center", key: 'favorite', dataIndex: 'favorite', width: 100},
            {title: '硬币', align: "center", key: 'coin', dataIndex: 'coin', width: 100},
            {title: '得分', align: "center", key: 'point', dataIndex: 'point', width: 100},
            {title: '排名', align: "center", key: 'rank', dataIndex: 'rank', width: 70},
            {title: '历史排名', align: "center", key: 'hisRank', dataIndex: 'hisRank', width: 100},
            {title: '是否长期', align: "center", key: 'isLen', dataIndex: 'isLen', width: 100, ...getColumnChooseProps(filters, 'copyright', [
                    {value: 1, text: '是'}, {value: 0, text: '否'}
            ])},
        ];
        
        return (
            <div>
                <Card onKeyUp={this.handleKeyUp} tabIndex="-1">
                    <div>
                        <Button type="primary" style={{marginBottom: 8}} onClick={this.handleRefreshButtonClick}>刷新</Button>
                        <SimpleTable
                            selectedRows={this.state.selectedRows}
                            selectedRowKeys={this.state.selectedRowKeys}
                            isShowRowSelection={true}
                            onSelectRow={this.handleSelectRows}
                            onDoubleClick={()=>this.setState({visible: true})}
                            size='small'
                            scroll={{x:100,y:400}}
                            loading={this.state.loading}
                            data={this.state.data}
                            columns={columns}
                            onChange={this.handleTableChange}
                            rowKey={(row) => row.av}
                        />
                    </div>
                </Card>
            </div>
        )
    }
}