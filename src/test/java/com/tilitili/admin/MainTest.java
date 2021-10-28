package com.tilitili.admin;

import com.tilitili.StartApplication;
import com.tilitili.admin.service.mirai.FindImageHandle;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.PixivMoeManager;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

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
    @Resource
    private PixivMoeManager pixivMoeManager;
    @Resource
    private TaskManager taskManager;
    @Resource
    private RedisCache redisCache;

    private static final int TIME_OUT = 10000;
    private static final CloseableHttpClient httpClient;

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).setRetryHandler(new StandardHttpRequestRetryHandler()).build();
    }
    @Test
    public void test() throws IOException {
        System.out.println(redisCache.getMapValue(RedisKeyEnum.SPIDER_PIXIV_PAGENO.getKey(), "チルノ"));
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
