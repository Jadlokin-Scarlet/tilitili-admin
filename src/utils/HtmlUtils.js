import React from "react";
import {Input, Popover, Typography, message, Select} from "antd";
const { Paragraph } = Typography;

export const isNull = (obj, or) => {
    if (or === undefined) {
        return obj === undefined || obj === null;
    }else {
        return obj === undefined || obj === null? or: obj;
    }
};
export const isNotNull = obj => !isNull(obj);
export const isEmpty = list => list === undefined || list === null || list.length === 0;
export const isNotEmpty = list =>  !isEmpty(list);
export const isEmptyObject = obj => isNull(obj) || Object.keys(obj).length === 0
export const isNotEmptyObject = obj => !isEmptyObject(obj)
export const isBlank = (string, or) => or === undefined
    ? string === undefined || string === null || string.length === 0
    : string === undefined || string === null || string.length === 0? or: string;
export const isNotBlank = (string, or) => or === undefined? !isBlank(string): !isBlank(string)? or: string

export const getColumnChooseProps = (filteredInfo, key, map, isFilter = true) => ({
    filterMultiple: false,
    filteredValue: isNull(filteredInfo[key])? null : [filteredInfo[key]],//根据filteredInfo决定筛选参数
    filters: isFilter? map: undefined,
    render: key => {
        for (const obj of (map || [])) {
            if (obj.value === key) {
                return obj.text;
            }
        }
    }
})

export const getColumnOrderProps = (sorter, key) => ({
    sorter: true,
    sortDirections: ['descend', 'ascend', ],
    sortOrder: sorter.field === key? (sorter.order || null) : null,
})

export function getParagraph(text, smallText) {
    return (
        <Popover placement="right" content={text}>
            <Paragraph ellipsis style={{marginBottom: "0em"}}>{smallText || text}</Paragraph>
        </Popover>
    )
}

export const InputTitle = (props) => {
    const {title, filteredInfo, index, style, onChanged, onChange} = props;
    return <div>
        <Input size="small" placeholder={title} value={filteredInfo[index]} style={style} allowClear
               onPaste={e=>{
                   e.preventDefault();
                   const newFilteredInfo = defineProperty(filteredInfo, index, e.clipboardData.getData('Text'));
                   onChange(newFilteredInfo)
                   onChanged(newFilteredInfo)
               }}
               onPressEnter={e=>onChanged(defineProperty(filteredInfo, index, e.target.value))}
               onChange={e=> {
                   const newFilteredInfo = defineProperty(filteredInfo, index, e.target.value);
                   onChange(newFilteredInfo)
                   if (isBlank(e.target.value)) {
                       onChanged(newFilteredInfo)
                   }
               }}
        />
    </div>
}
export const Img = (props) => {
    return <img src={props.loading?"/loading.png":props.src} style={props.style} alt={props.alt} onClick={props.onClick}/>
}

export const getSmallImg = (loading, hrefConverse, onClick) => {
    const converseUrl = src => isEmpty(src)? "/loading.png": src;
    return {
        width: 60,
        className: "no-padding",
        render: (src, row={}) => getParagraph(
            <Img loading={loading} style={{height: '400px'}} alt='-' src={converseUrl(src)} onClick={onClick}/>,
            If(isNull(hrefConverse)).then(() =>
                <Img loading={loading} style={{height: '37px', maxWidth: '60px'}} alt='-' src={converseUrl(src)} onClick={onClick}/>
            ).else(() =>
                <a href={hrefConverse(row)} target="_Blank" rel="noopener noreferrer">
                    <Img loading={loading} style={{ height: '37px', maxWidth: '60px' }} src={converseUrl(src)} alt="-"/>
                </a>
            )
        )
    }
}

export const InputSelect = (props) => (
    <Select value={isNull(props.value)? props.value: props.value.toString()} onChange={props.onChange} onSelect={props.onSelect} showSearch autoClearSearchValue allowClear dropdownMatchSelectWidth optionFilterProp="children" filterOption={inputSelectFilterOption} placeholder={props.placeholder} size={props.size}>
        {For(props.resource).then((item, index) => (
            <Select.Option key={index} value={isNull(item.value)? item.value: item.value.toString()}>{isNull(item.text)? item.text: item.text.toString()}</Select.Option>
        ))}
    </Select>
)

export const inputSelectFilterOption = (input, option) => {
    return option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
}

export const getInputTitle = (title, index, filteredInfo, onChanged, onChange, style) => ({
    title: <InputTitle
        {...{title, index, filteredInfo, onChanged, onChange, style}}
    />,
    filteredValue: isNull(filteredInfo[index])? null : [filteredInfo[index]],
    className: "no-padding",
})

//注意，此工具会会渲染每个条件的全部元素，注意性能和空值处理
const runFunction = func => typeof func === "function"? func(): func;
export function If(boolean) {
    return new IfClass(boolean);
}

export class IfClass {
    constructor(boolean) {
        this.boolean = boolean;
        this.result = null;
    }

    if (boolean) {
        this.boolean = boolean;
        return this;
    }

    then (result) {
        if (this.boolean && this.result == null) {
            this.result = result;
        }
        return this;
    }

    elseIf (boolean) {
        this.boolean = boolean;
        return this;
    }

    else (result) {
        if (this.result != null) {
            return runFunction(this.result);
        }else {
            return runFunction(result);
        }
    }

    endIf () {
        return runFunction(this.result);
    }
}

export const For = object => {
    if (Array.isArray(object)) {
        return new ForArray(object);
    }
    if (typeof object === 'object') {
        return new ForObject(object);
    }
    return new ForArray([]);
}

class ForArray {
    constructor(list) {
        this.list = list;
    }
    then(mapFun) {
        return this.list.map(mapFun)
    }
    if(filterFun) {
        this.list.filter(filterFun)
        return this;
    }
}

class ForObject {
    constructor(object) {
        this.object = object;
    }
    then(mapFun) {
        const tmp = {};
        for (const key of Object.keys(this.object)) {
            tmp[key] = mapFun(key, this.object[key]);
        }
        return tmp;
    }
    if(filterFun) {
        const tmp = {};
        for (const key of Object.keys(this.object)) {
            if (filterFun(key, this.object[key])) {
                tmp[key] = this.object[key];
            }
        }
        this.object = tmp;
        return this;
    }
}


export const convertToPrams = (pagination = {}, filters = {}, sorter = {}) => {
    filters = For(filters).if((key,value) => isNotNull(value))
        .then((key, value) => value[0]);
    const params = Object.assign({}, filters, pagination);
    if (isNotEmptyObject(sorter)) {
        params.sorter = toLine(sorter.field)
        params.sorted = sorter.order === 'descend' ? 'desc' : 'asc';
    }
    // return {query: params};
    return params
}

export const emptyFunc = () => {}
export const selfFunc = a => a

export const compose = (firstFun = selfFunc, secondFun = selfFunc) => {
    return (...props) => secondFun(firstFun(...props));
}

function currying(fn) {
    return function _currying(...args) {
        if (args.length >= fn.length) {
            return fn(...args)
        }
        return function (...args2) {
            return _currying(...args, ...args2)
        }
    }
}

export const defineProperty = (obj, key, value) => {
    return Object.defineProperty(obj, key, {
        enumerable: true,
        configurable: true,
        value
    })
}

export const toLine = (name) => {
    return name.replace(/([A-Z])/g,"_$1").toLowerCase();
}

export const splitToList = (key, text) => {
    if (isNotNull(text)) {
        return text.split(key).map(text => <p key={text} style={{marginBottom: "0em"}}>{text}</p>);
    }
}

export const splitToListRender = currying(splitToList)

export const priceRender = currying((unit, text) => {
    if (isNotNull(text)) {
        return unit + text
    }
})

export const checkResp = data => {
    if (isNull(data)) {
        data = {};
    }
    if (isNull(data.success)) {
        data.success = true;
    }
    if(!data.success) {
        message.error(isBlank(data.message, '失败'));
        return data;
    } else {
        return data;
    }
}

export const defaultRowKey = (record, index) => index;

export const defaultScroll = {x:1}