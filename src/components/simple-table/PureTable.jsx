import React, {PureComponent} from "react";
import { Table } from 'antd';

export default class PureTable extends PureComponent{

    // componentDidUpdate(prevProps, prevState, snapshot) {
    //     if (this.props.columns.length > 30) {
    //         console.log("main reload")
    //     }else if (this.props.columns.length > 20){
    //         console.log("sub reload");
    //     }
    //     for (const key of Object.keys(prevProps)) {
    //         if (prevProps[key] !== this.props[key]) {
    //             console.log(key)
    //         }
    //     }
    // }


    render() {
        return (
            <Table
                {...this.props}
            />
        )
    }

}