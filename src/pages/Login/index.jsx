import React,{Component} from "react"
import {Card, Layout} from "antd"
import {Head} from "./Head";
import {Left} from "./Left";
import {Body} from "./Body";
import {Foot} from "./Foot";

const { Header, Content, Footer, Sider } = Layout;

export class Login extends Component{


    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        };
    }

    render() {
        return (
            <Layout style={{position: 'absolute', height: "100%", width: "100%"}}>
                <Header className="login-header">
                    <Head/>
                </Header>
                <Layout>
                    <Sider>
                        <Left/>
                    </Sider>
                    <Layout>
                        <Content>
                            <Body/>
                        </Content>
                        <Footer>
                            <Foot/>
                        </Footer>
                    </Layout>
                </Layout>
            </Layout>
        )
    }

    // handleSubmit = (event) => {
    //     // // 阻止事件的默认行为
    //     event.preventDefault()
    //
    //     this.props.form.validateFields(async (err, values) => {
    //         // 检验通过
    //         if (!err) {
    //             const {username,password} = values
    //             const result = await reqLogin(username,password)
    //             if(result.success){
    //                 // 登录成功提示
    //                 message.success("登录成功")
    //
    //                 // 保存user信息
    //                 memoryUtils.user = result     // 保存在内存中
    //                 storageUtils.saveUser(result)  // 保存在Local中
    //
    //                 // 跳转到管理首页
    //                 this.props.history.replace("/")
    //             }else{
    //                 message.error(result.message)
    //             }
    //         }else{
    //             console.log("校验失败")
    //         }
    //     });
    // }

}
