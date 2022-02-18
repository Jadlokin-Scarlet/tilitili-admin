package com.tilitili.admin.entity.view;

public class BotTaskView {
	private String name;
	private String keyListStr;
	private Integer sort;
	private String description;

	public String getName() {
		return name;
	}

	public BotTaskView setName(String name) {
		this.name = name;
		return this;
	}

	public Integer getSort() {
		return sort;
	}

	public BotTaskView setSort(Integer sort) {
		this.sort = sort;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public BotTaskView setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getKeyListStr() {
		return keyListStr;
	}

	public BotTaskView setKeyListStr(String keyListStr) {
		this.keyListStr = keyListStr;
		return this;
	}
}
