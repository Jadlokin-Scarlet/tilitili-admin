import React, {Component} from "react";
import { Modal } from "antd";
import "./DragModal.css";
import {isNull} from "../../utils/HtmlUtils";

// 获得随机数
const genNonDuplicateID = length => {
    return Number(
        Math.random()
            .toString()
            .substr(3, length) + Date.now()
    ).toString(36);
};

export default class DragModal extends Component {
    constructor(props) {
        super(props);

        this.id = genNonDuplicateID(10);

        if (isNull(props.width)) {
            //默认30%
            this.width = window.innerWidth * 0.3
        } else if (Number.isInteger(props.width)) {
            //如果是数字就默认px宽度
            this.width =  props.width;
        } else if (props.width.includes("%")) {
            //百分比宽度
            this.width = Number.parseFloat(props.width.split("%")[0]) / 100 * window.innerWidth
        }else {
            //px单位的硬编码宽度
            this.width = Number.parseFloat(props.width.replace("px", ""))
        }

        if (this.width >= window.innerWidth) {//超出屏幕宽度
            this.width = window.innerWidth;
        }

        this.initLeft = (window.innerWidth - this.width) / 2;

        this.dragDom = null; // 拖拽的目标元素
        this.dragging = false; // 是否拖拽的开关
        this.tLeft = 0; // ---|
        this.tTop = 0; //  ------> 坐标轴
    }

    componentDidMount() {
        this.initDragDom();
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.dragDom) {
            if (prevProps.visible === false && this.props.visible === true) {
                this.onShowModal();
            }
        }else {
            this.initDragDom();
        }
    }

    shouldComponentUpdate(nextProps, nextState, nextContext) {
        return this.props.visible || nextProps.visible;
    }

    /*
     * 初始渲染时，直接获取 Modal 的 dom 会获取不到。
     * 设置 ref 使用 findDOMNode 也获取不到。
     * 只能在定时器中使用原生方式来获取。
     * */
    initDragDom = () => {
        setTimeout(() => {
            // 获取唯一标示元素
            const dragDom = document.getElementsByClassName(`d_${this.id}`)[0];
            if (dragDom) {
                console.log(`d_${this.id} init`)
                dragDom.style.left = `${this.initLeft}px`;
                dragDom.style.width = `${this.width}px`
                dragDom.style.minWidth = "30%"
                dragDom.style.top = '100px'
                dragDom.style.zIndex = 1000;
                this.dragDom = dragDom;

            }
        });
    };

    onShowModal = () => {
        console.log(`d_${this.id} show`)
        //其他窗口降一层
        Array.from(document.getElementsByClassName("drag_modal")).forEach(item => (item.style.zIndex -= 1));
        this.dragDom.style.zIndex = 1000;
    }

    onMouseDown = e => {
        e.preventDefault();
        this.dragging = true; // 激活拖拽状态
        /*
        ** 将当前所有涉及可拖拽的浮层的 zindex = 999
        ** 将当前拖拽目标的 zindex = 1000
        **/
        Array.from(document.getElementsByClassName("drag_modal")).forEach(item => (item.style.zIndex -= 1));
        this.dragDom.style.zIndex = 1000;

        /*
        * getBoundingClientRect: 返回一个 DomRect 对象
        *   包含该元素的 top、right、bottom、left 值，对应的是到屏幕上方和左边的距离，单位 px
        * */
        const dragDomRect = this.dragDom.getBoundingClientRect();
        const left0 = e.clientX;
        const top0 = e.clientY;

        document.onmousemove = e => {
            e.preventDefault();
            if (this.dragging) {
                this.dragDom.style.left = `${dragDomRect.left + e.clientX - left0}px`;
                this.dragDom.style.top = `${dragDomRect.top + e.clientY - top0}px`;
            }
        };
    };

    onMouseUp = e => {
        e.preventDefault();
        this.dragging = false; // 停止移动状态
        document.onmousemove = null; // 停止鼠标移动事件
    };

    render() {
        const { children, title = "Drag-Modal", wrapClassName = "", isHidden=false, visible=false } = this.props;

        return (
            <Modal
                {...this.props}
                mask={false}
                width="100%"
                wrapClassName={`drag_modal d_${this.id} ${wrapClassName}`}
                ref={ele => this.modal = ele}
                visible={(!isHidden) && visible}
                title={
                    <div
                        className="drag_title"
                        onMouseDown={this.onMouseDown}
                        onMouseUp={this.onMouseUp}
                    >
                        {title}
                    </div>
                }
            >
                {children}
            </Modal>
        );
    }
}