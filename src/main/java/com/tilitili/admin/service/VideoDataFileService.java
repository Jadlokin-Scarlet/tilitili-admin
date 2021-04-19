package com.tilitili.admin.service;

import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.utils.MathUtil;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.VideoDataManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tilitili.admin.utils.StringUtil.bigNumberFormat;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class VideoDataFileService {

    private final VideoDataManager videoDataManager;

    @Autowired
    public VideoDataFileService(VideoDataManager videoDataManager) {
        this.videoDataManager = videoDataManager;
    }

    public List<VideoDataFileItem> listForDataFile(VideoDataQuery videoDataQuery) {
        videoDataQuery.setHasRank(true);
        videoDataQuery.setPageSize(200);
        videoDataQuery.setSorter("point", "desc");
        List<VideoData> videoDataList = videoDataManager.list(videoDataQuery);
        List<VideoDataFileItem> result = new ArrayList<>();

        int rankWithoutLen = 1;

        for (VideoData videoData : videoDataList) {
            VideoDataFileItem video = new VideoDataFileItem();

            if (rankWithoutLen < 4) {
                video.setLevel(1);
                video.setShowLength(40);
            } else if (rankWithoutLen < 11) {
                video.setLevel(2);
                video.setShowLength(30);
            } else if (rankWithoutLen < 21) {
                video.setLevel(3);
                video.setShowLength(20);
            } else if (rankWithoutLen < 31) {
                video.setLevel(4);
                video.setShowLength(10);
            } else if (rankWithoutLen < 101) {
                video.setLevel(5);
            } else {
                break;
            }

            VideoData oldVideo = videoDataManager.getOrDefault(videoData.getAv(), videoData.getIssue() - 1);
            VideoData moreOldVideo = videoDataManager.getOrDefault(videoData.getAv(), videoData.getIssue() - 2);

            BeanUtils.copyProperties(videoData, video);
            video.setHisRank(oldVideo.getRank());
            video.setIsLen(videoData.getRank(), oldVideo.getRank(), moreOldVideo.getRank());

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

            // 计算过程记录
            video.setPage(Long.valueOf(page).intValue());
            video.setA(MathUtil.formatByScale(a100, 2));
            video.setB(MathUtil.formatByScale(b1000, 3));
            video.setViewPoint(viewPoint);
            video.setCheckPoint(point);
            // 新旧计算的得分差距大于旧得分的10%的发出警告
            video.setIsPointWarning(Math.abs(point - videoData.getPoint()) > videoData.getPoint() / 10);

            // 将数据填入占位槽
            video.setAvStr("av" + videoData.getAv());
            video.setNameStr(videoData.getName());
            // 视频里时间格式为yyyy-mm-dd
            if (videoData.getPubTime() != null && videoData.getPubTime().contains(" ")) {
                video.setPubTimeStr(videoData.getPubTime().split(" ")[0]);
            }
            // 数据为落差数据
            video.setFavoriteStr("收藏 " + bigNumberFormat(favorite));
            video.setCoinStr("硬币 " + bigNumberFormat(coin));
            video.setViewStr("播放 " + bigNumberFormat(view));
            video.setReplyStr("评论 " + bigNumberFormat(reply));
            video.setPointStr(bigNumberFormat(videoData.getPoint()) + "PT");
            video.setRankStr(videoData.getRank().toString());
            // 历史排名
            if (oldVideo.getRank() != 0) {
                video.setHisRankStr("上周" + oldVideo.getRank() + "位");
            }

            // 主副榜不同的设置
            if (video.getLevel() < 5) {
                video.setTypeStr(videoData.getType());
                // 是否搬运
                if (videoData.getCopyright()) {
                    if (isBlank(videoData.getExternalOwner())) {
                        video.setOwnerStr(videoData.getOwner());
                    }else {
                        video.setOwnerStr(videoData.getExternalOwner());
                        video.setSubOwnerStr("搬运:"+videoData.getOwner());
                    }
                }else {
                    video.setOwnerStr(videoData.getOwner());
                    if (videoData.getIsCopyWarning()) {
                        video.setSubOwnerStr("疑似搬运");
                    }
                }
                // 是否上升排名
                video.setIsUp(oldVideo.getRank() == 0 || videoData.getRank() <= oldVideo.getRank());
                // 长期视频只5s
                if (video.getIsLen()) {
                    video.setShowLength(5);
                }
            }else {
                // 副榜历史排名默认显示上周-位，主榜不显示
                if (oldVideo.getRank() == 0) {
                    video.setHisRankStr("上周-位");
                }
                video.setPubTimeStr("投稿日期 " + video.getPubTimeStr());
            }

            result.add(video);

            if (! video.getIsLen()) {
                rankWithoutLen++;
            }
        }
        return result;
    }

}
