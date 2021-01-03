import React, {Component} from "react";
import {Select} from "antd";
import {For, inputSelectFilterOption, isNull} from "../../utils/HtmlUtils";

export default class SelectInput extends Component{
    constructor(props) {
        super(props);
        this.state = {
            scrollPage: 1,
            // keyWords: '',
            // optionData: [],
        }
    }
    handleScroll = e => {
        e.persist();
        const { target } = e;
        // scrollHeight：代表包括当前不可见部分的元素的高度
        // scrollTop：代表当有滚动条时滚动条向下滚动的距离，也就是元素顶部被遮住的高度
        // clientHeight：包括padding但不包括border、水平滚动条、margin的元素的高度
        const rmHeight = target.scrollHeight - target.scrollTop;
        const clHeight = target.clientHeight;
        // 当下拉框失焦的时候，也就是不下拉的时候
        if (rmHeight === 0 && clHeight === 0) {
            this.setState({ scrollPage: 1 });
        } else {
            // 当下拉框下拉并且滚动条到达底部的时候
            // 可以看成是分页，当滚动到底部的时候就翻到下一页
            if (rmHeight < clHeight + 5) {
                const { scrollPage } = this.state;
                this.setState({ scrollPage: scrollPage + 1 });
                //调用处理数据的函数增加下一页的数据
                // this.loadOption(scrollPage + 1);
            }
        }
    };

    handleBlur = () => {
        this.setState({ scrollPage: 1 });
    }

    render() {
        const {width, placeholder, onChange, onSelect, filters=[], disabled, size, value, mode} = this.props;

        const someFilters = filters.filter((filter, index) => index < this.state.scrollPage * 100)

        return (
            <div style={{width: `${width}px`}}>
                <Select showSearch allowClear autoClearSearchValue dropdownMatchSelectWidth optionFilterProp="children" size={size} value={value} placeholder={placeholder} onChange={onChange} onSelect={onSelect}  disabled={disabled} mode={mode}
                        filterOption={inputSelectFilterOption}
                        onPopupScroll={this.handleScroll}
                        onBlur={this.handleBlur}
                >
                    {For(someFilters).then((item, index) => (
                        <Select.Option key={index} value={isNull(item.value)? item.value: item.value.toString()}>
                            {isNull(item.text)? item.text: item.text.toString()}
                        </Select.Option>
                    ))}
                </Select>
            </div>
        )
    }

}