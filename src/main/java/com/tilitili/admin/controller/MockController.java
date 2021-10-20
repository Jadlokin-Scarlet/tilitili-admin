package com.tilitili.admin.controller;

import com.tilitili.admin.entity.mirai.TestRequest;
import com.tilitili.common.manager.MiraiManager;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.SSLContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/mock")
public class MockController {
    private final MiraiManager miraiManager;

    @Autowired
    public MockController(MiraiManager miraiManager) {
        this.miraiManager = miraiManager;
    }

    @RequestMapping("")
    public void mock(HttpServletRequest request, @RequestBody(required = false) String body) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        body = body == null? "": body;
        String cookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{})).map(c -> String.format("%s=%s", c.getName(), c.getValue())).collect(Collectors.joining("; "));
        miraiManager.sendFriendMessage("Plain", String.format("%s?%s\nbody=%s\ncookie=%s", requestURL, queryString, body, cookie));
    }

    @RequestMapping("/test")
    @ResponseBody
    public void get(@RequestBody TestRequest request) throws IOException {
        proxy_no_auth_socks("", "127.0.0.1:23334","https://www.pixiv.net/ajax/search/artworks/チルノ?word=チルノ&order=date_d&mode=all&p=6&s_mode=s_tag&type=all&lang=zh");
    }

    // HttpClient 支持socks5代理的自定义类
    static class MyConnectionSocketFactory extends PlainConnectionSocketFactory {
        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            InetSocketAddress unresolvedRemote = InetSocketAddress.createUnresolved(host.getHostName(), remoteAddress.getPort());
            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
        }
    }

    static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {
        public MySSLConnectionSocketFactory(final SSLContext sslContext) {
//          super(sslContext, ALLOW_ALL_HOSTNAME_VERIFIER);
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            InetSocketAddress unresolvedRemote = InetSocketAddress.createUnresolved(host.getHostName(), remoteAddress.getPort());
            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
        }
    }

    static class FakeDnsResolver implements DnsResolver {
        @Override
        public InetAddress[] resolve(String host) throws UnknownHostException {
            // Return some fake DNS record for every request, we won't be using it
            return new InetAddress[] { InetAddress.getByAddress(new byte[] { 1, 1, 1, 1 }) };
        }
    }

    // 无密代理, 支持 socks5
    public static void proxy_no_auth_socks(String proxyType, String proxyIpPort, String webUrl) {
        String proxy_ip = proxyIpPort.split(":")[0];
        int proxy_port = Integer.parseInt(proxyIpPort.split(":")[1]);

        try {
            Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", new MyConnectionSocketFactory())
                    .register("https", new MySSLConnectionSocketFactory(SSLContexts.createSystemDefault()))
                    .build();
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg, new FakeDnsResolver());
            CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).build();

            InetSocketAddress addr = new InetSocketAddress(proxy_ip, proxy_port);
            HttpClientContext context = HttpClientContext.create();
            context.setAttribute("socks.address", addr);

            HttpGet request = new HttpGet(webUrl);
            request.addHeader("Host","proxy.mimvp.com");
            request.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36");

            HttpResponse response = client.execute(request, context);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println(result);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
