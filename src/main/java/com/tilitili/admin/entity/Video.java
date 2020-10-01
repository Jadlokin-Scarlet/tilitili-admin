package com.tilitili.admin.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class Video implements Serializable {
	private Long av;
	private String name;
	private String img;
	private String type;
	private Boolean copyright;
	private String pubTime;
	private Long startTime;
	private String bv;
	private String description;
	private Long state;
	private Long attribute;
	private Long duration;
	private Long missionId;
	private String dynamic;

	private Integer issue;
	private Integer view;
	private Integer reply;
	private Integer favorite;
	private Integer coin;
	private Integer page;
	private Integer point;
	private Integer rank;
	private Integer danmaku;
	private Integer share;
	private Integer like;
	private Integer dislike;
	private String evaluation;

	private String owner;
	private String face;

	private List<String> tags;
	private List<VideoPage> pages;

	private Integer hisRank;
	private Integer isLen;

	private static final long serialVersionUID = 1L;

//	public Video(Object o) {
//		copyProperties(o);
//	}

	public String toString(List<String> fields) {
		final BeanMap beanMap = new BeanMap(this);
		return fields.stream()
				.map(beanMap::get)
				.map(Object::toString)
				.collect(Collectors.joining("\t"));
	}






//	public Video copyProperties(Object o) {
//		if (o != null) {
//			BeanUtils.copyProperties(o, this);
//		}
//		return this;
//	}
//
//	public Video setIsLen(long rank, long hisRank, long moreHisRank) {
//		boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0;
//		isLen &= rank <= 30 && hisRank <= 30 && moreHisRank <=30;
//		return setIsLen(isLen? 1L : 0L);
//	}
}
