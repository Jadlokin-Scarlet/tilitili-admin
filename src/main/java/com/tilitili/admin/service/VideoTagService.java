package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.TopTagCount;
import com.tilitili.common.entity.VideoTag;
import com.tilitili.common.entity.dto.TagRelationGroup;
import com.tilitili.common.entity.query.VideoTagQuery;
import com.tilitili.common.mapper.tilitili.TagMapper;
import com.tilitili.common.mapper.tilitili.VideoDataMapper;
import com.tilitili.common.mapper.tilitili.VideoTagRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoTagService {

    private final VideoTagRelationMapper videoTagRelationMapper;
    private final TagMapper tagMapper;
    private final VideoDataMapper videoDataMapper;

    @Autowired
    public VideoTagService(VideoTagRelationMapper videoTagRelationMapper, TagMapper tagMapper, VideoDataMapper videoDataMapper) {
        this.videoTagRelationMapper = videoTagRelationMapper;
        this.tagMapper = tagMapper;
        this.videoDataMapper = videoDataMapper;
    }

    public List<VideoTag> listVideoTag(VideoTagQuery videoTagQuery) {
        List<VideoTag> videoTagList = videoTagRelationMapper.listVideoTag(videoTagQuery);
        return videoTagList.parallelStream().peek(videoTag ->
                videoTag.setTagList(tagMapper.listByIdListStr(videoTag.getTagIdListStr()))
        ).collect(Collectors.toList());
    }

    public List<TopTagCount> listTopTagCount(VideoTagQuery query) {
        query.setTagType(0);
        query.setIssue(videoDataMapper.getNewIssue());
        List<TagRelationGroup> groupList = videoTagRelationMapper.groupByTagAndData(query);
        return groupList.parallelStream().map(group ->
                new TopTagCount().setTag(group.getTag()).setNumber(group.getNumber())
        ).collect(Collectors.toList());
    }

}
