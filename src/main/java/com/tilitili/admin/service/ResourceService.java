package com.tilitili.admin.service;

import com.tilitili.common.emnus.*;
import com.tilitili.common.entity.Resource;
import com.tilitili.common.entity.view.DispatchResourcesView;
import com.tilitili.common.mapper.ResourcesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Supplier;

@Service
public class ResourceService {
    private ResourcesMapper resourcesMapper;

    private final Map<String, Supplier<List<Resource>>> resourceMap = new HashMap<>();

    @Autowired
    public ResourceService(TypeService typeService, VideoDataService videoDataService, ResourcesMapper resourcesMapper) {
        this.resourcesMapper = resourcesMapper;
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

    public String getFlagFileResources() {
        return getFlagResources().toString();
    }

    public DispatchResourcesView getFlagResources() {
        DispatchResourcesView view = new DispatchResourcesView();
        view.setIsStaffShow1(resourcesMapper.getValueByType(ResourcesType.IS_STAFF_SHOW_1.value));
        view.setIsStaffShow2(resourcesMapper.getValueByType(ResourcesType.IS_STAFF_SHOW_2.value));
        view.setMusicName(resourcesMapper.getValueByType(ResourcesType.MUSIC_NAME.value));
        view.setMusicOwner(resourcesMapper.getValueByType(ResourcesType.MUSIC_OWNER.value));
        view.setMusicCard(resourcesMapper.getValueByType(ResourcesType.MUSIC_CARD.value));
        view.setMusicImage(resourcesMapper.getValueByType(ResourcesType.MUSIC_IMAGE.value));
        view.setMusicSource(resourcesMapper.getValueByType(ResourcesType.MUSIC_SOURCE.value));
        return view;
    }

    @Transactional
    public void updateFlagResources(DispatchResourcesView resourcesView) {
        resourcesMapper.updateValueByType(ResourcesType.IS_STAFF_SHOW_1.value, resourcesView.getIsStaffShow1());
        resourcesMapper.updateValueByType(ResourcesType.IS_STAFF_SHOW_2.value, resourcesView.getIsStaffShow2());
        resourcesMapper.updateValueByType(ResourcesType.MUSIC_NAME.value, resourcesView.getMusicName());
        resourcesMapper.updateValueByType(ResourcesType.MUSIC_OWNER.value, resourcesView.getMusicOwner());
        resourcesMapper.updateValueByType(ResourcesType.MUSIC_CARD.value, resourcesView.getMusicCard());
        resourcesMapper.updateValueByType(ResourcesType.MUSIC_IMAGE.value, resourcesView.getMusicImage());
        resourcesMapper.updateValueByType(ResourcesType.MUSIC_SOURCE.value, resourcesView.getMusicSource());
    }

}
