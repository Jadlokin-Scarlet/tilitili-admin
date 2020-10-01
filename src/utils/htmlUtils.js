import React from "react";
import {Button, Icon, Input, Popover, Typography} from "antd";
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



let searchInput;
export const _getColumnSearchProps = filteredInfo => ({
    filteredValue: filteredInfo || null,
    filterDropdown: ({ setSelectedKeys, confirm, clearFilters}) => (
        <div style={{ padding: 8 }}>
            <Input
                ref={node => {
                    searchInput = node
                }}
                placeholder={`Search`}
                onChange={e => setSelectedKeys(e.target.value ? [e.target.value.trim()] : [])}
                onPressEnter={() => {confirm()}}
                style={{ width: 188, marginBottom: 8, display: 'block' }}
            />
            <Button
                type="primary"
                onClick={() => {confirm()}}
                icon="search"
                size="small"
                style={{ width: 90, marginRight: 8 }}
            >
                Search
            </Button>
            <Button
                onClick={() => {clearFilters()}}
                size="small"
                style={{ width: 90 }}
            >
                Reset
            </Button>
        </div>
    ),
    filterIcon: filtered => (
        <Icon type="search" style={{ color: filtered ? '#1890ff' : undefined }} />
    ),
    onFilterDropdownVisibleChange: visible => {
        if (visible) {
            setTimeout(() => searchInput.select())
        }
    },
})
export const getColumnSearchProps = (filteredInfo, key) => _getColumnSearchProps(filteredInfo[key])

export const _getColumnChooseProps = (filteredInfo, map, isFilter = true) => ({
    filterMultiple: false,
    filteredValue: filteredInfo || null,//根据filteredInfo决定筛选参数
    filters: isFilter? map: undefined,
    render: key => {
        if (isEmpty(map) || isNull(key)) {
            return '';
        }
        for (const obj of map) {
            if (key !== undefined && obj.value.toString() === key.toString()) {
                return getParagraph(obj.text);
            }
        }
    }
})
export const getColumnChooseProps = (filteredInfo, key, map, isFilter) => _getColumnChooseProps(filteredInfo[key], map, isFilter)

export const getColumnOrderProps = (sorter, key) => ({
    sorter: true,
    sortOrder: sorter.field === key? (sorter.order || null) : null,
})

export function getParagraph(text, smallText) {
    return (
        <Popover placement="right" content={text}>
            <Paragraph ellipsis style={{marginBottom: "0em"}}>{smallText || text}</Paragraph>
        </Popover>
    )
}

const getSimpleFilteredInfo = (filteredInfo, key, value) => {
    let newFilterInfo = {}
    newFilterInfo[key] = [value];
    return Object.assign({}, filteredInfo, newFilterInfo);
}

export const InputTitle = (props) => {
    const {title, filteredInfo, index, style, onChanged, onChange} = props;
    const value = filteredInfo[index]
    return <div>
        <Input size="small" placeholder={title} value={value} style={style} allowClear
               onPaste={e=>{
                   e.preventDefault();
                   const newFilteredInfo = getSimpleFilteredInfo(filteredInfo, index, e.clipboardData.getData('Text'));
                   onChange(newFilteredInfo)
                   onChanged(newFilteredInfo)
               }}
               onPressEnter={e=>onChanged(getSimpleFilteredInfo(filteredInfo, index, e.target.value))}
               onChange={e=>onChange(getSimpleFilteredInfo(filteredInfo, index, e.target.value))}
        />
    </div>
}
export const Img = (props) => {
    return <img src={props.loading?"/loading.png":props.src} style={props.style} alt={props.alt} onClick={props.onClick}/>
}

export const getSmallHaihuImg = (loading, hrefConverse, onClick) => {
    const converseUrl = src => isEmpty(src)? "/loading.png":
        src.includes("http")? src:
            'http://img.haihu.com/' + src + "@1c_1e_80w"
    return {
        className: "no-padding",
        render: (src, row) => getParagraph(
            <Img loading={loading} style={{width: '80px', height: '80px'}} alt='-' src={converseUrl(src)} onClick={onClick}/>,
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

export const getInputTitle = (title, index, filteredInfo, onChanged, onChange, style) => ({
    title: <InputTitle
        {...{title, index, filteredInfo, onChanged, onChange, style}}
    />,
    filteredValue: filteredInfo[index] || null,
    className: "no-padding",
})

export const getProductSkuHtml = (data) => {
    if (data === undefined || data === null || data === '') {
        return "无属性"
    }
    if (typeof data !== 'string') {
        return data
    }
    if (data.endsWith("@")) {
        data = data.slice(0, -1);
    }
    if (data.startsWith("[") && (typeof JSON.parse(data) === "object")) {
        let textList = JSON.parse(data).map(obj => obj[0] + '(' + obj[1] + ')')
        return <Popover placement="right"
                        content={<div>{textList.slice(1).map(text => <p key={text} style={{marginBottom: "0em"}}>{text}</p>)}</div>}>
            <p style={{marginBottom: "0em"}}>{textList[0]}</p>
        </Popover>
    }else if (data.indexOf("<br>") !== -1) {
        let textList = data.split("<br>");
        return <Popover placement="right"
                        content={<div>{textList.slice(1).map(text => <p key={text} style={{marginBottom: "0em"}}>{text}</p>)}</div>}>
            <p style={{marginBottom: "0em"}}>{textList[0]}</p>
        </Popover>
    }else {
        return (
            <Paragraph ellipsis={{rows: 1, expandable: true, symbol: 'more'}}
                       style={{marginBottom: "0em"}}>
                {data}
            </Paragraph>
        );
    }
}
//注意，此工具会会渲染每个条件的全部元素，注意性能和空值处理
const runFunction = func => typeof func === "function"? func(): func;
export function If(boolean) {
    return {
        then: trueEle => {
            return {
                endIf: () => boolean? runFunction(trueEle): undefined,
                else: falseEle => boolean? runFunction(trueEle): runFunction(falseEle),
                elseIf: boolean2 => {
                    return {
                        then: falseEle => {
                            return {
                                endIf: () => boolean? runFunction(trueEle): (boolean2? runFunction(falseEle): undefined),
                            }
                        }
                    }
                }
            }
        }
    }
}

export const For = list => ({
    then: mapFun => list.map(mapFun).map((item, index) => <item key={index}/>),
})