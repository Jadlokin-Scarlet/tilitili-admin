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
public class VideoDataAdminFileItem {
	private Long av;
	private String name;
	private String img;
	private String type;
	private String owner;
	private String externalOwner;
	private Boolean copyright;
	private String pubTime;
	private Long startTime;
	private Long duration;

	private Long view;
	private Long reply;
	private Long favorite;
	private Long coin;
	private Long point;
	private Long rank;

	private Long hisRank;
	private Boolean isUp;
	private Boolean isLen;
	private Integer level;
	private Integer showLength;

	//视频中的占位字段
	private String avStr;
	private String nameStr;
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

	//不导出至视频的数据
	private String bv;
	private Long page;
	private Boolean isPointWarning;
	private Long viewPoint;
	private String a;
	private String b;
	private Long checkPoint;
}
