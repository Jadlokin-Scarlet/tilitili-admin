import Button from 'antd/lib/button';
import QueueAnim from 'rc-queue-anim';
import * as React from "react";

export default class Test1 extends React.Component{
    state = {
        show: true
    };
    onClick = () => {
        this.setState({
            show: !this.state.show
        });
    }
    render() {
        return (
            <div className="queue-demo">
                <p className="buttons">
                    <Button type="primary" onClick={this.onClick}>Switch</Button>
                </p>
                <div className="demo-thead" key="a">
                <QueueAnim className="demo-content" component="ul">
                    {this.state.show ?
                        <>
                            <li key={1}/>
                            <li key={2}/>
                            <li key={3}/>
                        </>
                        :
                        <>
                            <li key={4}>123</li>
                            <li key={5}>3123</li>
                            <li key={6}>3213</li>
                        </>
                    }
                </QueueAnim>
                </div>
            </div>
        );
    }
}