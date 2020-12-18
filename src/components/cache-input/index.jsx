import React, {Component} from "react";
import {Input} from "antd";
import {isBlank} from "../../utils/HtmlUtils";

export default class CacheInput extends Component {
    constructor(props) {
        super(props);
        this.state = {
            cacheValue: props.value,
            isEdit: false,
        }
    }

    startEdit = () => {
        this.setState({isEdit: true, cacheValue: this.props.value})
    }

    endEdit = () => {
        this.setState({isEdit: false, cacheValue: this.props.value})
    }

    change = e => {
        this.setState({cacheValue: e.target.value})
        if (! this.state.isEdit) {
            this.props.onChange(isBlank(e.target.value, null))
        }
    }

    submit = () => {
        this.props.onChange(this.state.cacheValue);
    }

    paste = e => {
        e.preventDefault();
        const value = e.clipboardData.getData('Text');
        this.setState({cacheValue: value})
        this.props.onChange(value);
    }

    render() {
        const {placeholder, value, width} = this.props;
        const {cacheValue, isEdit} = this.state;
        return (
            <Input
                size="small"
                allowClear
                placeholder={placeholder}
                value={isEdit? cacheValue: value}
                style={{width: `${width}px`}}
                onFocus={this.startEdit}
                onBlur={this.endEdit}
                onChange={this.change}
                onPressEnter={this.submit}
                onPaste={this.paste}
            />
        )
    }
}