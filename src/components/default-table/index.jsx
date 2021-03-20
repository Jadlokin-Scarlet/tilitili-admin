import React, {Component} from 'react';
import {Button, Card, Row, Tag, message, Form, Input} from "antd";
import {
    checkResp,
    compose,
    defaultRowKey, defaultScroll,
    defineProperty,
    emptyFunc,
    getParagraph,
    If,
    Img,
    isEmpty,
    isNotEmpty,
    isNotNull,
    isNull,
    splitToList,
    convertToPrams, selfFunc, isEmptyObject
} from "../../utils/HtmlUtils";
import {getResources} from "../../api";
import './index.css'
import SimpleTable from "../simple-table";
import CacheInput from "../cache-input";
import SelectInput from "../select-input/SelectInput";
export default class DefaultTable extends Component {

    constructor(props) {
        super(props)

        const defaultParams = this.getDefaultParams();

        this.state = {
            data: {},
            resources: [],
            columns: [],

            pagination: defaultParams.pagination,
            filters: defaultParams.filters,
            sorter: defaultParams.sorter,

            selectedRows: [],
            selectedRowKeys: [],
            clickRow: {},

            dataLoading: true,
            resourcesLoading: true,
            visible: false,
        }
    }

    componentDidMount = async () => {
        this.setState({columns: this.props.columnsConfig.map(this.newColumn)})
        if (isNull(this.props.onResourceLoaded)) {
            this.getResources();
            this.refreshWithPrams();
        }else {
            const resources = await this.getResources();
            this.props.onResourceLoaded(resources, () => {
                this.updateFilters(this.props.defaultFilters, this.handleUpdated)
            });
        }
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.filters !== this.props.filters) {
            this.updateFilters(this.props.filters, this.handleUpdated)
        }
    }

    getDataByCondition = async (pagination, filters, sorter) => {
        const params = convertToPrams(pagination, filters, sorter)
        const {onGetData=emptyFunc} = this.props;
        const [isSubmit=true, error] = onGetData() || [];
        if (isSubmit) {
            // 只接收最后一次请求
            const reqId = Math.random()
            this.setState({dataLoading: true, reqId})
            let data;
            try {
                data = await this.props.getDataApi(params).then(checkResp);
            }catch (e) {
                this.setState({dataLoading: false});
            }
            if (reqId === this.state.reqId) {
                if (isNotNull(data)) {
                    this.updateSelectedRow(data);
                    this.setState({data});
                }
                this.setState({dataLoading: false});
            }
        }else if (isNotNull(error)){
            message.error(error);
        }
    }

    getResources = async callback => {
        const needResourcesList = this.props.needResourcesList || [];
        const resources = (this.props.onResourcesInit || emptyFunc)() || {};
        if (isNotEmpty(needResourcesList)) {
            const otherResources = await getResources({needResourcesList}).then(checkResp)
            Object.assign(resources, otherResources);
        }
        this.setState({resources, resourcesLoading: false}, callback)
        return resources;
    }

    updateSelectedRow = data => {
        const {rowKey=defaultRowKey} = this.props;
        const {selectedRowKeys} = this.state;
        if (selectedRowKeys.length > 0) {
            const {list=[]} = data;
            const keyList = list.map(rowKey);
            const newSelectedRows = [];
            const newSelectedRowKeys = [];

            for (const selectedRowKey of selectedRowKeys) {
                const index = keyList.indexOf(selectedRowKey)
                if (index !== -1) {
                    newSelectedRowKeys.push(selectedRowKey);
                    newSelectedRows.push(list[index]);
                }
            }
            this.setSelectRows(newSelectedRowKeys, newSelectedRows)
        }
        return data;
    }
    getDefaultParams = () => {
        return {
            filters: Object.assign({},this.props.defaultFilters || {}, this.props.filters || {}),
            sorter: Object.assign({}, this.props.defaultSorter) || {},
            pagination: {
                total: 0,
                pageSize: isNull(this.props.defaultPageSize)? 10: this.props.defaultPageSize,
                current: isNull(this.props.defaultCurrent)? 1: this.props.defaultCurrent,
            },
        }
    }

    getRowHtml = (rowKey) => {
        return document.getElementsByClassName("row-" + rowKey)[0]
    }

    setParams = (pagination=this.state.pagination, filters=this.state.filters, sorter=this.state.sorter, callback=emptyFunc) => {
        (this.props.onUpdateParams || emptyFunc)(pagination, filters, sorter);
        this.setState({pagination, filters, sorter}, callback)
    }

    updateParams = (pagination=this.state.pagination, filters={}, sorter=this.state.sorter, callback=emptyFunc) => {
        this.setParams(pagination, Object.assign({}, this.state.filters, filters), sorter, callback);
    }

    updateFilters = (filters, callback=emptyFunc) => {
        this.setParams(undefined, Object.assign({}, this.state.filters, filters), undefined, callback);
    }

    setFilter = (key, value, callback=emptyFunc) => {
        this.updateFilters(defineProperty({}, key, value), callback);
    }

    setSelectRows = (selectedRowKeys, selectedRows, callback) => {
        (this.state.data.list || []).map(this.props.rowKey).map(this.getRowHtml).forEach(element => {
            element.className = element.className.replace(" selected-row-color", "");
        })
        selectedRowKeys.map(this.getRowHtml).forEach(element => {
            element.className += ' selected-row-color';
        })
        this.setState({selectedRowKeys, selectedRows}, callback);
    }

    clearFilters = () => {
        const { pagination, filters, sorter } = this.getDefaultParams();
        this.setParams(pagination, filters, sorter)
    }

    refreshWithDefaultPrams = () => {
        const { pagination, filters, sorter } = this.getDefaultParams();
        this.getDataByCondition(pagination, filters, sorter);
    }

    refreshWithPrams = () => {
        const { pagination, filters, sorter } = this.state;
        this.getDataByCondition(pagination, filters, sorter);
    }

    handleRefresh = () => {
        this.refreshWithPrams();
    }

    handleReset = () => {
        this.clearFilters();
        this.refreshWithDefaultPrams();
    }

    handleSelectRows = (selectedRowKeys, selectedRows, callback) => {
        this.setSelectRows(selectedRowKeys, selectedRows, callback);
    }

    handleNotSelectAll = () => {
        this.setSelectRows([],[]);
    }

    handleSelectAll = () => {
        const selectedRows = [...this.state.data.list] || [];
        const selectedRowKeys = selectedRows.map(this.props.rowKey);
        this.setSelectRows(selectedRowKeys, selectedRows)
    }

    handleTableChange = (pagination, filters, sorter) => {
        const {total, pageSize, current} = pagination;
        pagination = {total, pageSize, current};
        filters = Object.assign(this.state.filters, filters);
        sorter = isEmptyObject(sorter)? this.props.defaultSorter: sorter;
        this.setParams(pagination, filters, sorter)
        this.getDataByCondition(pagination, filters, sorter);
    }

    handleUpdated = () => {
        this.refreshWithPrams();
    }

    handleKeyUp = (e) => {
        if (e.keyCode === 18) {
            this.refreshWithPrams();
        }
    }

    handleDoubleClick = (record) => {
        this.setState({visible: true, clickRow: record})
    }

    handleVisibleChange = visible => {
        this.setState({visible})
    }

    handleRowClassName = (row, index) => "row-" + (this.props.rowKey || defaultRowKey)(row, index)
        + ((this.props.onRowClassName || emptyFunc)(row, index) || "");


    render() {
        const {onButtonsInit=emptyFunc,rowKey=defaultRowKey,isMultipleSelect=false} = this.props;
        const {selectedRowKeys} = this.state;
        const selectRowLength = selectedRowKeys.length;
        const rowLength = (this.state.data.list || []).length;

        let loading = this.state.dataLoading;

        this.state.columns.map((column, index) => {
            return Object.assign(column, this.newColumn(this.props.columnsConfig[index]));
        })

        return (
            <Card onKeyUp={this.handleKeyUp} tabIndex="-1" size="small">
                <Row type="flex" className="margin-bottom-8">
                    <Button type="primary" onClick={this.handleReset} className="margin-left-8" disabled={loading}>重置</Button>
                    <Button type="primary" onClick={this.handleRefresh} className="margin-left-8" disabled={loading}>刷新</Button>
                    {If (isMultipleSelect && (selectRowLength < rowLength || rowLength === 0)).then(() => (
                        <Button type="primary" onClick={this.handleSelectAll} className="margin-left-8" disabled={loading || rowLength === 0}>全选</Button>
                    )).elseIf(isMultipleSelect && selectRowLength === rowLength).then(() => (
                        <Button type="primary" onClick={this.handleNotSelectAll} className="margin-left-8" disabled={loading || rowLength === 0}>取消全选</Button>
                    )).endIf()}
                    {onButtonsInit(this)}
                </Row>
                <SimpleTable
                    selectedRows={this.state.selectedRows}
                    selectedRowKeys={selectedRowKeys}
                    isMultipleSelect={this.props.isMultipleSelect}
                    isShowRowSelection={true}
                    onSelectRow={this.handleSelectRows}
                    onClick={this.handleClick}
                    onDoubleClick={this.handleDoubleClick}
                    onSelectAll={this.handleSelectAll}
                    size='small'
                    scroll={defaultScroll}
                    loading={loading}
                    data={this.state.data}
                    columns={this.state.columns}
                    onChange={this.handleTableChange}
                    rowKey={rowKey}
                    rowClassName={this.handleRowClassName}
                />
            </Card>
        )
    }

    newColumn = (columnConfig) => {
        const column = {};
        column.title = columnConfig.title;
        column.key = columnConfig.key;
        column.width = columnConfig.width;
        column.dataIndex = columnConfig.dataIndex || columnConfig.key;
        column.align = columnConfig.align || 'center';

        if (columnConfig.type === 'search') {
            this.newSearchColumn(column, columnConfig);
        }else if (columnConfig.type === 'choose') {
            this.newChooseColumn(column, columnConfig);
        }else if (columnConfig.type === 'sorter') {
            this.newSorterColumn(column, columnConfig);
        }else if (columnConfig.type === 'chooseRender') {
            this.newChooseRenderColumn(column, columnConfig);
        }else if (columnConfig.type === 'image') {
            this.newImage(column, columnConfig);
        }else if (columnConfig.type === 'chooseInput') {
            this.newChooseInput(column, columnConfig);
        }else if (columnConfig.type === 'button') {
            this.newButtonColumn(column, columnConfig);
        }

        if (isNotNull(columnConfig.ellipsis)) {
            if (columnConfig.ellipsis === true) {
                column.render = compose(column.render, getParagraph)
            }else if (typeof columnConfig.ellipsis === 'string') {
                const oldRender = column.render;
                column.render = compose(oldRender, text => getParagraph(
                    splitToList(columnConfig.ellipsis, text), text)
                )
            }
        }else if (isNotNull(columnConfig.afterRender)) {
            const oldRender = column.render || selfFunc;
            column.render = (text, record) => columnConfig.afterRender(oldRender(text, record), record)
        }else if (isNotNull(columnConfig.href)) {
            const oldRender = column.render || selfFunc;
            const afterRender = (value, record) => <a href={columnConfig.href(value, record)} target="_Blank" rel="noopener noreferrer">{value}</a>
            column.render = (text, record) => afterRender(oldRender(text, record), record)
        }

        return column
    }
    newSearchColumn = (column, columnConfig) => {
        const {filters} = this.state;

        column.className = "no-padding";
        column.title = (
            <CacheInput
                placeholder={columnConfig.title}
                value={isNull(filters[columnConfig.key], [])[0]}
                width={columnConfig.width}
                onChange={value=>this.setFilter(columnConfig.key, [value], this.handleUpdated)}
            />
        );
    }
    newChooseColumn = (column, columnConfig) => {
        const {filters, resources} = this.state;
        const resource = resources[columnConfig.chooseMap];

        column.filteredValue = filters[columnConfig.key] || null;
        column.filterMultiple = false;
        column.filters = resource;
        this.newChooseRenderColumn(column, columnConfig);
    }
    newChooseRenderColumn = (column, columnConfig) => {
        const {resources} = this.state;
        const resource = resources[columnConfig.chooseMap];

        column.render = key => {
            if (isNotEmpty(resource) && isNotNull(key)) {
                for (const item of resource) {
                    if (item.value.toString() === key.toString()) {
                        if (isNotNull(item.tag)) {
                            return this.newTag(item.text, item.tag);
                        }else if (item.renderHidden === true) {
                            return '';
                        }else {
                            return item.text;
                        }
                    }
                }
            }
        }
    }
    newSorterColumn = (column,  columnConfig) => {
        const {sorter} = this.state;

        column.sorter = true;
        column.sortOrder = sorter.field === columnConfig.key? (sorter.order || null) : null;
    }
    newImage = (column, columnConfig) => {
        const converseUrl = src => isEmpty(src)? "/loading.png": src;

        const {dataLoading:loading} = this.state;

        column.className = "no-padding";
        column.width = column.width || 60;
        column.render = (src, row) => getParagraph(
            <Img loading={loading} style={{width: '80px', height: '80px'}} alt='-' src={converseUrl(src, 80)}/>,
            If(isNull(columnConfig.href)).then(() =>
                <Img loading={loading} style={{height: '37px', maxWidth: '60px'}} alt='-' src={converseUrl(src, 60)}/>
            ).else(() =>
                <a href={columnConfig.href(src, row)} target="_Blank" rel="noopener noreferrer">
                    <Img loading={loading} style={{ height: '37px', maxWidth: '60px' }} src={converseUrl(src, 60)} alt="-"/>
                </a>
            )
        )
    }

    newChooseInput = (column, columnConfig) => {
        const {resources} = this.state;
        let resource = resources[columnConfig.chooseMap];

        if (Array.isArray(resource)) {
            resource = [{text: '全部', value: "----"}, ...resource]
        }

        const handleSelectInputChange = value => {
            if (value === "----") {
                value = null
            }
            this.setFilter(columnConfig.key, [value]);
        };
        const handleSelectInputChanged = value => {
            if (value === "----") {
                value = null
            }
            this.setFilter(columnConfig.key, [value], this.handleUpdated);
        };

        column.className = "no-padding";
        column.title = (
            <SelectInput
                size="small"
                width={columnConfig.width}
                placeholder={columnConfig.title}
                value={columnConfig.title}
                onChange={handleSelectInputChange}
                onSelect={handleSelectInputChanged}
                filters={resource}
            />
        );

        this.newChooseRenderColumn(column, columnConfig);
    }

    newButtonColumn = (column, columnConfig) => {
        column.className = "no-padding";
        column.render = (value, record) => (this.props.onButtonColumnInit || emptyFunc)(this, value, record);
    }

    newTag = (text, tag) => {
        if (tag === 'no') {
            return <Tag>{text}</Tag>
        }
        return <Tag color={tag}>{text}</Tag>
    }
}
