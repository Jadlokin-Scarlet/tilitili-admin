package com.tilitili.admin;

import com.google.common.collect.ImmutableMap;
import com.tilitili.StartApplication;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.mirai.FindImageHandle;
import com.tilitili.common.emnus.GroupEmum;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.FileUtil;
import com.tilitili.common.utils.HttpClientHelper;
import com.tilitili.common.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class MainTest {
    @Resource
    private MiraiManager miraiManager;
    @Resource
    private VideoDataManager videoDataManager;
    @Resource
    private FindImageHandle findImageHandle;

    private static final int TIME_OUT = 10000;
    private static final CloseableHttpClient httpClient;

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).setRetryHandler(new StandardHttpRequestRetryHandler()).build();
    }
    @Test
    public void test() throws IOException {
        MiraiMessage result = new MiraiMessage();
        String url = "http://c2cpicdw.qpic.cn/offpic_new/545459363//545459363-3613474805-9AB7E35963A95437F2B081CC05B164F5/0?term=2";
        Asserts.notBlank(url, "格式错啦(图片)");
        String html = HttpClientUtil.httpPost("https://saucenao.com/search.php?url="+url, ImmutableMap.of());
        Asserts.notBlank(html, "没要到图😇\n"+url);
        Document document = Jsoup.parse(html);
        Elements imageList = document.select(".result:not(.hidden):not(#result-hidden-notification)");
        Asserts.isFalse(imageList.isEmpty(), "没找到🤕\n"+url);
        Element image = imageList.get(0);
        String rate = image.select(".resultsimilarityinfo").text();
        Elements linkList = image.select(".resultcontentcolumn a.linkify");
        String link = linkList.get(0).attr("href");
        System.out.println(result.setMessage(String.format("找到啦😊！相似度%s\n%s", rate, link)).setMessageType("Plain"));
    }



    @Test
    public void test3() {
        VideoData videoData = videoDataManager.getByAvAndIssue(706L, 27);
        System.out.println(fun(videoData));
    }

    public static Double fun(VideoData data) {
        ;
        double a=0.000999999999327989;
        double b=0.005999999999998619;
        double c=-0.015000000000000457;
        double d=0.002999999998994034;
        double e=-0.33499999999978086;
        double newPoint = data.getView() * a + data.getReply() * b + data.getFavorite() * c + data.getCoin() * d + data.getPage() * e;
        return newPoint;
    }

    @Test
    public void test2() {
        List<VideoData> dataList = videoDataManager.list(new VideoDataQuery().setIssue(videoDataManager.getNewIssue()).setPageSize(10000).setSorter("point", "desc"));
        List<VideoData> resultList = videoDataManager.list(new VideoDataQuery().setIssue(videoDataManager.getNewIssue() - 1).setPageSize(10000).setSorter("point", "desc"));

        double a=0.000999999999327989;
        double b=0.005999999999998619;
        double c=-0.015000000000000457;
        double d=0.002999999998994034;
        double e=-0.33499999999978086;
        Ver ver = new Ver(a, b, c, d, e);

        ArrayList<Ver> verList = new ArrayList<>();
        for (Double da : Arrays.asList(0D, 0.00001D, -0.00001D))
            for (Double db : Arrays.asList(0D, 0.00001D, -0.00001D))
                for (Double dc : Arrays.asList(0D, 0.00001D, -0.00001D))
                    for (Double dd : Arrays.asList(0D, 0.00001D, -0.00001D))
                        for (Double de : Arrays.asList(0D, 0.00001D, -0.00001D))
                            verList.add(new Ver(da, db, dc, dd, de));



        for (int i = 0; i < 10000; i++) {
            double minDcOfD = Integer.MAX_VALUE;
            Ver minV = null;
            for (Ver dv : verList) {
                Ver v = ver.add(dv);

                double avgDPoint = 0;
                for (VideoData data : dataList) {
                    double newPoint = data.getView() * v.a + data.getReply() * v.b + data.getFavorite() * v.c + data.getCoin() * v.d + data.getPage() * v.e;
                    double dPoint = data.getPoint() - newPoint;
                    avgDPoint += Math.abs(dPoint);
                }
                avgDPoint = Math.abs(avgDPoint) / dataList.size();

                if (avgDPoint < minDcOfD) {
                    minDcOfD = avgDPoint;
                    minV = v;
                }
            }

            ver = minV;
            System.out.println(minDcOfD + " " + minV);// + " " + firstData.a + " " + firstData.b + " " + firstData.c);
        }

        double result = 0;
        for (VideoData data : resultList) {
            double newPoint = data.getView() * ver.a + data.getReply() * ver.b + data.getFavorite() * ver.c + data.getCoin() * ver.d + data.getPage() * ver.e;
            double dPoint = data.getPoint() - newPoint;
            result += Math.abs(dPoint);
        }
        System.out.println(result);

    }
}

class Ver {
    public double a;
    public double b;
    public double c;
    public double d;
    public double e;

    public Ver(double a, double b, double c, double d, double e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    public Ver add(Ver otherVer) {
        return new Ver(a + otherVer.a, b + otherVer.b, c + otherVer.c, d + otherVer.d, e + otherVer.e);
    }

    @Override
    public String toString() {
        return "Ver{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                '}';
    }
}
