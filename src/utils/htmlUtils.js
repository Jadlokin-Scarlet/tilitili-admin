import React from "react";
import { Input, Popover, Typography} from "antd";
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
                return getParagraph(obj.text);
            }
        }
    }
})

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
               onChange={e=>onChange(defineProperty(filteredInfo, index, e.target.value))}
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
        render: (src, row) => getParagraph(
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

export const For = (list = []) => ({
    then: mapFun => list.map(mapFun),
    if: filterFun => ({
        then: mapFun => list.filter(filterFun).map(mapFun),
    }),
})

export const convertToPrams = (pagination = {}, filters = {}, sorter = {}) => {
    const params = Object.assign({}, filters);
    if (isNotEmptyObject(pagination)) {
        params.start = (pagination.current - 1) * pagination.pageSize;
        params.pageSize = pagination.pageSize;
    }
    if (isNotEmptyObject(sorter)) {
        params.sorter = sorter.field
        params.sorted = sorter.order === 'descend' ? 'desc' : 'asc';
    }
    // return {query: params};
    return params
}

export const emptyFunc = () => {}

export const defineProperty = (obj, key, value) => {
    return Object.defineProperty(obj, key, {
        enumerable: true,
        configurable: true,
        value
    })
}