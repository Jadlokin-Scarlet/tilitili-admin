package com.tilitili.admin.controller;

import com.tilitili.admin.service.BatchTaskService;
import com.tilitili.common.entity.BatchTask;
import com.tilitili.common.entity.query.BatchTaskQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.BatchTaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/batchTask")
@Validated
@Slf4j
public class BatchTaskController extends BaseController {
    private final BatchTaskMapper batchTaskMapper;
    private final BatchTaskService batchTaskService;

    @Autowired
    public BatchTaskController(BatchTaskMapper batchTaskMapper, BatchTaskService batchTaskService) {
        this.batchTaskMapper = batchTaskMapper;
        this.batchTaskService = batchTaskService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getBatchTaskByCondition(BatchTaskQuery query) {
        int count = batchTaskMapper.count(query);
        List<BatchTask> batchTaskList = batchTaskService.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), batchTaskList);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public BaseModel deleteBatchTask(@PathVariable Long id) {
        batchTaskService.deleteBatchTask(id);
        return new BaseModel("删除成功", true);
    }

    @PostMapping("/testBatchSpiderVideo")
    @ResponseBody
    public BaseModel testBatchSpiderVideo() {
        batchTaskService.testBatchSpiderVideo();
        return new BaseModel("添加任务成功", true);
    }

    @PostMapping("/batchSpiderHiddenVideo")
    @ResponseBody
    public BaseModel batchSpiderHiddenVideo() {
        batchTaskService.batchSpiderHiddenVideo();
        return new BaseModel("添加任务成功", true);
    }

    @PostMapping("/batchSpiderAllVideo")
    @ResponseBody
    public BaseModel batchSpiderAllVideo() {
        batchTaskService.batchSpiderAllVideo();
        return new BaseModel("添加任务成功", true);
    }

    @PostMapping("/batchSpiderAllVideoTag")
    @ResponseBody
    public BaseModel batchSpiderAllVideoTag() {
        batchTaskService.batchSpiderAllVideoTag();
        return new BaseModel("添加任务成功", true);
    }

}
