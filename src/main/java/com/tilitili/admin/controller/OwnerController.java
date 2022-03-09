package com.tilitili.admin.controller;

import com.tilitili.common.entity.Owner;
import com.tilitili.common.entity.query.OwnerQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.rank.OwnerMapper;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.QueryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/owner")
@Validated
@Slf4j
public class OwnerController {

    private final OwnerMapper ownerMapper;

    @Autowired
    public OwnerController(OwnerMapper ownerMapper) {
        this.ownerMapper = ownerMapper;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel<PageModel<Owner>> getOwnerByCondition(OwnerQuery query) {
        Asserts.notNull(query, "参数异常");

        QueryUtil.suppleQuery(query);

        int count = ownerMapper.countOwnerByCondition(query);
        List<Owner> ownerList = ownerMapper.getOwnerByCondition(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), ownerList);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel<?> updateOwner(@RequestBody Owner owner) {
        Asserts.notNull(owner, "参数有误");
        Asserts.notNull(owner.getUid(), "参数有误");
        Asserts.notNull(owner.getStatus(), "参数有误");

        ownerMapper.updateOwnerSelective(owner);
        return new BaseModel<>("更新作者成功", true);
    }

}
