package com.tilitili.admin.controller;

import com.tilitili.common.entity.Resource;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.admin.service.ResourceService;
import com.tilitili.common.entity.view.DispatchResourcesView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/resources")
@Validated
@Slf4j
public class ResourceController extends BaseController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getResources(@RequestParam List<String> needResourcesList) {
        HashMap<String, List<Resource>> resourceMap = new HashMap<>();
        needResourcesList.forEach(
                resourceName -> resourceMap.put(resourceName, resourceService.getResource(resourceName))
        );
        return new BaseModel("查询成功", true, resourceMap);
    }

    @GetMapping("/flag")
    @ResponseBody
    public BaseModel getFlag() {
        DispatchResourcesView flagResource = resourceService.getFlagResources();
        return new BaseModel("success", true, flagResource);
    }

    @GetMapping("/flag.txt")
    @ResponseBody
    public String getFlagText() {
        return resourceService.getFlagFileResources();
    }

    @PatchMapping("/flag")
    @ResponseBody
    public BaseModel updateFlag(@RequestBody DispatchResourcesView resourcesView) {
        Assert.notNull(resourcesView.getIsStaffShow1(), "参数异常");
        Assert.notNull(resourcesView.getIsStaffShow2(), "参数异常");
        Assert.notNull(resourcesView.getMusicName(), "参数异常");
        Assert.notNull(resourcesView.getMusicOwner(), "参数异常");
        Assert.notNull(resourcesView.getMusicCard(), "参数异常");
        Assert.notNull(resourcesView.getMusicImage(), "参数异常");
        Assert.notNull(resourcesView.getMusicSource(), "参数异常");
        resourceService.updateFlagResources(resourcesView);
        DispatchResourcesView flagResource = resourceService.getFlagResources();
        return new BaseModel("保存成功", true, flagResource);
    }
}
