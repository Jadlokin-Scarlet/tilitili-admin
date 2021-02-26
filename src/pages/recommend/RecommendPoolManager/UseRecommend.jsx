import React, {Component} from "react";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {useRecommend} from "../../../api";

const formConfig = [
    {key: 'id', hidden: true},
    {key: 'name', submit: false},
]

export default class UseRecommend extends Component {

    modalRender = state => {
        const {recommendIssueResource=[]} = this.props.resources;
        const newIssue = recommendIssueResource[0]?.value;
        return <p>
            {`确定使用推荐至第${newIssue}期: ${state.name}`}
        </p>
    }

    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title='使用推荐'
                value='使用推荐'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={useRecommend}
                modalRender={this.modalRender}
            />
        )
    }
}