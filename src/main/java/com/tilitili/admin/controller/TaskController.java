package com.tilitili.admin.controller;

import com.tilitili.admin.entity.SimpleTaskView;
import com.tilitili.admin.utils.StringUtil;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.emnus.TaskStatus;
import com.tilitili.common.entity.Task;
import com.tilitili.common.entity.query.TaskQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import com.tilitili.common.entity.view.message.SimpleTask;
import com.tilitili.common.manager.TaskManager;
import com.tilitili.common.mapper.rank.TaskMapper;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    public BaseModel<PageModel<Task>> getTaskByCondition(TaskQuery query) {
        Asserts.notNull(query, "参数异常");

        if (query.getSorted() == null) query.setSorted("desc");
        if (query.getPageNo() == null) query.setPageNo(1);
        if (query.getPageSize() == null) query.setPageSize(20);

        int count = taskMapper.countTaskByCondition(query);
        List<Task> taskList = taskMapper.getTaskByCondition(query);
        return PageModel.of(count, query.getPageSize(), query.getCurrent(), taskList);
    }

    @PatchMapping("")
    @ResponseBody
    public BaseModel<?> updateTask(@RequestBody TaskQuery query) {
        Asserts.notNull(query.getIdList(), "参数有误");
        Asserts.notNull(query.getStatus(), "参数有误");

        List<Long> idList = StringUtil.splitNumberList(query.getIdList());

        for (Long id : idList) {
            taskMapper.updateTaskSelective(new Task().setId(id).setStatus(query.getStatus()));
            if (Objects.equals(query.getStatus(), TaskStatus.WAIT.value)) {
                taskManager.reSpiderVideo(id);
            }
        }

        return new BaseModel<>("修改成功", true);
    }

    @PostMapping("")
    @ResponseBody
    public BaseModel<?> spiderVideo(@RequestBody SimpleTaskView simpleTaskView) {
        Asserts.notNull(simpleTaskView, "参数有误");
        Asserts.notNull(simpleTaskView.getValue(), "参数有误");

        SimpleTask simpleTask = new SimpleTask().setReason(simpleTaskView.getReason()).setValueList(Collections.singletonList(simpleTaskView.getValue()));
        if (simpleTask.getReason() == null) {
            simpleTask.setReason(TaskReason.NO_REASON.getValue());
        }

        taskManager.simpleSpiderVideo(simpleTask);
        return new BaseModel<>("添加任务成功", true);
    }

}
