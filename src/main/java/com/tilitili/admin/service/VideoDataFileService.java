package com.tilitili.admin.service;

import com.tilitili.admin.entity.VideoDataAdminFileItem;
import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.admin.utils.MathUtil;
import com.tilitili.common.entity.dto.VideoDTO;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.mapper.rank.VideoDataMapper;
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
    private final VideoDataMapper videoDataMapper;

    @Autowired
    public VideoDataFileService(VideoDataMapper videoDataMapper) {
        this.videoDataMapper = videoDataMapper;
    }

    public List<VideoDataAdminFileItem> listForDataFile(VideoDataQuery videoDataQuery) {
        List<VideoDTO> videoDTOList = videoDataMapper.listForDataFile(videoDataQuery.getIssue());
        List<VideoDataAdminFileItem> result = new ArrayList<>();

        int rankWithoutLen = 0;
        for (long index = 0; index < videoDTOList.size(); index++) {
            long rank = index + 1;
            VideoDTO videoDTO = videoDTOList.get(Math.toIntExact(index));
            long av = Optional.ofNullable(videoDTO.getAv()).orElse(0L);
            String bv = Optional.ofNullable(videoDTO.getBv()).orElse("");
            String name = Optional.ofNullable(videoDTO.getName()).orElse("");
            String img = Optional.ofNullable(videoDTO.getImg()).orElse("");
            String type = Optional.ofNullable(videoDTO.getType()).orElse("");
            String owner = Optional.ofNullable(videoDTO.getOwner()).orElse("");
            String externalOwner = Optional.ofNullable(videoDTO.getExternalOwner()).orElse("");
            boolean copyright = Optional.ofNullable(videoDTO.getCopyright()).orElse(true);
            boolean copyWarning = Optional.ofNullable(videoDTO.getIsCopyWarning()).orElse(true);
            String pubTime = Optional.ofNullable(videoDTO.getPubTime()).orElse("");
            long startTime = Optional.ofNullable(videoDTO.getStartTime()).orElse(0L);
            long duration = Optional.ofNullable(videoDTO.getDuration()).orElse(0L);
            long view = videoDTO.getView();
            long reply = videoDTO.getReply();
            long favorite = videoDTO.getFavorite();
            long coin = videoDTO.getCoin();
            long point = videoDTO.getPoint();
            long hisRank = Optional.ofNullable(videoDTO.getHisRank()).orElse(0L);
            long moreHisRank = Optional.ofNullable(videoDTO.getMoreHisRank()).orElse(0L);
            long oldView = Optional.ofNullable(videoDTO.getOldView()).orElse(0L);
            long oldFavorite = Optional.ofNullable(videoDTO.getOldFavorite()).orElse(0L);
            long oldCoin = Optional.ofNullable(videoDTO.getOldCoin()).orElse(0L);
            long oldReply = Optional.ofNullable(videoDTO.getOldReply()).orElse(0L);
            long page = videoDTO.getPage();

            boolean isUp = hisRank == 0 || rank <= hisRank;
            boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0 && rank <= 30 && hisRank <= 30 && moreHisRank <= 30;
            long theRankWithoutLen = isLen? rankWithoutLen: (++rankWithoutLen);
            int level = getLevelByRankWithoutLen(theRankWithoutLen);
            int showLength = isLen? 5: getShowLengthByLevel(level);

            if (level == 0) {
                continue;
            }

            long dFavorite = favorite - oldFavorite;
            long dCoin = coin - oldCoin;
            long dView = view - oldView;
            long dReply = reply - oldReply;

            // 播放得分
            long viewPoint = dView / 10 / (page + 1);
            long viewPoint1000 = viewPoint * 1000;
            // 修正a
            long a100 = (viewPoint + dFavorite) * 10 * 100 / (viewPoint + dFavorite + dReply * 10);
            long a1000 = a100 * 10;
            // 修正b
            long b1000 = (dFavorite + dCoin) * 3 * 1000 / (viewPoint * 4);
            b1000 = b1000 > 2000? 2000: b1000;

            long checkPoint =  (viewPoint1000 + dReply * a1000 + (dFavorite + dCoin) * 500) * b1000 / 1000000;

            // 计算过程记录
            String a = MathUtil.formatByScale(a100, 2);
            String b = MathUtil.formatByScale(b1000, 3);
            // 新旧计算的得分差距大于旧得分的10%的发出警告
            Boolean isPointWarning = Math.abs(point - checkPoint) > point / 10;

            // 数据为落差数据
            String favoriteStr = "收藏 " + bigNumberFormat(dFavorite);
            String coinStr = "硬币 " + bigNumberFormat(dCoin);
            String viewStr = "播放 " + bigNumberFormat(dView);
            String replyStr = "评论 " + bigNumberFormat(dReply);
            String pointStr = bigNumberFormat(point) + "PT";
            String rankStr = String.valueOf(rank);

            String avStr = "av" + av;
            String nameStr = name;

            String typeStr;
            if (level < 5) {
                typeStr = type;
            } else {
                typeStr = "";
            }

            String ownerStr;
            String subOwnerStr;
            if (level < 5) {
                if (copyright) {
                    if (isBlank(externalOwner)) {
                        ownerStr = owner;
                        subOwnerStr = "";
                    } else {
                        ownerStr = externalOwner;
                        subOwnerStr = "搬运:" + owner;
                    }
                } else {
                    ownerStr = owner;
                    if (copyWarning) {
                        subOwnerStr = "疑似搬运";
                    } else {
                        subOwnerStr = "";
                    }
                }
            } else {
                ownerStr = "";
                subOwnerStr = "";
            }

            // 副榜历史排名默认显示上周-位，主榜不显示
            String hisRankStr;
            if (level < 5) {
                hisRankStr = hisRank != 0? "上周" + hisRank + "位": "";
            } else {
                if (hisRank == 0) {
                    hisRankStr = "上周-位";
                } else {
                    hisRankStr = "上周" + hisRank + "位";
                }

            }

            String pubTimeStr;
            if (level < 5) {
                if (pubTime.contains(" ")) {
                    pubTimeStr = pubTime.split(" ")[0];
                } else {
                    pubTimeStr = "";
                }
            } else {
                if (pubTime.contains(" ")) {
                    pubTimeStr = "投稿日期 " + pubTime.split(" ")[0];
                } else {
                    pubTimeStr = "投稿日期";
                }
            }

            VideoDataAdminFileItem resultItem = new VideoDataAdminFileItem();
            resultItem.setAv(av);
            resultItem.setName(name);
            resultItem.setImg(img);
            resultItem.setType(type);
            resultItem.setOwner(owner);
            resultItem.setExternalOwner(externalOwner);
            resultItem.setCopyright(copyright);
            resultItem.setPubTime(pubTime);
            resultItem.setStartTime(startTime);
            resultItem.setDuration(duration);
            resultItem.setView(view);
            resultItem.setReply(reply);
            resultItem.setFavorite(favorite);
            resultItem.setCoin(coin);
            resultItem.setPoint(point);
            resultItem.setRank(rank);
            resultItem.setHisRank(hisRank);
            resultItem.setIsUp(isUp);
            resultItem.setIsLen(isLen);
            resultItem.setLevel(level);
            resultItem.setShowLength(showLength);
            resultItem.setAvStr(avStr);
            resultItem.setNameStr(nameStr);
            resultItem.setTypeStr(typeStr);
            resultItem.setOwnerStr(ownerStr);
            resultItem.setSubOwnerStr(subOwnerStr);
            resultItem.setPubTimeStr(pubTimeStr);
            resultItem.setViewStr(viewStr);
            resultItem.setReplyStr(replyStr);
            resultItem.setFavoriteStr(favoriteStr);
            resultItem.setCoinStr(coinStr);
            resultItem.setPointStr(pointStr);
            resultItem.setRankStr(rankStr);
            resultItem.setHisRankStr(hisRankStr);
            resultItem.setBv(bv);
            resultItem.setPage(page);
            resultItem.setIsPointWarning(isPointWarning);
            resultItem.setViewPoint(viewPoint);
            resultItem.setA(a);
            resultItem.setB(b);
            resultItem.setCheckPoint(checkPoint);
            result.add(resultItem);
        }
        return result;
    }

    public List<VideoDataFileItem> toDataFile(List<VideoDataAdminFileItem> adminDataFile) {
        return adminDataFile.stream().map(data -> {
            VideoDataFileItem result = new VideoDataFileItem();
            result.setAv(data.getAv());
            result.setName(data.getName());
            result.setImg(data.getImg());
            result.setStartTime(data.getStartTime());
            result.setHisRank(data.getHisRank());
            result.setIsUp(data.getIsUp());
            result.setIsLen(data.getIsLen());
            result.setLevel(data.getLevel());
            result.setShowLength(data.getShowLength());
            result.setAvStr(data.getAvStr());
            result.setTypeStr(data.getTypeStr());
            result.setOwnerStr(data.getOwnerStr());
            result.setSubOwnerStr(data.getSubOwnerStr());
            result.setPubTimeStr(data.getPubTimeStr());
            result.setViewStr(data.getViewStr());
            result.setReplyStr(data.getReplyStr());
            result.setFavoriteStr(data.getFavoriteStr());
            result.setCoinStr(data.getCoinStr());
            result.setPointStr(data.getPointStr());
            result.setRankStr(data.getRankStr());
            result.setHisRankStr(data.getHisRankStr());
            return result;
        }).collect(Collectors.toList());
    }

    public Integer getShowLengthByLevel(Integer level) {
        switch (level) {
            case 1: return 40;
            case 2: return 30;
            case 3: return 20;
            case 4: return 10;
            default: return 0;
        }
    }

    public Integer getLevelByRankWithoutLen(long theRankWithoutLen) {
        if (theRankWithoutLen < 4) {
            return 1;
        } else if (theRankWithoutLen < 11) {
            return 2;
        } else if (theRankWithoutLen < 21) {
            return 3;
        } else if (theRankWithoutLen < 31) {
            return 4;
        } else if (theRankWithoutLen < 101) {
            return 5;
        } else {
            return 0;
        }
    }
}
