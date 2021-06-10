package com.tilitili.admin;

import com.tilitili.StartApplication;
import com.tilitili.common.manager.MiraiManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class MainTest {
    @Resource
    private MiraiManager miraiManager;
    @Test
    public void test() throws IOException {
        CloseableHttpClient httpclient = HttpClients.custom().build();

        String url = "http://saucenao.com/search.php?url=http://c2cpicdw.qpic.cn/offpic_new/545459363//545459363-3569422940-DF1C7BA04A74E973";
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        System.out.println(response);
    }
}
