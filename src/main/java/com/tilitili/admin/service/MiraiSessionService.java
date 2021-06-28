package com.tilitili.admin.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MiraiSessionService {
    private final Map<String, Map<String, String>> sessionMap;

    public MiraiSessionService() {
        this.sessionMap = new HashMap<>();
    }

    public Map<String, String> getSession(String sessionKey) {
        if (! sessionMap.containsKey(sessionKey)) {
            sessionMap.put(sessionKey, new HashMap<>());
        }
        return sessionMap.get(sessionKey);
    }

    public String getValue(String sessionKey, String key) {
        return getSession(sessionKey).get(key);
    }

    public void setSession(String sessionKey, Map<String, String> session) {
        sessionMap.put(sessionKey, session);
    }

    public void setValue(String sessionKey, String key, String value) {
        getSession(sessionKey).put(key, value);
    }
}
