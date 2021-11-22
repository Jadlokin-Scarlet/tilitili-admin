package com.tilitili.admin.entity.view;

import com.tilitili.common.entity.query.BaseQuery;

public class RedisQuery extends BaseQuery<RedisQuery> {
    private String key;

    public String getKey() {
        return key;
    }

    public RedisQuery setKey(String key) {
        this.key = key;
        return this;
    }
}
