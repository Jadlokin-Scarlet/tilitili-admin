package com.tilitili.admin.controller;

import com.tilitili.common.entity.Tag;
import com.tilitili.common.entity.query.TagQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.rank.TagMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequestMapping("api/tag")
public class TagController {
    private final TagMapper tagMapper;

    @Autowired
    public TagController(TagMapper tagMapper) {
        this.tagMapper = tagMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<PageModel<Tag>> getTagByCondition(TagQuery query) {
        int count = tagMapper.countTagByCondition(query);
        List<Tag> tagList = tagMapper.getTagByCondition(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), tagList);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel<?> updateTag(@RequestBody Tag tag) {
        Asserts.notNull(tag, "参数有误");
        Asserts.notNull(tag.getId(), "参数有误");
        Asserts.notNull(tag.getType(), "参数有误");

        tagMapper.updateTagSelective(tag);
        return new BaseModel<>("更新tag成功", true);
    }
}
