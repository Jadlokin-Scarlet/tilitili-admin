package com.tilitili.admin.entity.view;

import com.tilitili.common.entity.Task;

import java.util.Date;
import java.util.List;

public class BatchTaskView {
	private Long id;
	private Integer status;
	private Integer type;
	private Date createTime;
	private Date updateTime;
	private String remark;
	private Integer reason;

	private Integer totalTaskNumber;
	private Integer waitTaskNumber;
	private Integer successTaskNumber;
	private Integer failTaskNumber;

	private List<Task> taskList;

	public Long getId() {
		return id;
	}

	public BatchTaskView setId(Long id) {
		this.id = id;
		return this;
	}

	public Integer getStatus() {
		return status;
	}

	public BatchTaskView setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public Integer getType() {
		return type;
	}

	public BatchTaskView setType(Integer type) {
		this.type = type;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public BatchTaskView setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public BatchTaskView setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public BatchTaskView setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public Integer getReason() {
		return reason;
	}

	public BatchTaskView setReason(Integer reason) {
		this.reason = reason;
		return this;
	}

	public Integer getTotalTaskNumber() {
		return totalTaskNumber;
	}

	public BatchTaskView setTotalTaskNumber(Integer totalTaskNumber) {
		this.totalTaskNumber = totalTaskNumber;
		return this;
	}

	public Integer getWaitTaskNumber() {
		return waitTaskNumber;
	}

	public BatchTaskView setWaitTaskNumber(Integer waitTaskNumber) {
		this.waitTaskNumber = waitTaskNumber;
		return this;
	}

	public Integer getSuccessTaskNumber() {
		return successTaskNumber;
	}

	public BatchTaskView setSuccessTaskNumber(Integer successTaskNumber) {
		this.successTaskNumber = successTaskNumber;
		return this;
	}

	public Integer getFailTaskNumber() {
		return failTaskNumber;
	}

	public BatchTaskView setFailTaskNumber(Integer failTaskNumber) {
		this.failTaskNumber = failTaskNumber;
		return this;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public BatchTaskView setTaskList(List<Task> taskList) {
		this.taskList = taskList;
		return this;
	}
}
