import React, {Component, PureComponent} from 'react';
import {Button, Card, Col, Pagination, Row, Table} from 'antd';
import {
    compose,
    convertToPrams,
    defineProperty,
    emptyFunc,
    getColumnChooseProps,
    getColumnOrderProps,
    getInputTitle,
    getParagraph,
    getSmallImg, If, isEmpty, isNotNull,
    isNull, selfFunc
} from "../../utils/HtmlUtils";
import {getResources} from "../../api";
import './index.css'

export default class DefaultTable extends PureComponent {
    constructor(props) {
        super(props);

        const { defaultPageSize = 20, defaultFilters = {}, defaultSorter = {} } = this.props
        
        
        this.state = {
            list: [],
            resources: [],

            pagination: {},
            filters: {},
            sorter: {},

            selectedRows: [],
            selectedRowKeys: [],

            loading: true,
            visible: false,
            
            defaultParams: {
                filters: defaultFilters,
                sorter: defaultSorter,
                pagination: {
                    pageSize: defaultPageSize,
                    current: 1,
                    total: 0,
                },
            },
        }
    }

    componentDidMount = async () => {
        const { pagination, filters, sorter } = this.state.defaultParams
        const resources = await this.getResource();
        if (this.props.onParamsInit) {
            Object.assign(filters, this.props.onParamsInit(resources));
            this.setState(Object.assign(this.state.defaultParams, {filters}))
        }
        await this.updateDataByCondition(convertToPrams(pagination, filters, sorter));
        this.setState({ filters, sorter, resources })
    }
    
    getResource = async () => {
        if (isEmpty(this.props.needResourcesList)) {
           return {} 
        }
        return await getResources({needResourcesList: this.props.needResourcesList});
    }

    updateDataByCondition = async (params) => {
        this.setState({loading: true})
        try {
            const data = await this.props.onReqData(params)
            this.setState({
                list: data.list,
                pagination: {
                    total: data.total,
                    pageSize: data.pageSize,
                    current: data.current
                },
            })
        } finally {
            this.setState({
                loading: false,
            })
        }
    }

    clearSelectedRows = () => {
        this.setState({
            selectedRows: [],
            selectedRowKeys: [],
        })
    }

    updateSelectedRows = () => {
        const { selectedRowKeys=[], list=[] } = this.state;
        const { rowKey=(record, index) => isNull(record.id, index) } = this.props;
        const selectedRows = selectedRowKeys.map(selectedRowKey => (
            list.filter((record, index) => rowKey(record,index) === selectedRowKey)[0]
        ))
        this.setState({selectedRows,})
    }
    
    refresh = (
        pagination = this.state.defaultParams.pagination,
        filters = this.state.defaultParams.filters,
        sorter = this.state.defaultParams.sorter
    ) => {
        const params = convertToPrams(pagination, filters, sorter)
        this.setState({pagination, filters, sorter})
        this.updateDataByCondition(params).then();
    }

    refreshWithFilters = (
        pagination = this.state.pagination,
        filters = this.state.filters,
        sorter = this.state.sorter
    ) => {
        const params = convertToPrams(pagination, filters, sorter)
        this.setState({pagination, filters, sorter})
        this.updateDataByCondition(params).then()
    }

    handleRefreshButtonClick = () => {
        this.refresh();
        this.clearSelectedRows();
    }

    handleTableChange = (pagination, filters, sorter) => {
        filters = Object.entries(filters)
            .map(item => defineProperty({}, item[0], isNull(item[1])? item[1]: item[1][0]))
            .reduce((a, b) => Object.assign({}, a, b), {});

        this.refreshWithFilters(undefined, filters, sorter);
        this.clearSelectedRows();
    }

    handlePaginationChange = (page, pageSize) => {
        const { pagination, filters, sorter } = this.state;
        pagination.current = page
        pagination.pageSize = pageSize;
        this.refreshWithFilters(pagination, filters, sorter);
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
        if (e.keyCode === 18) {
            this.refreshWithFilters();
            this.clearSelectedRows();
        }
    }


    handleSelectRow = (selectedRowKey, selectedRow) => {
        const { multiple=false } = this.props;
        if (multiple) {
            const { selectedRowKeys, selectedRows } = this.state;
            if (selectedRowKeys.includes(selectedRowKey)) {
                selectedRows.splice(selectedRowKeys.indexOf(selectedRowKey), 1)
                selectedRowKeys.splice(selectedRowKeys.indexOf(selectedRowKey), 1);
            }else {
                selectedRows.push(selectedRow);
                selectedRowKeys.push(selectedRowKey);
            }
            this.setState({
                selectedRowKeys: selectedRowKeys,
                selectedRows: selectedRows
            })
        }else {
            this.setState({
                selectedRowKeys: [selectedRowKey],
                selectedRows: [selectedRow]
            })
        }
    }

    handleRowSelectChange = (selectedRowKeys, selectedRows) => {
        this.setState({selectedRowKeys, selectedRows});
    };

    getColumnsFromProps() {
        const { filters, sorter, loading, resources } = this.state;
        const { columns=[] } = this.props

        return columns.map(columnConfig => {
            const { title, key, dataIndex, width, align, type, chooseMap=[], href, ellipsis, beforeRender=selfFunc, afterRender=selfFunc } = columnConfig;
            const column =  {
                title, key, width,
                dataIndex: dataIndex || key,
                align: align || 'center',
            }

            if (type === 'choose') {
                Object.assign(column, getColumnChooseProps(filters, key, Array.isArray(chooseMap)? chooseMap: resources[chooseMap]));
            }else if (type === 'order') {
                Object.assign(column, getColumnOrderProps(sorter, key));
            }else if (type === 'search') {
                Object.assign(column, getInputTitle(title, key, filters, this.handleFilteredInfoChanged, this.handleFilteredInfoChange));
            }else if (type === 'image') {
                Object.assign(column, getSmallImg(loading, href));
            }

            if (type !== 'image') {
                if (isNotNull(beforeRender)) {
                    column.render = compose(beforeRender, column.render || selfFunc)
                }
                if (isNotNull(ellipsis)) {
                    if (ellipsis === true) {
                        column.render = compose(column.render || selfFunc, text => getParagraph(text))
                    } else {
                        const smallTextFun = text => text.includes(ellipsis) ? text.split(ellipsis).map(text =>
                            <p key={text} style={{marginBottom: "0em"}}>{text}</p>
                        ) : text;
                        column.render = compose(column.render || selfFunc, text => getParagraph(smallTextFun(text), text))
                    }
                }
                if (isNotNull(afterRender)) {
                    column.render = compose(column.render || selfFunc, afterRender)
                }
            }
            return column;
        })
    }

    handleTitleInit = (data) => {
        const { onTitleInit=emptyFunc } = this.props;
        const { pagination } = this.state;
        const title = onTitleInit(this.state, this.handleUpdated) || emptyFunc;
        return (
            <Title {...pagination}
                   onPaginationChange={this.handlePaginationChange}
                   onRefreshButtonClick={this.handleRefreshButtonClick}
            >{title(data)}</Title>
        )
    }

    handleRow = (record, index) => {
        const { rowKey=(record, index) => index, onClick=emptyFunc, onDoubleClick=emptyFunc } = this.props;
        return {
            onClick: async () => {
                this.handleSelectRow(rowKey(record, index), record)
                onClick(record, index)
            },
            onDoubleClick: async () => {
                onDoubleClick(record, index);
            }
        }
    }

    render() {
        console.log("reload html")
        const {
            rowKey=(record, index) => index,
            size='small',
            type='radio',
            isShowRowSelection=false,
        } = this.props;
        const { loading, list:dataSource, selectedRowKeys } = this.state;
        const columns = this.getColumnsFromProps();
        const rowSelection = If(isShowRowSelection).then({
            type,
            selectedRowKeys,
            onChange: this.handleRowSelectChange,
        }).endIf()
        return (
            <Card onKeyUp={this.handleKeyUp} tabIndex="-1">
                <Table
                    {...{ rowKey, size, loading, dataSource, columns }}
                    bordered
                    scroll={{x: 1}}
                    pagination={false}
                    rowSelection={rowSelection}
                    onChange={this.handleTableChange}
                    onRow={this.handleRow}
                    title={this.handleTitleInit}
                />
            </Card>
        )
    }


}

class Title extends PureComponent {
    pageSizeOptions = ['10', '20', '50', '100'];
    showTotal = total => `总共 ${total}`;
    render() {
        return (
            <Row type="flex" justify="space-between" align="middle">
                <Col>
                    <Button type="primary" onClick={this.props.onRefreshButtonClick}>刷新</Button>
                    {this.props.children}
                </Col>
                <Col>
                    <Pagination
                        size="small"
                        total={this.props.total}
                        current={this.props.current}
                        pageSize={this.props.pageSize || 0}
                        showSizeChanger
                        showQuickJumper
                        pageSizeOptions={this.pageSizeOptions}
                        showTotal={this.showTotal}
                        onChange={this.props.onPaginationChange}
                        onShowSizeChange={this.props.onPaginationChange}
                    />
                </Col>
            </Row>

        )
    }
}