import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommend} from "../../../api";
import {isEmptyObject} from "../../../utils/HtmlUtils";

const formConfig = [
    {key: 'id', hidden: true},
    {label: 'av号', key: 'av', type: 'input', disable: true},
    {label: '推荐人', key: 'operator', type: 'input'},
    {label: '开始展示时间', key: 'startTime', type: 'input', suffix: '秒'},
    {label: '原作者', key: 'externalOwner', type: 'input'},
    {label: '推荐语', key: 'text', type: 'textArea', col: 2, autoSize: {minRows: 5}}
]

export default class UpdateRecommend extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                col={2}
                title='编辑'
                value='编辑'
                disabled={isEmptyObject(this.props.selectedRow)}
                formConfig={formConfig}
                updateApi={updateRecommend}
            />
        )
    }
}