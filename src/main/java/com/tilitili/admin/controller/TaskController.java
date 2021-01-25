package com.tilitili.admin.controller;

import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.entity.Task;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.entity.view.SimpleTaskView;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@Controller
@RequestMapping("api/task")
public class TaskController extends BaseController{

    private final TaskMapper taskMapper;
    private final TaskManager taskManager;

    @Autowired
    public TaskController(TaskMapper taskMapper, TaskManager taskManager) {
        this.taskMapper = taskMapper;
        this.taskManager = taskManager;
    }

    @GetMapping("")
    @ResponseBody
    public BaseModel getTaskByCondition(TaskQuery query) {
        int count = taskMapper.count(query);
        List<Task> taskList = taskMapper.list(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), taskList);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel updateTask(@RequestBody Task task) {
        Assert.notNull(task.getIdList(), "参数有误");
        Assert.notNull(task.getStatus(), "参数有误");

        List<Long> idList = StringUtil.splitNumberList(task.getIdList());

        for (Long id : idList) {
            taskMapper.update(new Task().setId(id).setStatus(task.getStatus()));
        }

        return new BaseModel("修改成功", true);
    }

    @PostMapping("")
    @ResponseBody
    public BaseModel spiderVideo(@RequestBody SimpleTaskView simpleTaskView) {
        Assert.notNull(simpleTaskView, "参数有误");
        Assert.notNull(simpleTaskView.getValue(), "参数有误");

        if (simpleTaskView.getReason() == null) {
            simpleTaskView.setReason(TaskReason.NO_REASON.getValue());
        }

        taskManager.simpleSpiderVideo(simpleTaskView);
        return new BaseModel("添加任务成功", true);
    }

}
