package com.tilitili.admin.controller;

import com.tilitili.admin.entity.count.TagCount;
import com.tilitili.admin.service.VideoTagService;
import com.tilitili.common.entity.VideoTag;
import com.tilitili.common.entity.query.VideoTagQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.rank.VideoTagRelationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("api/video/tag")
@Validated
@Slf4j
public class VideoTagController extends BaseController {
    private final VideoTagRelationMapper videoTagRelationMapper;
    private final VideoTagService videoTagService;

    @Autowired
    public VideoTagController(VideoTagRelationMapper videoTagRelationMapper, VideoTagService videoTagService) {
        this.videoTagRelationMapper = videoTagRelationMapper;
        this.videoTagService = videoTagService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<PageModel<VideoTag>> getVideoTagByCondition(VideoTagQuery query) {
        int count = videoTagRelationMapper.countVideoTag(query);
        List<VideoTag> videoTagList = videoTagService.listVideoTag(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), videoTagList);
    }

    @GetMapping("/count")
    @ResponseBody
    public BaseModel<TagCount> getTagCount(VideoTagQuery query) {
        TagCount tagCount = new TagCount();
        tagCount.setTopTagList(videoTagService.listTopTagCount(query));
        return BaseModel.success(tagCount);
    }

}
