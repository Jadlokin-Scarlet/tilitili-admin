package com.tilitili.admin.entity.view;

import com.tilitili.common.entity.RecommendVideo;
import com.tilitili.common.entity.dto.RecommendDTO;

import java.util.List;

public class RecommendVideoView extends RecommendVideo {
	//recommend表
	private Integer recommendNumber;
	private Integer selfRecommendNumber;
	private List<RecommendDTO> recommendList;
	private List<RecommendDTO> selfRecommendList;

	//recommend talk表
	private Boolean hasTalk;

	public Integer getRecommendNumber() {
		return recommendNumber;
	}

	public RecommendVideoView setRecommendNumber(Integer recommendNumber) {
		this.recommendNumber = recommendNumber;
		return this;
	}

	public Integer getSelfRecommendNumber() {
		return selfRecommendNumber;
	}

	public RecommendVideoView setSelfRecommendNumber(Integer selfRecommendNumber) {
		this.selfRecommendNumber = selfRecommendNumber;
		return this;
	}

	public List<RecommendDTO> getRecommendList() {
		return recommendList;
	}

	public RecommendVideoView setRecommendList(List<RecommendDTO> recommendList) {
		this.recommendList = recommendList;
		return this;
	}

	public List<RecommendDTO> getSelfRecommendList() {
		return selfRecommendList;
	}

	public RecommendVideoView setSelfRecommendList(List<RecommendDTO> selfRecommendList) {
		this.selfRecommendList = selfRecommendList;
		return this;
	}

	public Boolean getHasTalk() {
		return hasTalk;
	}

	public RecommendVideoView setHasTalk(Boolean hasTalk) {
		this.hasTalk = hasTalk;
		return this;
	}
}
