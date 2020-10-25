package com.tilitili.admin.service;

import com.tilitili.common.entity.Resource;
import com.tilitili.common.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Service
public class ResourceService {
    private final Map<String, Supplier<List<Resource<?>>>> resourceMap = new HashMap<>();

    @Autowired
    public ResourceService(TypeService typeService, VideoDataService videoDataService) {
        resourceMap.put("videoTypeResource", typeService::getTypeResource);
        resourceMap.put("videoIssueResource", videoDataService::getIssueResource);
    }

    public List<Resource<?>> getResource(String resourceName) {
        return resourceMap.getOrDefault(resourceName, Collections::emptyList).get();
    }
}
