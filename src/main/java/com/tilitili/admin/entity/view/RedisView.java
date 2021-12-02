package com.tilitili.admin.entity.view;

public class RedisView {
    private String key;
    private Object subKey;
    private Object value;
    private String type;

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

    public Object getSubKey() {
        return subKey;
    }

    public RedisView setSubKey(Object subKey) {
        this.subKey = subKey;
        return this;
    }
}
