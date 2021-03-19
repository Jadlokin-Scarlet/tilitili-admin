import React, {Component} from "react";
import {isEmptyObject} from "../../../utils/HtmlUtils";
import {spiderVideo} from "../../../api";
import DefaultModalButton from "../../../components/default-modal-button/DefaultModalButton";

const formConfig = [
    {key: 'value', selectedIndex: 'av', hidden: true},
]

export default class SpiderVideo extends Component{
    modalRender = state => {
        return <p>
            {`确定爬取av${state.value}`}
        </p>
    }

    render() {
        return (
            <DefaultModalButton
                {...this.props}
                title='爬取信息'
                value='爬取信息'
                formConfig={formConfig}
                disabled={isEmptyObject(this.props.selectedRow)}
                updateApi={spiderVideo}
                modalRender={this.modalRender}
            />
        )
    }

}