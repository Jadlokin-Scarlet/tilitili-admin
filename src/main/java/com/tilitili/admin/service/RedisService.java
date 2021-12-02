package com.tilitili.admin.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tilitili.admin.entity.view.RedisView;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisService {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisCache redisCache;

    public RedisService(RedisTemplate<String, Object> redisTemplate, RedisCache redisCache) {
        this.redisTemplate = redisTemplate;
        this.redisCache = redisCache;
    }

    public List<RedisView> getRedisViewByKey(String key) {
        if (!Optional.ofNullable(redisTemplate.hasKey(key)).orElse(false)) {
            return Collections.emptyList();
        }
        DataType keyType = redisTemplate.type(key);
        String type = Optional.ofNullable(keyType).map(DataType::code).orElse("");
        RedisView redisView = new RedisView().setKey(key).setType(type);
        switch (type) {
            case "string": return suppleString(redisView);
            case "hash": return suppleMap(redisView);
//                case "list": redisService.suppleList(redisView);
//                case "set": redisService.suppleSet(redisView);
//                case "none":
//                case "zset":
//                case "stream":
            default: log.warn("key{}类型未知", key);
        }
        return Collections.emptyList();
    }

    public List<RedisView> suppleString(RedisView redisView) {
        Object value = redisTemplate.opsForValue().get(redisView.getKey());
        if (value == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(redisView.setValue(gson.toJson(value)).setClazz(value.getClass().getName()));
    }

    public List<RedisView> suppleMap(RedisView redisView) {
        String key = redisView.getKey();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return map.entrySet().stream().map(entry -> new RedisView().setKey(key).setSubKey((String) entry.getKey()).setValue(gson.toJson(entry.getValue())).setType(redisView.getType()).setClazz(entry.getValue().getClass().getName())).collect(Collectors.toList());
    }

    public void editString(RedisView redisView) throws ClassNotFoundException {
        redisTemplate.opsForValue().set(redisView.getKey(), gson.fromJson(String.valueOf(redisView.getValue()), Class.forName(redisView.getClazz())));
    }

    public void editHash(RedisView redisView) throws ClassNotFoundException {
        redisTemplate.opsForHash().put(redisView.getKey(), redisView.getSubKey(), gson.fromJson(redisView.getValue(), Class.forName(redisView.getClazz())));
    }
}
