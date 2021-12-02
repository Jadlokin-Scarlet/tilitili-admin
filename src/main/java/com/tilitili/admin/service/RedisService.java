package com.tilitili.admin.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tilitili.admin.entity.view.RedisView;
import com.tilitili.common.utils.RedisCache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RedisService {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisCache redisCache;

    public RedisService(RedisTemplate<String, Object> redisTemplate, RedisCache redisCache) {
        this.redisTemplate = redisTemplate;
        this.redisCache = redisCache;
    }

    public void suppleString(RedisView redisView) {
        redisView.setValue(redisTemplate.opsForValue().get(redisView.getKey()));
    }

    public List<RedisView> suppleMap(RedisView redisView) {
        String key = redisView.getKey();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return map.entrySet().stream().map(entry -> new RedisView().setKey(key).setSubKey(entry.getKey()).setValue(entry.getValue()).setType(redisView.getType())).collect(Collectors.toList());
    }

    public void editString(RedisView redisView) {
        redisTemplate.opsForValue().set(redisView.getKey(), redisView.getValue());
    }

    public void editHash(RedisView redisView) {

    }

    @Cacheable(value = "test2")
    public RedisView test(Long value) {
        return new RedisView().setKey("test2").setValue(value);
    }
}
