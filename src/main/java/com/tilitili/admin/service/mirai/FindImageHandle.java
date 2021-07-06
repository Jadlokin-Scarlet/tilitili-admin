package com.tilitili.admin.service.mirai;

import com.google.common.collect.ImmutableMap;
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
    public String handleMessage(MiraiMessageView message, Map<String, String> map) {
        String url = map.get("url");
        Asserts.notBlank(url, "格式错啦(图片)");
        String html = HttpClientUtil.httpPost("https://saucenao.com/search.php?url="+url, ImmutableMap.of());
        Asserts.notBlank(html, "没要到图😇\n"+url);
        Document document = Jsoup.parse(html);
        Elements resultList = document.select(".result:not(.hidden):not(#result-hidden-notification)");
        Asserts.isFalse(resultList.isEmpty(), "没找到🤕\n"+url);
        Element result = resultList.get(0);
        String rate = result.select(".resultsimilarityinfo").text();
        Elements linkList = result.select(".resultcontentcolumn a.linkify");
        String link = linkList.get(0).attr("href");
        return String.format("找到啦😊！相似度%s\n%s", rate, link);
    }
}
