package com.tilitili.admin;

import com.tilitili.StartApplication;
import com.tilitili.admin.controller.RedisController;
import com.tilitili.admin.service.RedisService;
import com.tilitili.common.emnus.RedisKeyEnum;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.PixivMoeManager;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.mapper.tilitili.AdminMapper;
import com.tilitili.common.mapper.tilitili.BatchTaskMapper;
import com.tilitili.common.mapper.tilitili.VideoInfoMapper;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Resource
    private VideoDataManager videoDataManager;
    @Resource
    private PixivMoeManager pixivMoeManager;
    @Resource
    private TaskManager taskManager;
    @Resource
    private RedisCache redisCache;
    @Resource
    private BatchTaskMapper batchTaskMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final int TIME_OUT = 10000;
    private static final CloseableHttpClient httpClient;


    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedisController redisController;
    @Resource
    private RedisService redisService;
    @Resource
    private VideoInfoMapper videoInfoMapper;

    @Test
    public void test4() {
        String key = "test";
//        redisTemplate.delete(key);
//        redisTemplate.opsForValue().set(key, 1);
//        Object value = redisTemplate.opsForValue().get("test");
//        System.out.println(value.getClass().getName());
//        System.out.println((Integer) value);
//
//        RedisView redisView = new Gson().fromJson("{\"key\": \""+key+"\", \"value\": 2}", new RedisView<>().setValue(value).getClass());
//        redisTemplate.opsForValue().set(key, redisView.getValue());
//        Object value2 = redisTemplate.opsForValue().get("test");
//        System.out.println(value2.getClass().getName());
//        System.out.println(value2.getClass().cast(value2));
//
//        Object value3 = redisTemplate.opsForValue().increment(key);
//        System.out.println(value3.getClass().getName());
//        System.out.println(value3.getClass().cast(value3));

        redisCache.addMapValue("SPIDER_PIXIV_PAGENO", "桐藤ナギサ", 0);
//        System.out.println(redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_PAGENO.getKey(), "桐藤ナギサ"));
    }

    static {
        RequestConfig config = RequestConfig.custom().setConnectTimeout(TIME_OUT).setSocketTimeout(TIME_OUT).setConnectionRequestTimeout(TIME_OUT).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(config).setRetryHandler(new StandardHttpRequestRetryHandler()).build();
    }
    @Test
    public void test() throws IOException {
        System.out.println(redisCache.increment(RedisKeyEnum.SPIDER_PIXIV_PAGENO.getKey(), "チルノ 東方Project100users入り", -1));
    }

    @Test
    public void test3() {
        videoInfoMapper.updateVideoInfoSelective(new VideoInfo().setAv(849784184L).setIsDelete(true));
    }
}