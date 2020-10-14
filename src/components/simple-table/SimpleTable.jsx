import React from 'react';
import { Table } from 'antd';
import {
    defineProperty,
    emptyFunc,
    getColumnChooseProps,
    getColumnOrderProps,
    getInputTitle,
    getParagraph,
    getSmallImg,
    isNull
} from "../../utils/htmlUtils";

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
        const columns = this.props.columns;
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
            .map(item => defineProperty({}, item[0], isNull(item[1])? item[1]: item[1][0]))
            .reduce((a, b) => Object.assign({}, a, b), {});

        onChange(pagination, filter, sorter)
    }

    render() {
        const { config = {}, selectConfig = {}, data = {}, loading } = this.props;

        const { size, event = {}, rowKey = ((record, index) => index) } = config
        const { isShowRowSelection, selectedRowKey, onSelectRow = emptyFunc, type } = selectConfig;
        const { list: dataSource=[], totalRows, pageSize, start } = data;

        const { onClick = emptyFunc, onDoubleClick = emptyFunc } = event

        const columns = this.getColumnsFromProps();

        const pagination = {
            position: "top",
            showSizeChanger: true,
            showQuickJumper: true,
            total: totalRows,
            pageSize: pageSize,
            current: start / pageSize + 1
        };

        const rowSelection = {
            type: isNull(type, 'radio'),
            selectedRowKeys: [selectedRowKey],
            onChange: this.handleRowSelectChange,
        };
        return (
            <Table
                {...{ rowKey, size, pagination, loading, dataSource, columns }}
                bordered
                size='small'
                scroll={{x: 1}}
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
            />
        );
    }
}

export default SimpleTable;
