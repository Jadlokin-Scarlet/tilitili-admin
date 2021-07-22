package com.tilitili.admin.controller;

import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.manager.VideoDataManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Slf4j
@Controller
@RequestMapping("/api/run")
public class RunController extends BaseController{
    @Resource
    private VideoDataManager videoDataManager;

    @GetMapping("/shell")
    @ResponseBody
    public BaseModel runShell(String shell) throws IOException {
        Process p = Runtime.getRuntime().exec(shell);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return BaseModel.success(sb.toString());
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel run() {
        List<VideoData> dataList = videoDataManager.list(new VideoDataQuery().setIssue(videoDataManager.getNewIssue()).setPageSize(10000).setSorter("point", "desc"));
        log.info("获取数据 size="+dataList.size());
        List<VideoData> resultList = videoDataManager.list(new VideoDataQuery().setIssue(videoDataManager.getNewIssue() - 1).setPageSize(10000).setSorter("point", "desc"));
        log.info("获取结果数据 size="+resultList.size());
        Random random = new Random(System.currentTimeMillis());

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

        log.info("初始化完毕");
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
            log.info(minDcOfD + " " + minV);// + " " + firstData.a + " " + firstData.b + " " + firstData.c);
        }

        double result = 0;
        for (VideoData data : resultList) {
            double newPoint = data.getView() * ver.a + data.getReply() * ver.b + data.getFavorite() * ver.c + data.getCoin() * ver.d + data.getPage() * ver.e;
            double dPoint = data.getPoint() - newPoint;
            result += Math.abs(dPoint);
        }
        log.info(String.valueOf(result));
        return BaseModel.success(result);
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
