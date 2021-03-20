import React, {Component} from "react";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {updateRecommendVideo} from "../../../api";

const formConfig = [
    {key: 'id', hidden: true},
    {key: 'status', value: -1, hidden: true},
    {key: 'name', submit: false},
]

export default class UpdateDeleteRecommendVideo extends Component {
    modalRender = state => {
        return <p>
            {`确定废弃[${state.name}]吗? 其下的推荐和自荐都将被移回池中`}
        </p>
    }

    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title='废弃'
                value='废弃'
                disabled={isEmptyObject(this.props.selectedRow)}
                formConfig={formConfig}
                updateApi={updateRecommendVideo}
                modalRender={this.modalRender}
            />
        )
    }
}