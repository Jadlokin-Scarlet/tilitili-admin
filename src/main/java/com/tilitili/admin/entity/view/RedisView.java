package com.tilitili.admin.entity.view;

public class RedisView {
    private String key;
    private String subKey;
    private String value;
    private String type;
    private String clazz;

    public String getKey() {
        return key;
    }

    public RedisView setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public RedisView setValue(String value) {
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

    public String getSubKey() {
        return subKey;
    }

    public RedisView setSubKey(String subKey) {
        this.subKey = subKey;
        return this;
    }

    public String getClazz() {
        return clazz;
    }

    public RedisView setClazz(String clazz) {
        this.clazz = clazz;
        return this;
    }
}
