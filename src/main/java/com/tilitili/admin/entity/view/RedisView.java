package com.tilitili.admin.entity.view;

import java.util.List;
import java.util.Map;

public class RedisView<T> {
    private String key;
    private String subKey;
    private T value;
    private String type;
    private Map<T, T> map;
    private List<T> list;

    public String getKey() {
        return key;
    }

    public RedisView<T> setKey(String key) {
        this.key = key;
        return this;
    }

    public T getValue() {
        return value;
    }

    public RedisView<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public RedisView<T> setType(String type) {
        this.type = type;
        return this;
    }

    public Map<T, T> getMap() {
        return map;
    }

    public RedisView<T> setMap(Map<T, T> map) {
        this.map = map;
        return this;
    }

    public List<T> getList() {
        return list;
    }

    public RedisView<T> setList(List<T> list) {
        this.list = list;
        return this;
    }

    public String getSubKey() {
        return subKey;
    }

    public RedisView<T> setSubKey(String subKey) {
        this.subKey = subKey;
        return this;
    }
}
