package com.tilitili.admin.entity.request;

public class CopyMappingRequest {
	private Long fromTaskId;
	private Long toTaskId;

	public Long getFromTaskId() {
		return fromTaskId;
	}

	public CopyMappingRequest setFromTaskId(Long fromTaskId) {
		this.fromTaskId = fromTaskId;
		return this;
	}

	public Long getToTaskId() {
		return toTaskId;
	}

	public CopyMappingRequest setToTaskId(Long toTaskId) {
		this.toTaskId = toTaskId;
		return this;
	}
}
