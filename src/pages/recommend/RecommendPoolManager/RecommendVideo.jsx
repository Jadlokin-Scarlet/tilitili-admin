import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {addRecommend} from "../../../api";

const formConfig = [
    {label: 'av', key: 'av', type: 'input'},
    {label: 'bv', key: 'bv', type: 'input'},
    {label: '推荐语', key: 'text', type: 'textArea', col: 2, autoSize: {minRows: 5}},
]

export default class RecommendVideo extends Component {
    render() {
        return (
            <DefaultModalButton
                {...this.props}
                col={2}
                title='推荐视频'
                value='推荐视频'
                formConfig={formConfig}
                updateApi={addRecommend}
            />
        )
    }
}