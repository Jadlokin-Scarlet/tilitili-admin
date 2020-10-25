import {defineProperty} from "./HtmlUtils";

const list = ['f','Z','o','d','R','9','X','Q','D','S','U','m','2','1','y','C','k','r','6','z','B','q','i','v','e','Y','a','h','8','b','t','4','x','s','W','p','H','n','J','E','7','j','L','5','V','G','3','g','u','M','T','K','N','P','A','w','c','F'];
const map = list.map((char, index) => defineProperty({}, char, index)).reduce(Object.assign)
export const bvToAv = bv => {
    return bv.split("").map(item => map[item]).reverse()
        .map((item, index) => item * Math.pow(list.length, index))
        .reduce((a, b) => a + b);
}