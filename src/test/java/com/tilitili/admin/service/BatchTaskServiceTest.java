package com.tilitili.admin.service;

import com.tilitili.StartApplication;
import com.tilitili.admin.controller.BatchTaskController;
import com.tilitili.admin.entity.view.BatchTaskView;
import com.tilitili.common.emnus.TaskType;
import com.tilitili.common.entity.query.BatchTaskQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.tilitili.BatchTaskMapper;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
class BatchTaskServiceTest extends TestCase {
    @Autowired
    private BatchTaskController batchTaskController;
    @Autowired
    private BatchTaskService batchTaskService;
    @Autowired
    private BatchTaskMapper batchTaskMapper;

    BatchTaskQuery batchTaskQuery = new BatchTaskQuery().setType(TaskType.BatchSpiderVideo.value).setSorter("create_time").setSorted("desc");

    @Test
    void api() {
        int count = batchTaskMapper.countBatchTaskByCondition(batchTaskQuery);
        List<BatchTaskView> batchTaskList = batchTaskService.list(batchTaskQuery);
        PageModel.of(0, batchTaskQuery.getPageSize(), batchTaskQuery.getCurrent(), new ArrayList<>());
    }

    @Test
    void count() {
        int count = batchTaskMapper.countBatchTaskByCondition(batchTaskQuery);
    }

    @Test
    void list() {
        List<BatchTaskView> batchTaskList = batchTaskService.list(batchTaskQuery);
    }

    @Test
    void of() {
        long a = System.currentTimeMillis();
        BaseModel objectPageModel = PageModel.of(0, batchTaskQuery.getPageSize(), batchTaskQuery.getCurrent(), new ArrayList<>());
        long b = System.currentTimeMillis();
        System.out.println(b-a);
    }


}