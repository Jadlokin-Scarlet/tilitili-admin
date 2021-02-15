import React from "react";
import {Spin} from "antd";
import {Chart, Interval, Legend, Line, Point, Tooltip} from "bizcharts";
import {isEmpty} from "../../../utils/HtmlUtils";

export default function newVideoCount(props) {
    const {newVideoCountList} = props

    const scale = {
        newVideoAdd: {
            type: 'linear-strict',
            alias: '视频增量',
        },
        newVideoNum: {
            type: 'linear-strict',
            alias: '视频数',
            min: newVideoCountList[0]?.newVideoNum || 0,
        },
    };
    const colors = ["#6394f9", "#62daaa"];
    const items = [
        {
            value: "newVideoNum",
            name: "视频数",
            marker: {
                symbol: "square",
                style: { fill: colors[0], r: 5 },
            },
        },
        {
            value: "newVideoAdd",
            name: "视频增量",
            marker: {
                symbol: "hyphen",
                style: { stroke: colors[1], r: 5, lineWidth: 3 },
            },
        },
    ];

    return (
        <Spin spinning={isEmpty(newVideoCountList)} style={{marginTop: 10}}>
            <Chart scale={scale} autoFit height={400} data={newVideoCountList}>
                <Legend custom allowAllCanceled items={items} />
                <Tooltip shared />
                <Interval position="time*newVideoNum" color={colors[0]} />
                <Line
                    position="time*newVideoAdd"
                    color={colors[1]}
                    size={3}
                    shape="smooth"
                />
                <Point
                    position="time*newVideoAdd"
                    color={colors[1]}
                    size={3}
                    shape="circle"
                />
            </Chart>
        </Spin>
    )
}