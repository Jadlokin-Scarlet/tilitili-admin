package com.tilitili.admin.controller;

import com.tilitili.admin.entity.count.TagCount;
import com.tilitili.common.entity.Tag;
import com.tilitili.common.entity.query.TagQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.TagMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
    public BaseModel getTagByCondition(TagQuery query) {
        int count = tagMapper.count(query);
        List<Tag> tagList = tagMapper.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), tagList);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel updateTag(@RequestBody Tag tag) {
        Asserts.notNull(tag, "参数有误");
        Asserts.notNull(tag.getId(), "参数有误");
        Asserts.notNull(tag.getType(), "参数有误");

        tagMapper.update(tag);
        return new BaseModel("更新tag成功", true);
    }
}
