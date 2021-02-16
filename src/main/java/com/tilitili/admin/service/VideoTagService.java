package com.tilitili.admin.service;

import com.tilitili.admin.entity.count.sub.TopTagCount;
import com.tilitili.common.entity.VideoTag;
import com.tilitili.common.entity.dto.TagRelationGroup;
import com.tilitili.common.entity.query.BaseQuery;
import com.tilitili.common.entity.query.VideoTagQuery;
import com.tilitili.common.mapper.TagMapper;
import com.tilitili.common.mapper.VideoTagRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoTagService {

    private final VideoTagRelationMapper videoTagRelationMapper;
    private final TagMapper tagMapper;

    @Autowired
    public VideoTagService(VideoTagRelationMapper videoTagRelationMapper, TagMapper tagMapper) {
        this.videoTagRelationMapper = videoTagRelationMapper;
        this.tagMapper = tagMapper;
    }

    public List<VideoTag> listVideoTag(VideoTagQuery videoTagQuery) {
        List<VideoTag> videoTagList = videoTagRelationMapper.listVideoTag(videoTagQuery);
        return videoTagList.parallelStream().peek(videoTag -> {
            videoTag.setTagList(tagMapper.listByIdListStr(videoTag.getTagIdListStr()));
        }).collect(Collectors.toList());
    }

    public List<TopTagCount> listTopTagCount(VideoTagQuery query) {
        query.setTagType(0);
        List<TagRelationGroup> groupList = videoTagRelationMapper.groupByTag(query);
        return groupList.parallelStream().map(group ->
                new TopTagCount().setTag(group.getTag()).setNumber(group.getNumber())
        ).collect(Collectors.toList());
    }

}
