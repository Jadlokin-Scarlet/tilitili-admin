import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {updateDeleteRecommend} from "../../../api";

const formConfig = [
    {key: 'id', hidden: true},
    {key: 'name', submit: false},
]

export default class DeleteRecommend extends Component {

    modalRender = state => {
        return <p>
            {`确定删除推荐: ${state.name}`}
        </p>
    }

    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title='废弃推荐'
                value='废弃推荐'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={updateDeleteRecommend}
                modalRender={this.modalRender}
            />
        )
    }
}