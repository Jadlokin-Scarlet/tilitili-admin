package com.tilitili.admin.service;

import com.tilitili.admin.entity.DispatchRecommendResourcesView;
import com.tilitili.admin.entity.DispatchResourcesView;
import com.tilitili.common.emnus.*;
import com.tilitili.common.entity.view.resource.Resource;
import com.tilitili.common.manager.BotTaskManager;
import com.tilitili.common.mapper.rank.RecommendVideoMapper;
import com.tilitili.common.mapper.rank.ResourcesMapper;
import com.tilitili.common.mapper.rank.VideoDataMapper;
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
    private final RecommendVideoMapper recommendVideoMapper;
    private final VideoDataMapper videoDataMapper;

    private final Map<String, Supplier<List<Resource>>> resourceMap = new HashMap<>();

    @Autowired
    public ResourceService(TypeService typeService, VideoDataService videoDataService, ResourcesMapper resourcesMapper, RecommendVideoService recommendVideoService, RecommendVideoMapper recommendVideoMapper, VideoDataMapper videoDataMapper, BotTaskManager botTaskManager) {
        this.resourcesMapper = resourcesMapper;
        this.recommendVideoMapper = recommendVideoMapper;
        this.videoDataMapper = videoDataMapper;
        resourceMap.put("videoTypeResource", typeService::getTypeResource);
        resourceMap.put("videoIssueResource", videoDataService::getIssueResource);
        resourceMap.put("TaskTypeResource", TaskType::getResource);
        resourceMap.put("TaskStatusResource", TaskStatus::getResource);
        resourceMap.put("TaskReasonResource", TaskReason::getResource);
        resourceMap.put("TagTypeResource", TagType::getResource);
        resourceMap.put("recommendIssueResource", recommendVideoService::listIssue);
        resourceMap.put("BotTaskResource", botTaskManager::listTaskResource);
    }

    public List<Resource> getResource(String resourceName) {
        return resourceMap.getOrDefault(resourceName, Collections::emptyList).get();
    }

//    public String getFlagFileResources() {
//        return getFlagResources().toString();
//    }

    public DispatchResourcesView getFlagResources() {
        DispatchResourcesView view = new DispatchResourcesView();
        view.setIsStaffShow1(resourcesMapper.getValueByType(ResourcesType.IS_STAFF_SHOW_1.value));
        view.setIsStaffShow2(resourcesMapper.getValueByType(ResourcesType.IS_STAFF_SHOW_2.value));
        view.setMusicName(resourcesMapper.getValueByType(ResourcesType.MUSIC_NAME.value));
        view.setMusicOwner(resourcesMapper.getValueByType(ResourcesType.MUSIC_OWNER.value));
        view.setMusicCard(resourcesMapper.getValueByType(ResourcesType.MUSIC_CARD.value));
        view.setMusicImage(resourcesMapper.getValueByType(ResourcesType.MUSIC_IMAGE.value));
        view.setMusicSource(resourcesMapper.getValueByType(ResourcesType.MUSIC_SOURCE.value));
        view.setTips(resourcesMapper.getValueByType(ResourcesType.TIPS.value));
        view.setMarkTime(getMarkTime());
        view.setCountTime(getCountTime());
        view.setV(videoDataMapper.getNewIssue().toString());
        return view;
    }

    public DispatchRecommendResourcesView getRecommendFlagResources() {
        DispatchRecommendResourcesView view = new DispatchRecommendResourcesView();
        view.setV(recommendVideoMapper.getNew().getId());
        return view;
    }
    /*
    * 06 07 01 02 03 04 05
    * 00 -1 -2 -3 -4 -5 -6
    */
    private String getCountTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int week = calendar.get(Calendar.DAY_OF_WEEK);

        int count = (12 - week) % 7 - 6;
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");

        calendar.add(Calendar.DAY_OF_WEEK, count);
        String endTime = df.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, -7);
        String startTime = df.format(calendar.getTime());


        return startTime + "~" + endTime;
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
        calendar.add(Calendar.DAY_OF_WEEK, count);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(calendar.getTime());
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
        resourcesMapper.updateValueByType(ResourcesType.TIPS.value, resourcesView.getTips());
    }

}
