package com.tilitili.admin.service.mirai;

import com.google.common.collect.ImmutableMap;
import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class FindImageHandle implements BaseMessageHandle{
    @Override
    public List<String> getKeyword() {
        return Arrays.asList("找图", "zt");
    }

    @Override
    public String getDescription() {
        return "查找原图";
    }

    @Override
    public String getSendType() {
        return "friend";
    }

    @Override
    public MiraiMessage handleMessage(MiraiRequest request) {
        MiraiMessage result = new MiraiMessage();
        String url = request.getUrl();
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
        return result.setMessage(String.format("找到啦😊！相似度%s\n%s", rate, link)).setMessageType("Plain");
    }
}
