package com.tilitili.admin;

import com.tilitili.StartApplication;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class MainTest {
    @Resource
    private MiraiManager miraiManager;
    @Test
    public void test() throws IOException {
        BufferedImage source = ImageIO.read(new URL("http://gchat.qpic.cn/gchatpic_new/294195656/942271894-2616503631-62F8B6B006C042B0BC91C412CF6E3F9F/0?term=2"));
        BufferedImage result = FileUtil.pgm2png(source, "png");
        String s = FileUtil.md5Image(source, "png");
        String s1 = FileUtil.md5Image(result, "png");
        System.out.println(result);
    }
}
