package com.tilitili.admin.job;

import com.tilitili.admin.service.TaskService;
import com.tilitili.common.emnus.TaskReason;
import com.tilitili.common.emnus.TaskType;
import com.tilitili.common.entity.BatchTask;
import com.tilitili.common.mapper.BatchTaskMapper;
import com.tilitili.common.mapper.TouhouAllMapper;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Component
public class VideoInfoCheckJob extends QuartzJobBean {

    private final TouhouAllMapper touhouAllMapper;
    private final TaskService taskService;
    private final BatchTaskMapper batchTaskMapper;

    @Autowired
    public VideoInfoCheckJob(TouhouAllMapper touhouAllMapper, TaskService taskService, BatchTaskMapper batchTaskMapper) {
        this.touhouAllMapper = touhouAllMapper;
        this.taskService = taskService;
        this.batchTaskMapper = batchTaskMapper;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("【VideoInfoCheckJob】check video info start");
        BatchTask batchTask = new BatchTask().setType(TaskType.AutoBatchSpiderVideo.getValue()).setReason(TaskReason.SUPPLEMENT_VIDEO_INFO.getValue());
        List<Long> avList = touhouAllMapper.checkVideoInfo();
        taskService.batchSpiderVideo(batchTask, avList);
        log.info("【VideoInfoCheckJob】check video info end");
    }
    @Bean
    public JobDetail videoInfoCheckJobDetail() {
        return newJob(this.getClass()).withIdentity(this.getClass().getName()).storeDurably().build();
    }

    @Bean
    public Trigger videoInfoCheckTrigger(JobDetail jobDetail) {
        return newTrigger().withSchedule(cronSchedule("0 18 1/2 * * ?")).withIdentity(this.getClass().getName()).forJob(jobDetail).build();
    }

}
