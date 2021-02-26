package com.tilitili.admin.service;

import com.tilitili.common.emnus.*;
import com.tilitili.common.entity.Resource;
import com.tilitili.common.entity.view.DispatchResourcesView;
import com.tilitili.common.mapper.RecommendMapper;
import com.tilitili.common.mapper.ResourcesMapper;
import com.tilitili.common.manager.VideoDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;

@Service
public class ResourceService {
    private final ResourcesMapper resourcesMapper;
    private final VideoDataManager videoDataManager;

    private final Map<String, Supplier<List<Resource>>> resourceMap = new HashMap<>();

    @Autowired
    public ResourceService(TypeService typeService, VideoDataService videoDataService, ResourcesMapper resourcesMapper, VideoDataManager videoDataManager, RecommendService recommendService) {
        this.resourcesMapper = resourcesMapper;
        this.videoDataManager = videoDataManager;
        resourceMap.put("videoTypeResource", typeService::getTypeResource);
        resourceMap.put("videoIssueResource", videoDataService::getIssueResource);
        resourceMap.put("TaskTypeResource", TaskType::getResource);
        resourceMap.put("TaskStatusResource", TaskStatus::getResource);
        resourceMap.put("TaskReasonResource", TaskReason::getResource);
        resourceMap.put("TagTypeResource", TagType::getResource);
        resourceMap.put("recommendIssueResource", recommendService::listIssue);
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
        view.setMarkTime(getMarkTime());
        view.setV(videoDataManager.getNewIssue().toString());
        return view;
    }

    private String getMarkTime() {
        // 设置当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int week = calendar.get(Calendar.DAY_OF_WEEK);

        // 取距离当前日期最近的周日与当前日期相差的天数（count：相差的天数。正数：之后的周日，负数：之前的周日）
        int count;
        if (week <= 4) {
            count = 1 - week;
        } else {
            count = 8 - week;
        }

        // 获取距离当前日期最近的周日日期
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_WEEK, count);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(c.getTime());
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
