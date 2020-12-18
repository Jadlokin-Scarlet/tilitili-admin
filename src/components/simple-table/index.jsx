import React, {PureComponent} from 'react';
import {defaultRowKey, emptyFunc} from "../../utils/HtmlUtils";
import PureTable from "./PureTable";

export default class SimpleTable extends PureComponent {

    handleSelectRow = (record, index, callback) => {
        const {rowKey=defaultRowKey,isMultipleSelect,onSelectRow=emptyFunc} = this.props;
        const selectedRowKey = rowKey(record, index);

        let {selectedRowKeys,selectedRows} = this.props;

        if (isMultipleSelect !== true) {
            selectedRowKeys = [selectedRowKey];
            selectedRows = [record]
        }else {
            const j = selectedRowKeys.indexOf(selectedRowKey);
            if (j === -1) {
                selectedRows.push(record);
                selectedRowKeys.push(selectedRowKey);
            }else {
                selectedRows.splice(j, 1);
                selectedRowKeys.splice(j, 1);
            }
        }
        onSelectRow(selectedRowKeys, selectedRows, callback)
    }

    handleClick = (record, index) => {
        const {onClick=emptyFunc} = this.props;
        this.handleSelectRow(record, index, () => {
            onClick(record, index);
        });
    }

    handleDoubleClick = (record, index) => {
        const {onDoubleClick=emptyFunc} = this.props;
        onDoubleClick(record, index)
    }

    paginationProps = {
        showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} items`,
        pageSizeOptions: this.props.pageSizeOptions || ['5', '10', '20', '50', '100'],
        position: this.props.position,
        showSizeChanger: true,
        showQuickJumper: true,
    }

    onClick = (record, index) => {
        return {
            onClick: () => {
                setTimeout(this.handleClick.bind(this, record, index), 0);
            },
            onDoubleClick: () => {
                setTimeout(this.handleDoubleClick.bind(this, record, index), 0);
            }
        }
    }

    render() {
        const { data, rowKey, scroll, size} = this.props;
        const { list, total, pageSize, current } = data;

        this.paginationProps.pageSize = pageSize;
        this.paginationProps.total = total;
        this.paginationProps.current = current;

        return (
            <PureTable
                columns={this.props.columns}
                bordered
                rowKey={rowKey || defaultRowKey}
                size={size}
                scroll={scroll}
                dataSource={list}
                pagination={this.paginationProps}
                loading={this.props.loading}
                onChange={this.props.onChange}
                rowClassName={this.props.rowClassName}
                onRow={this.onClick}
            />
        );
    }
}
