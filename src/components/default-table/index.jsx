import React, {Component} from 'react';
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

export default class DefaultTable extends Component {
    constructor(props) {
        super(props);

        const { defaultPageSize = 20, defaultFilters = {}, defaultSorter = {} } = this.props
        
        
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
            selectedRow: {},
            selectedRowKey: null,
        })
    }
    
    updateSelectedRows = () => {
        const { rowKey=(record, index) => index } = this.props;
        const list = isNull(this.state.list, [])
        const selectedRows = list.filter((record, index) => rowKey(record,index) === this.state.selectedRowKey);
        this.setState({
            selectedRow: selectedRows[0] || {},
        })
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

    handleSelectRow = (selectedRowKey, selectedRow) => {
        this.setState({selectedRowKey, selectedRow})
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


    handleRowSelectChange = (selectedRowKeys, selectedRows) => {
        const { onSelectRow } = isNull(this.props.selectConfig, {});
        const selectedRowKey = selectedRowKeys[0] || null;
        const selectedRow = selectedRows[0] || {};
        if (onSelectRow) {
            onSelectRow(selectedRowKey, selectedRow);
        }
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

    render() {
        const {
            rowKey=(record, index) => index,
            size='small',
            type='radio',
            isShowRowSelection=false,
            onClick=emptyFunc,
            onDoubleClick=emptyFunc,
            onTitleInit=emptyFunc
        } = this.props;
        const { loading, list:dataSource, selectedRowKey, pagination } = this.state;
        const { pageSize, current, total } = pagination
        const columns = this.getColumnsFromProps();
        const title = onTitleInit(this.state, this.handleUpdated) || emptyFunc;
        return (
            <Card onKeyUp={this.handleKeyUp} tabIndex="-1">
                <Table
                    {...{ rowKey, size, loading, dataSource, columns }}
                    bordered
                    scroll={{x: 1}}
                    pagination={false}
                    rowSelection={If(isShowRowSelection).then({
                        type,
                        selectedRowKeys: [selectedRowKey],
                        onChange: this.handleRowSelectChange,
                    }).endIf()}
                    onChange={this.handleTableChange}
                    onRow={(record, index) => ({
                        onClick: () => {
                            this.handleSelectRow(rowKey(record, index), record)
                            onClick(record, index)
                        },
                        onDoubleClick: () => {
                            this.handleSelectRow(rowKey(record, index), record)
                            onDoubleClick(record, index);
                        }
                    })}
                    title={(data) =>
                        <Row type="flex" justify="space-between" align="middle">
                            <Col>
                                <Button type="primary" onClick={this.handleRefreshButtonClick}>刷新</Button>
                                {title(data)}
                            </Col>
                            <Col>
                                <Pagination size="small"
                                            total={total}
                                            current={current}
                                            pageSize={pageSize || 0}
                                            showSizeChanger
                                            showQuickJumper
                                            pageSizeOptions={['10', '20', '50', '100']}
                                            showTotal={total => `总共 ${total}`}
                                            onChange={this.handlePaginationChange}
                                            onShowSizeChange={this.handlePaginationChange}/>
                            </Col>
                        </Row>
                    }
                />
            </Card>
        )
    }


}