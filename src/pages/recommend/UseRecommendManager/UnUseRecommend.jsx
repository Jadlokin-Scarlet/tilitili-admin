import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {unUseRecommend} from "../../../api";

const formConfig = [
    {key: 'id', hidden: true},
    {key: 'name', submit: false},
]

export default class UnUseRecommend extends Component {

    modalRender = state => {
        return <p>
            {`确定把推荐[${state.name}]送回推荐池`}
        </p>
    }

    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title='送回推荐池'
                value='送回推荐池'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={unUseRecommend}
                modalRender={this.modalRender}
            />
        )
    }
}