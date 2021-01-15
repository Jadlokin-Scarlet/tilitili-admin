package com.tilitili.admin.service;

import com.tilitili.common.emnus.TagType;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.emnus.TaskStatus;
import com.tilitili.common.emnus.TaskType;
import com.tilitili.common.entity.Resource;
import com.tilitili.common.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ResourceService {
    private final Map<String, Supplier<List<Resource>>> resourceMap = new HashMap<>();

    @Autowired
    public ResourceService(TypeService typeService, VideoDataService videoDataService) {
        resourceMap.put("videoTypeResource", typeService::getTypeResource);
        resourceMap.put("videoIssueResource", videoDataService::getIssueResource);
        resourceMap.put("TaskTypeResource", TaskType::getResource);
        resourceMap.put("TaskStatusResource", TaskStatus::getResource);
        resourceMap.put("TaskReasonResource", TaskReason::getResource);
        resourceMap.put("TagTypeResource", TagType::getResource);
    }

    public List<Resource> getResource(String resourceName) {
        return resourceMap.getOrDefault(resourceName, Collections::emptyList).get();
    }

}
