import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommend} from "../../../api";

const formConfig = [
    {key: 'id', hidden: true},
    {label: 'av号', key: 'av', type: 'input', disable: true},
    {label: '推荐人', key: 'operator', type: 'input'},
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
                formConfig={formConfig}
                updateApi={updateRecommend}
            />
        )
    }
}