import React, {Component} from "react";
import {Input} from "antd";
import {defineProperty, emptyFunc, isBlank} from "../../../utils/HtmlUtils";
import CacheInput from "../../../components/cache-input";

export default class ChooseIssue extends Component {
    setIssue = issue => {
        const {onChange=emptyFunc} = this.props;
        onChange(defineProperty({}, 'issue', [issue]))
    }
    render() {
        const {filters,disabled} = this.props;
        const issue = (filters.issue || [])[0];
        return (
            <span style={{marginLeft: 8}}>
                <CacheInput value={issue} suffix="期" width={70} size="normal" disabled={disabled} onChange={this.setIssue}/>
            </span>
        )
    }
}