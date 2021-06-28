package com.tilitili.admin.service;

import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.utils.MathUtil;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.manager.VideoDataManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public BaseModel listForDataFile(VideoDataQuery videoDataQuery) {
        Integer pageSize = videoDataQuery.getPageSize();
        Integer current = videoDataQuery.getCurrent();
        Integer start = videoDataQuery.getStart();
        int total = videoDataManager.count(videoDataQuery.setHasLevel(true));
        List<VideoData> videoDataList = videoDataManager.listDataFile(videoDataQuery.getIssue());
        List<VideoDataFileItem> result = videoDataList.parallelStream().map(videoData -> {
            VideoDataFileItem video = new VideoDataFileItem();
            Integer rank = videoData.getRank();

            long view = Optional.ofNullable(videoData.getView()).orElse(0);
            long favorite = Optional.ofNullable(videoData.getFavorite()).orElse(0);
            long coin = Optional.ofNullable(videoData.getCoin()).orElse(0);
            long reply = Optional.ofNullable(videoData.getReply()).orElse(0);

            switch (videoData.getLevel()) {
                case 1: video.setShowLength(40);break;
                case 2: video.setShowLength(30);break;
                case 3: video.setShowLength(20);break;
                case 4: video.setShowLength(10);break;
            }

            VideoData oldVideo = videoDataManager.getByAvAndIssue(videoData.getAv(), videoData.getIssue() - 1);
            VideoData moreOldVideo = videoDataManager.getByAvAndIssue(videoData.getAv(), videoData.getIssue() - 2);

            long oldView = Optional.ofNullable(oldVideo).map(VideoData::getView).orElse(0);
            long oldFavorite = Optional.ofNullable(oldVideo).map(VideoData::getFavorite).orElse(0);
            long oldCoin = Optional.ofNullable(oldVideo).map(VideoData::getCoin).orElse(0);
            long oldReply = Optional.ofNullable(oldVideo).map(VideoData::getReply).orElse(0);

            Integer oldRank = Optional.ofNullable(oldVideo).map(VideoData::getRank).orElse(0);
            Integer moreOldRank = Optional.ofNullable(moreOldVideo).map(VideoData::getRank).orElse(0);

            BeanUtils.copyProperties(videoData, video);
            video.setHisRank(oldRank);
            video.setIsLen(rank, oldRank, moreOldRank);

            long dFavorite = favorite - oldFavorite;
            long dCoin = coin - oldCoin;
            long dView = view - oldView;
            long dReply = reply - oldReply;
            long page = video.getPage();


            // 播放得分
            long viewPoint = dView / 10 / (page + 1);
            long viewPoint1000 = viewPoint * 1000;
            // 修正a
            long a100 = (viewPoint + dFavorite) * 10 * 100 / (viewPoint + dFavorite + dReply * 10);
            long a1000 = a100 * 10;
            // 修正b
            long b1000 = (dFavorite + dCoin) * 3 * 1000 / (viewPoint * 4);
            b1000 = b1000 > 2000? 2000: b1000;

            long point =  (viewPoint1000 + dReply * a1000 + (dFavorite + dCoin) * 500) * b1000 / 1000000;

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
            video.setFavoriteStr("收藏 " + bigNumberFormat(dFavorite));
            video.setCoinStr("硬币 " + bigNumberFormat(dCoin));
            video.setViewStr("播放 " + bigNumberFormat(dView));
            video.setReplyStr("评论 " + bigNumberFormat(dReply));
            video.setPointStr(bigNumberFormat(videoData.getPoint()) + "PT");
            video.setRankStr(videoData.getRank().toString());
            // 历史排名
            if (oldRank != 0) {
                video.setHisRankStr("上周" + oldRank + "位");
            }

            // 主副榜不同的设置
            if (video.getLevel() < 5) {
                video.setTypeStr(videoData.getType());
                // 是否搬运
                if (videoData.getCopyright()) {
                    if (isBlank(videoData.getExternalOwner())) {
                        video.setOwnerStr(videoData.getOwner());
                    } else {
                        video.setOwnerStr(videoData.getExternalOwner());
                        video.setSubOwnerStr("搬运:" + videoData.getOwner());
                    }
                } else {
                    video.setOwnerStr(videoData.getOwner());
                    if (videoData.getIsCopyWarning()) {
                        video.setSubOwnerStr("疑似搬运");
                    }
                }
                // 是否上升排名
                video.setIsUp(oldRank == 0 || videoData.getRank() <= oldRank);
                // 长期视频只5s
                if (video.getIsLen()) {
                    video.setShowLength(5);
                }
            } else {
                // 副榜历史排名默认显示上周-位，主榜不显示
                if (oldRank == 0) {
                    video.setHisRankStr("上周-位");
                }
                video.setPubTimeStr("投稿日期 " + video.getPubTimeStr());
            }

            return video;
        }).skip(start).limit(pageSize).collect(Collectors.toList());
        return PageModel.of(total, pageSize, current, result);
    }

}
