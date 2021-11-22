package com.tilitili.admin.entity.view;

import java.util.List;
import java.util.Map;

public class RedisView {
    private String key;
    private Object value;
    private String type;
    private Map<Object, Object> map;
    private List<Object> list;

    public String getKey() {
        return key;
    }

    public RedisView setKey(String key) {
        this.key = key;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public RedisView setValue(Object value) {
        this.value = value;
        return this;
    }

    public String getType() {
        return type;
    }

    public RedisView setType(String type) {
        this.type = type;
        return this;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public RedisView setMap(Map<Object, Object> map) {
        this.map = map;
        return this;
    }

    public List<Object> getList() {
        return list;
    }

    public RedisView setList(List<Object> list) {
        this.list = list;
        return this;
    }
}
