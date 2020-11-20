import React from 'react';
import { Table } from 'antd';
import {
    isNull
} from "../../utils/HtmlUtils";

class SimpleTable extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedRowKeys: [],
        };
    }

    handleRowSelectChange = (selectedRowKeys, selectedRows) => {
        const { onSelectRow } = this.props;
        if (onSelectRow) {
            onSelectRow(selectedRows, selectedRowKeys);
        }
        this.setState({ selectedRowKeys });
    };

    render() {
        const { selectedRowKeys } = this.state;
        const { data, rowKey, scroll, size, isShowRowSelection, type, ...rest } = this.props;
        const { list = [], totalRows, pageSize, currentPage } = data;
        const pagination = {
            total: totalRows,
            pageSize: pageSize,
            current: currentPage
        };
        const paginationProps = {
            showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} items`,
            pageSizeOptions: this.props.pageSizeOptions || ['5', '10', '20', '50', '100'],
            position: this.props.position,
            showSizeChanger: true,
            showQuickJumper: true,
            ...pagination,
        };

        const rowSelection = {
            type: type?type:'radio',
            selectedRowKeys: this.props.selectedRowKeys || selectedRowKeys,
            onChange: this.handleRowSelectChange,
            getCheckboxProps: record => ({
                disabled: record.disabled,
            }),
        };

        return (
            <div>
                <Table
                    onRow={(record, index) => {
                        return {
                            onClick: () => {
                                this.handleRowSelectChange(
                                    [isNull(rowKey , (record, index) => index)(record, index)],
                                    [record]);
                                if (this.props.onClick) {
                                    this.props.onClick(record, index)
                                }
                            },
                            onDoubleClick: () => {
                                this.handleRowSelectChange(
                                    [isNull(rowKey , (record, index) => index)(record, index)],
                                    [record]);
                                if (this.props.onDoubleClick) {
                                    this.props.onDoubleClick(record, index);
                                }
                            }
                        }
                    }}
                    bordered
                    rowKey={rowKey || ((record, index) => index)}
                    rowSelection={isShowRowSelection ? rowSelection : null}
                    size={size}
                    scroll={scroll}
                    dataSource={list}
                    pagination={paginationProps}
                    {...rest}
                />

            </div>
        );
    }
}

export default SimpleTable;
