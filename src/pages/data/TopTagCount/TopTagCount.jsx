import React from 'react';
import _ from 'lodash';
import {
    Chart,
    Geom,
    Tooltip,
    Coordinate,
    Legend,
    Interaction,
    G2,
    registerShape, Axis
} from "bizcharts";
import DataSet from "@antv/data-set";
import {Card} from "antd";

// 给point注册一个词云的shape

function getTextAttrs(cfg) {
    return _.assign(
        {},
        cfg.style,
        {
            fontSize: cfg.data.size,
            text: cfg.data.text,
            textAlign: 'center',
            fontFamily: cfg.data.font,
            fill: cfg.color,
            textBaseline: 'Alphabetic'
        }
    );
}
registerShape("point", "cloud", {
    draw(cfg, container) {
        console.log('cloud cfg', cfg);
        const attrs = getTextAttrs(cfg);
        const textShape = container.addShape("text", {
            attrs: _.assign(attrs, {
                x: cfg.x,
                y: cfg.y
            })
        });
        if (cfg.data.rotate) {
            G2.Util.rotate(textShape, cfg.data.rotate * Math.PI / 180);
        }
        return textShape;
    }
});

export default function TopTagCount(props) {
    const {topTagList} = props;
    const dv = new DataSet.View().source(topTagList);
    const range = dv.range('number');
    const min = range[0];
    const max = range[1];
    dv.transform({
        type: 'tag-cloud',
        fields: ['tag', 'number'],
        size: [600, 500],
        font: 'Verdana',
        padding: 0,
        timeInterval: 5000, // max execute time
        rotate() {
            let random = ~~(Math.random() * 4) % 4;
            if (random === 2) {
                random = 0;
            }
            return random * 90; // 0, 90, 270
        },
        fontSize(d) {
            if (d.number) {
                return ((d.number - min) / (max - min)) * (40 - 12) + 12;
            }
            return 0;
        }
    });
    const scale = {
        x: {
            nice: false
        },
        y: {
            nice: false
        }
    };
    return (
        <Card>
            <Chart autoFit
                   height={480}
                   data={dv.rows}
                   scale={scale}
                   padding={0}
            >
                <Tooltip showTitle={false} />
                <Coordinate reflect="y" />
                <Axis name='x' visible={false} />
                <Axis name='y' visible={false} />
                <Legend visible={false} />
                <Geom
                    type='point'
                    position="x*y"
                    color="tag"
                    shape="cloud"
                    tooltip="number*tag"
                />
                <Interaction type='element-active' />
            </Chart>
        </Card>
    );
}