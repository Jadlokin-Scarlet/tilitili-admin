import React from 'react';
import {Chart, Line, Point, Tooltip, Legend, Axis} from 'bizcharts';

const scale = {
    number: {
        min: 0,
        alias: '涨幅'
    },
    type: {
        formatter: v => {
            return {
                view: '播放量',
                reply: '评论',
                favorite: '收藏',
                coin: '硬币',
            }[v]
        }
    }
}

export default function VideoDataAddCount(props) {
    const {videoDataAddCountList} = props;

    return <Chart scale={scale} autoFit height={480} data={videoDataAddCountList}>
        <Point position="issue*number" color="type" shape='circle' />
        <Line shape="smooth" position="issue*number" color="type" label="number" />
        <Tooltip shared showCrosshairs />
        <Axis
            name="number"
            title
            label={{
                formatter: val => `${val}‱`
            }}
        />
        <Axis
            name="issue"
            label={{
                formatter: val => `${val}期`
            }}
        />
        <Legend background={{
            padding:[5,240,5,36],
            style: {
                fill: '#eaeaea',
                stroke: '#fff'
            }
        }} />
    </Chart>
}