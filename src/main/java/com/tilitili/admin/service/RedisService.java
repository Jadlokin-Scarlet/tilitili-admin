package com.tilitili.admin.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tilitili.admin.controller.RedisController;
import com.tilitili.admin.entity.view.RedisView;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public void suppleList(RedisView redisView) {
        String key = redisView.getKey();
        Long length = Optional.ofNullable(redisTemplate.opsForList().size(key)).orElse(0L);
        List<Object> list = redisTemplate.opsForList().range(key, 0, length);
        redisView.setList(list);
    }

    public void suppleSet(RedisView redisView) {
        String key = redisView.getKey();
        List<Object> list = Optional.ofNullable(redisTemplate.opsForSet().members(key)).map(ArrayList::new).orElse(new ArrayList<>());
        redisView.setList(list);
    }

    public void suppleMap(RedisView redisView) {
        String key = redisView.getKey();
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        redisView.setMap(map);
    }

    public void editString(RedisView redisView) {
        redisTemplate.opsForValue().set(redisView.getKey(), redisView.getValue());
    }

    public void editList(RedisView redisView) {
        String key = redisView.getKey();
        int size = redisView.getList().size();
        Long oldSize = redisTemplate.opsForList().size(key);
        Asserts.notNull(oldSize, "参数异常");
        for (int index = 0, bound = Math.toIntExact(Math.max(size, oldSize)); index < bound; index++) {
            Object item = redisView.getList().get(index);
            redisTemplate.opsForList().set(key, index, item);
        }
    }

    public void editSet(RedisView redisView) {
        String key = redisView.getKey();
        List<Object> newValueList = new Gson().fromJson((String) redisView.getValue(), new TypeToken<List<Object>>() {}.getType());
        Set<Object> oldValueSet = redisTemplate.opsForSet().members(key);



        redisTemplate.opsForSet().remove(key);
    }

    public void editHash(RedisView redisView) {

    }
}
