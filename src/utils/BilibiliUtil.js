import {defineProperty} from "./HtmlUtils";
import {message} from "antd";
const xor=177451812
const add=8728348608
const link = [11,10,3,8,4,6]
const list = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF".split("");
const map = list.map((char, index) => defineProperty({}, char, index)).reduce((a, b) => Object.assign(a, b), {})
export const bvToAv = bv => {
    if (bv.length !== 12) {
        message.error(`bv号${bv}长度应为12，不对劲`)
        return null;
    }
    let av = link.map(i => bv[i]).map(item => map[item])
        .map((item, index) => item * Math.pow(list.length, index))
        .reduce((a, b) => a + b)
    av = (av - add) ^ xor;
    if (av.toString().length > 9) {
        message.error(`av号${av}长度超过9了，不对劲`)
        return null;
    }
    return av
}