package com.tilitili.admin.controller;

import com.tilitili.admin.entity.view.RedisQuery;
import com.tilitili.admin.entity.view.RedisView;
import com.tilitili.admin.service.RedisService;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/api/redis")
public class RedisController {
    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisCache redisCache;

    public RedisController(RedisService redisService, RedisTemplate<String, Object> redisTemplate, RedisCache redisCache) {
        this.redisService = redisService;
        this.redisTemplate = redisTemplate;
        this.redisCache = redisCache;
    }

    @RequestMapping("")
    @ResponseBody
    public BaseModel<PageModel<RedisView>> listRedis(RedisQuery query) {
        int start = query.getStart();
        int pageSize = query.getPageSize();
        int current = query.getCurrent();
        String searchKey = query.getKey();

        int total;
        Set<String> keyList;
        if (searchKey == null) {
            Set<String> allKeyList = Optional.ofNullable(redisTemplate.keys("*")).orElse(new HashSet<>());
            total = allKeyList.size();
            keyList = allKeyList.stream().skip(start).limit(pageSize).collect(Collectors.toSet());
        } else if (Optional.ofNullable(redisTemplate.hasKey(searchKey)).orElse(false)) {
            keyList = new HashSet<>(Collections.singletonList(searchKey));
            total = 1;
        } else {
            keyList = new HashSet<>(Collections.emptyList());
            total = 0;
        }
        List<RedisView> redisViewList = keyList.stream().map(key -> {
            DataType keyType = redisTemplate.type(key);
            String type = Optional.ofNullable(keyType).map(DataType::code).orElse("");
            RedisView redisView = new RedisView().setKey(key).setType(type);
            switch (type) {
                case "string": redisService.suppleString(redisView); break;
                case "hash": redisService.suppleMap(redisView); break;
//                case "list": redisService.suppleList(redisView); break;
//                case "set": redisService.suppleSet(redisView); break;
//                case "none": break;
//                case "zset": break;
//                case "stream": break;
                default: log.warn("key{}类型未知", key);
            }
            return redisView;
        }).collect(Collectors.toList());
        return PageModel.of(total, pageSize, current, redisViewList);
    }

    @DeleteMapping("")
    @ResponseBody
    public BaseModel<?> delRedisKey(String key) {
        Asserts.notBlank(key, "参数异常");

        boolean success = Optional.ofNullable(redisTemplate.delete(key)).orElse(false);
        Asserts.isTrue(success, "删除失败");

        return BaseModel.success();
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel<?> editRedisKey(@RequestBody RedisView redisView) {
        Asserts.notNull(redisView, "参数异常");
        String key = redisView.getKey();
        Asserts.notBlank(key, "参数异常");

        DataType keyType = redisTemplate.type(key);
        Asserts.notNull(keyType, "参数异常");
        String type = keyType.code();

        switch (type) {
            case "string": redisService.editString(redisView); break;
            case "hash": redisService.editHash(redisView); break;
//            case "list": redisService.editList(redisView); break;
//            case "set": redisService.editSet(redisView); break;
//            case "none": break;
//            case "zset": break;
//            case "stream": break;
            default: throw new AssertException("未知类型");
        }
        return BaseModel.success();
    }

}
