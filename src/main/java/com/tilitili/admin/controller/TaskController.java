package com.tilitili.admin.controller;

import com.tilitili.common.entity.Task;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.mapper.TaskMapper;
import com.tilitili.admin.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/task")
@Validated
@Slf4j
public class TaskController {

    private final TaskMapper taskMapper;
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskMapper taskMapper, TaskService taskService) {
        this.taskMapper = taskMapper;
        this.taskService = taskService;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getTaskByCondition(TaskQuery query) {
        int count = taskMapper.count(query);
        List<Task> taskList = taskMapper.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), taskList);
    }

    @PostMapping("")
    @ResponseBody
    public BaseModel spiderVideo(@RequestBody Long av) {
        Assert.notNull(av, "参数有误");
        taskService.spiderVideo(av);
        return new BaseModel("添加任务成功", true);
    }

}