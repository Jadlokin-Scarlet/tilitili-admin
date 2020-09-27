import React,{Component} from "react"
import moment from "moment";
export class Foot extends Component{


    constructor(props) {
        super(props);
        this.state = {
        };
    }

    render() {
        return (
            <div style={{ textAlign: 'center' }}>
                淘粉吧@{moment(new Date()).format("YYYY")} 海狐技术部出品
            </div>
        )
    }
}
