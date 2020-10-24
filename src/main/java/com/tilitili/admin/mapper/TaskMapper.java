package com.tilitili.admin.mapper;

import com.tilitili.admin.entity.Task;
import com.tilitili.admin.entity.VideoData;
import com.tilitili.admin.entity.query.TaskQuery;
import com.tilitili.admin.entity.query.VideoDataQuery;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TaskMapper {

    List<Task> listTaskByCondition(TaskQuery taskQuery);

    int countTaskByCondition(TaskQuery taskQuery);

    @Insert("insert into task(av, create_time, update_time, status, type) values (#{av}, getdate(), getdate(), 0, #{type})")
    @Options(useGeneratedKeys = true)
    int addTask(Task task);

}
