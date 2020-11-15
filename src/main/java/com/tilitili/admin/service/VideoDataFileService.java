package com.tilitili.admin.service;

import com.tilitili.admin.entity.VideoDataFileItem;
import com.tilitili.common.entity.VideoData;
import com.tilitili.common.entity.query.VideoDataQuery;
import com.tilitili.common.manager.VideoManager;
import com.tilitili.common.mapper.VideoDataMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoDataFileService {

    private final VideoDataMapper videoDataMapper;

    private final List<String> fields = Arrays.asList(
            "av", "name", "img", "type", "owner",
            "copyright", "pubTime", "startTime", "view", "reply",
            "favorite", "coin", "point", "rank", "hisRank", "isLen"
    );

    @Autowired
    public VideoDataFileService(VideoDataMapper videoDataMapper) {
        this.videoDataMapper = videoDataMapper;
    }

    public String getVideoDataFile(int issue) {
        String head = String.join("\t", fields) + "\n";
        String body = listForDataFile(issue).stream()
                .map(video -> video.toString(fields))
                .collect(Collectors.joining("\n"));
        return head + body;
    }

    public List<VideoDataFileItem> listForDataFile(Integer issue) {
        VideoDataQuery videoDataQuery = new VideoDataQuery().setIssue(issue).setIsDelete(false).setStatus(0);
        videoDataQuery.setPageSize(100).setSorter("point", "desc");
        return videoDataMapper.list(videoDataQuery).stream().parallel().map(videoData -> {
            VideoDataFileItem video = new VideoDataFileItem();
            BeanUtils.copyProperties(videoData, video);

            VideoData his = videoDataMapper.getByAvAndIssue(videoData.getAv(), videoData.getIssue() - 1);
            if (his != null) {
                video.setHisRank(his.getRank());
            }else {
                video.setHisRank(0);
            }

            VideoData moreHis = videoDataMapper.getByAvAndIssue(videoData.getAv(), videoData.getIssue() - 2);
            if (his != null && moreHis != null) {
                video.setIsLen(videoData.getRank(), his.getRank(), moreHis.getRank());
            }else {
                video.setIsLen(0);
            }

            return video;
        }).collect(Collectors.toList());
    }

}
