package com.tilitili.admin.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class VideoDataFileItem implements Serializable {
	private Long av;
	private String name;
	private String img;
	private String type;
	private String owner;
	private Boolean copyright;
	private String pubTime;
	private Long startTime;

	private Integer view;
	private Integer reply;
	private Integer favorite;
	private Integer coin;
	private Integer point;
	private Integer rank;

	private Integer hisRank;
	private Integer isLen;

	public String toString(List<String> fields) {
		final BeanMap beanMap = new BeanMap(this);
		return fields.stream()
				.peek(field -> Assert.notNull(beanMap.get(field), "av=" + beanMap.get("av") + ",field=" + field + ", value is null"))
				.map(beanMap::get)
				.map(Object::toString)
				.collect(Collectors.joining("\t"));
	}

	public VideoDataFileItem setIsLen(Integer isLen) {
		this.isLen = isLen;
		return this;
	}

	public VideoDataFileItem setIsLen(long rank, long hisRank, long moreHisRank) {
		boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0;
		isLen &= rank <= 30 && hisRank <= 30 && moreHisRank <=30;
		return setIsLen(isLen? 1 : 0);
	}

}
