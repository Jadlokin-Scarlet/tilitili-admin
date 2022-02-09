package com.tilitili.admin.entity.view;

import com.tilitili.common.entity.RecommendTalk;

public class RecommendTalkView extends RecommendTalk {
	private String op;
	private String ed;

	public String getOp() {
		return op;
	}

	public RecommendTalkView setOp(String op) {
		this.op = op;
		return this;
	}

	public String getEd() {
		return ed;
	}

	public RecommendTalkView setEd(String ed) {
		this.ed = ed;
		return this;
	}
}
