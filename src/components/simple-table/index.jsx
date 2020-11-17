import React from 'react';
import {Col, Pagination, Row, Table} from 'antd';
import {
    defineProperty,
    emptyFunc,
    getColumnChooseProps,
    getColumnOrderProps,
    getInputTitle,
    getParagraph,
    getSmallImg, isNotBlank,
    isNull
} from "../../utils/HtmlUtils";

class SimpleTable extends React.Component {
    handleRowSelectChange = (selectedRowKeys, selectedRows) => {
        const { onSelectRow } = isNull(this.props.selectConfig, {});
        const selectedRowKey = selectedRowKeys[0] || null;
        const selectedRow = selectedRows[0] || {};
        if (onSelectRow) {
            onSelectRow(selectedRowKey, selectedRow);
        }
    };

    getColumnsFromProps() {
        const { filters, sorter, loading, config = {} } = this.props;
        const event = config.event || {};
        const { onFilteredInfoChanged, onFilteredInfoChange } = event
        const columns = this.props.columns || [];
        columns.forEach(column => Object.assign(column, {
            align: "center",
            dataIndex: column.key,
        }));
        columns.filter(column => column.ellipsis).forEach(column => Object.assign(column, {
            ellipsis: undefined,
            render: text => getParagraph(text),
        }))
        columns.forEach(column => Object.assign(column,
            column.type === 'choose'? getColumnChooseProps(filters, column.key, column.chooseMap):
                    column.type === 'order'? getColumnOrderProps(sorter, column.key):
                    column.type === 'search'? getInputTitle(column.title, column.key, filters, onFilteredInfoChanged, onFilteredInfoChange):
                    column.type === 'image'? getSmallImg(loading, column.href): {}
        ))
        return columns;
    }

    handleTableChange = (pagination, filters, sorter) => {
        const { config = {} } = this.props;
        const { event = {} } = config
        const { onChange = emptyFunc } = event

        const filter = Object.entries(filters)
            .filter(item => isNotBlank(item[1]))
            .map(item => defineProperty({}, item[0], isNull(item[1])? item[1]: item[1][0]))
            .reduce((a, b) => Object.assign({}, a, b), {});

        onChange(pagination, filter, sorter)
    }

    showTotal = (total) => {
        return `总共 ${total}`;
    }

    handlePaginationChange = page => {
        const { pagination, filter, sorter, config } = this.props;
        const { event = {} } = config
        const { onChange = emptyFunc } = event

        pagination.current = page
        onChange(pagination, filter, sorter)
    }

    render() {
        const { pagination = {}, title = emptyFunc, config = {}, selectConfig = {}, list:dataSource = [], loading } = this.props;

        const { size, event = {}, rowKey = ((record, index) => index) } = config
        const { isShowRowSelection, selectedRowKey, onSelectRow = emptyFunc, type } = selectConfig;
        const { current, pageSize, total } = pagination

        const { onClick = emptyFunc, onDoubleClick = emptyFunc } = event

        const columns = this.getColumnsFromProps();

        const rowSelection = {
            type: isNull(type, 'radio'),
            selectedRowKeys: [selectedRowKey],
            onChange: this.handleRowSelectChange,
        };
        return (
            <Table
                {...{ rowKey, size, loading, dataSource, columns }}
                bordered
                size='small'
                scroll={{x: 1}}
                pagination={false}
                rowSelection={isShowRowSelection ? rowSelection : null}
                onChange={this.handleTableChange}
                onRow={(record, index) => ({
                    onClick: () => {
                        onSelectRow(rowKey(record, index), record)
                        onClick(record, index)
                    },
                    onDoubleClick: () => {
                        onSelectRow(rowKey(record, index), record)
                        onDoubleClick(record, index);
                    }
                })}
                title={(data) =>
                    <Row type="flex" justify="space-between" align="middle">
                        <Col>{title(data)}</Col>
                        <Col>
                            <Pagination size="small"
                                        total={total}
                                        current={current}
                                        pageSize={pageSize || 0}
                                        showSizeChanger
                                        showQuickJumper
                                        pageSizeOptions={['10', '20', '50', '100']}
                                        showTotal={this.showTotal}
                                        onChange={this.handlePaginationChange}/>
                        </Col>
                    </Row>
                }
            />
        );
    }
}

export default SimpleTable;
