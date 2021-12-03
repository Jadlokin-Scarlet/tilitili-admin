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
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.http.util.TextUtils.isBlank;

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

    @RequestMapping("/list")
    @ResponseBody
    public BaseModel<PageModel<RedisView>> listRedis(RedisQuery query) {
        int start = query.getStart();
        int pageSize = query.getPageSize();
        int current = query.getCurrent();
        String searchKey = query.getKey();
        String searchSubKey = query.getSubKey();

        Set<String> keyList = Optional.ofNullable(redisTemplate.keys("*")).orElse(new HashSet<>());
        List<RedisView> redisViewList = keyList.stream()
                .filter(key -> isBlank(searchKey) || Objects.equals(searchKey, key))
                .map(redisService::getRedisViewByKey)
                .flatMap(Collection::stream)
                .filter(redisView -> isBlank(searchSubKey) || Objects.equals(searchSubKey, redisView.getSubKey()))
                .collect(Collectors.toList());
        List<RedisView> pageRedisViewList = IntStream.range(0, redisViewList.size()).mapToObj(index -> redisViewList.get(index).setIndex(index)).skip(start).limit(pageSize).collect(Collectors.toList());
        return PageModel.of(redisViewList.size(), pageSize, current, pageRedisViewList);
    }

    @PostMapping("/del")
    @ResponseBody
    public BaseModel<?> delRedisKey(@RequestBody RedisView redisView) {
        Asserts.notNull(redisView, "参数异常");
        Asserts.notBlank(redisView.getKey(), "参数异常");

        String key = redisView.getKey();
        Object subKey = redisView.getSubKey();

        if (subKey == null) {
            boolean success = Optional.ofNullable(redisTemplate.delete(key)).orElse(false);
            Asserts.isTrue(success, "删除失败");
        } else {
            boolean success = redisTemplate.opsForHash().delete(key, subKey) == 1;
            Asserts.isTrue(success, "删除失败");
        }

        return BaseModel.success();
    }

    @PostMapping("/edit")
    @ResponseBody
    public BaseModel<?> editRedisKey(@RequestBody RedisView redisView) throws ClassNotFoundException {
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
