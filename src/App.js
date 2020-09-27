import React, {Component} from 'react';
import './App.css';
import 'antd/dist/antd.css'
import {If, isNull} from "./utils/htmlUtils";
import {Admin} from "./pages/Admin";
import {Login} from "./pages/Login";


class App extends Component{

    constructor(props) {
        super(props);
        this.state = {
            user: {}
        }
    }

    handleLogin = user => {
        this.setState({user})
    }

    handleLoginOut = () => {
        this.setState({user: {}})
    }

    render() {
        const { user } = this.state;
        return (
            If(isNull(user)).then(() => (<div></div>
                // <Admin
                //     user={user}
                //     onLoginOut={this.handleLoginOut}
                // />
            )).else(() => (
                <Login
                    user={user}
                    onLogin={this.handleLogin}
                />
            ))
        )
    }
}

export default App;
