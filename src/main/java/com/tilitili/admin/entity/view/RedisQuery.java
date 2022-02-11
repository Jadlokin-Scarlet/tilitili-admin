package com.tilitili.admin.entity.view;

import com.tilitili.common.entity.query.base.BaseTableQuery;

public class RedisQuery extends BaseTableQuery<RedisQuery> {
    private String key;
    private String subKey;

    public String getKey() {
        return key;
    }

    public RedisQuery setKey(String key) {
        this.key = key;
        return this;
    }

    public String getSubKey() {
        return subKey;
    }

    public RedisQuery setSubKey(String subKey) {
        this.subKey = subKey;
        return this;
    }
}
