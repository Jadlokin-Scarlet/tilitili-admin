package com.tilitili.admin.service;

import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.entity.VideoDataFileItemV2;
import com.tilitili.admin.utils.MathUtil;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.VideoDataManager;
import com.tilitili.common.mapper.VideoDataMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.tilitili.admin.utils.StringUtil.bigNumberFormat;

@Service
public class VideoDataFileService {

    private final VideoDataMapper videoDataMapper;
    private final VideoDataManager videoDataManager;

    private final List<String> fields = Arrays.asList(
            "av", "name", "img", "type", "owner",
            "copyright", "pubTime", "startTime", "view", "reply",
            "favorite", "coin", "point", "rank", "hisRank", "isLen"
    );

    @Autowired
    public VideoDataFileService(VideoDataMapper videoDataMapper, VideoDataManager videoDataManager) {
        this.videoDataMapper = videoDataMapper;
        this.videoDataManager = videoDataManager;
    }

    public String getVideoDataFile(int issue) {
        String head = String.join("\t", fields) + "\n";
        String body = listForDataFile(new VideoDataQuery().setIssue(issue)).stream()
                .map(video -> video.toDataFileLine(fields))
                .collect(Collectors.joining("\n"));
        return head + body;
    }

    public List<VideoDataFileItemV2> listForDataFileV2(VideoDataQuery videoDataQuery) {
        videoDataQuery.setIsDelete(false).setStatus(0);
        videoDataQuery.setPageSize(100).setSorter("point", "desc");
        return videoDataMapper.list(videoDataQuery).stream().parallel().map(videoData -> {
            VideoDataFileItemV2 video = new VideoDataFileItemV2();
            BeanUtils.copyProperties(videoData, video);

            VideoData oldVideo = videoDataManager.getOrDefault(videoData.getAv(), videoData.getIssue() - 1);
            VideoData moreOldVideo = videoDataManager.getOrDefault(videoData.getAv(), videoData.getIssue() - 2);

            // 检查分数
            long favorite = videoData.getFavorite() - oldVideo.getFavorite();
            long coin = videoData.getCoin() - oldVideo.getCoin();
            long view = videoData.getView() - oldVideo.getView();
            long reply = videoData.getReply() - oldVideo.getReply();
            long page = videoData.getPage();

            // 播放得分
            long viewPoint = view / 10 / (page + 1);
            long viewPoint1000 = viewPoint * 1000;
            // 修正a
            long a100 = (viewPoint + favorite) * 10 * 100 / (viewPoint + favorite + reply * 10);
            long a1000 = a100 * 10;
            // 修正b
            long b1000 = (favorite + coin) * 3 * 1000 / (viewPoint * 4);
            b1000 = b1000 > 2000? 2000: b1000;

            long point =  (viewPoint1000 + reply * a1000 + (favorite + coin) * 500) * b1000 / 1000000;

            // 设置数据为落差
            video.setFavorite("收藏 " + bigNumberFormat(favorite));
            video.setCoin("硬币 " + bigNumberFormat(coin));
            video.setView("播放 " + bigNumberFormat(view));
            video.setReply("评论 " + bigNumberFormat(reply));
            video.setPage(Long.valueOf(page).intValue());

            // 计算过程记录
            video.setA(MathUtil.formatByScale(a100, 2));
            video.setB(MathUtil.formatByScale(b1000, 3));
            video.setViewPoint(viewPoint);
            video.setCheckPoint(point);
            // 新旧计算的得分差距大于旧得分的10%的发出警告
            video.setIsPointWarning(Math.abs(point - videoData.getPoint()) > videoData.getPoint() / 10);

            // 历史排名和是否长期
            video.setHisRank("上周" + oldVideo.getRank() + "位");
            video.setIsLen(videoData.getRank(), oldVideo.getRank(), moreOldVideo.getRank());

            // 视频里时间格式为yyyy-mm-dd
            if (video.getPubTime() != null && video.getPubTime().contains(" ")) {
                video.setPubTime("投稿日期 " + video.getPubTime().split(" ")[0]);
            }

            // 是否搬运
            video.setCopyright(videoData.getCopyright()? "true": "false");
            video.setAvStr("av " + videoData.getAv());

            return video;
        }).collect(Collectors.toList());
    }

    public List<VideoDataFileItem> listForDataFile(VideoDataQuery videoDataQuery) {
        videoDataQuery.setIsDelete(false).setStatus(0);
        videoDataQuery.setPageSize(100).setSorter("point", "desc");
        return videoDataMapper.list(videoDataQuery).stream().parallel().map(videoData -> {
            VideoDataFileItem video = new VideoDataFileItem();
            BeanUtils.copyProperties(videoData, video);

            VideoData oldVideo = videoDataManager.getOrDefault(videoData.getAv(), videoData.getIssue() - 1);
            VideoData moreOldVideo = videoDataManager.getOrDefault(videoData.getAv(), videoData.getIssue() - 2);

            // 检查分数
            long favorite = videoData.getFavorite() - oldVideo.getFavorite();
            long coin = videoData.getCoin() - oldVideo.getCoin();
            long view = videoData.getView() - oldVideo.getView();
            long reply = videoData.getReply() - oldVideo.getReply();
            long page = videoData.getPage();

            // 播放得分
            long viewPoint = view / 10 / (page + 1);
            long viewPoint1000 = viewPoint * 1000;
            // 修正a
            long a100 = (viewPoint + favorite) * 10 * 100 / (viewPoint + favorite + reply * 10);
            long a1000 = a100 * 10;
            // 修正b
            long b1000 = (favorite + coin) * 3 * 1000 / (viewPoint * 4);
            b1000 = b1000 > 2000? 2000: b1000;

            long point =  (viewPoint1000 + reply * a1000 + (favorite + coin) * 500) * b1000 / 1000000;

            // 设置数据为落差
            video.setFavorite(favorite);
            video.setCoin(coin);
            video.setView(view);
            video.setReply(reply);
            video.setPage(page);

            // 计算过程记录
            video.setA(MathUtil.formatByScale(a100, 2));
            video.setB(MathUtil.formatByScale(b1000, 3));
            video.setViewPoint(viewPoint);
            video.setCheckPoint(point);
            // 新旧计算的得分差距大于旧得分的10%的发出警告
            video.setIsPointWarning(Math.abs(point - videoData.getPoint()) > videoData.getPoint() / 10);

            // 历史排名和是否长期
            video.setHisRank(oldVideo.getRank());
            video.setIsLen(videoData.getRank(), oldVideo.getRank(), moreOldVideo.getRank());

            // 视频里时间格式为yyyy-mm-dd
            if (video.getPubTime() != null && video.getPubTime().contains(" ")) {
                video.setPubTime(video.getPubTime().split(" ")[0]);
            }

            // 如果填了原作者就用原作者代替作者
            if (Strings.isNotBlank(videoData.getExternalOwner())) {
                video.setOwner(videoData.getExternalOwner());
            }

            // 是否搬运
            video.setCopyright(videoData.getCopyright()? 1: 0);

            return video;
        }).collect(Collectors.toList());
    }
}
