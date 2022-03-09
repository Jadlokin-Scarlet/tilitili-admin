package com.tilitili.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class VideoDataFileItem {
	private Long av;
	private String name;
	private String img;
	private Long startTime;

	private Long hisRank;
	private Boolean isUp;
	private Boolean isLen;
	private Integer level;
	private Integer showLength;

	//视频中的占位字段
	private String avStr;
	private String typeStr;
	private String ownerStr;
	private String subOwnerStr;
	private String pubTimeStr;

	private String viewStr;
	private String replyStr;
	private String favoriteStr;
	private String coinStr;
	private String pointStr;
	private String rankStr;

	private String hisRankStr;

}
