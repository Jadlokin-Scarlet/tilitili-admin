package com.tilitili.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain=true)
public class VideoDataFileItemV2 implements Serializable {
	private Long av;
	private String avStr;
	private String name;
	private String img;
	private String type;
	private String owner;
	private String externalOwner;
	private String copyright;
	private String pubTime;
	private String startTime;

	private String view;
	private String reply;
	private String favorite;
	private String coin;
	private String point;
	private String rank;

	private String hisRank;
	private String isLen;

	//不导出至视频的数据
	private String bv;
	private Integer page;
	private Boolean isPointWarning;
	private Long viewPoint;
	private String a;
	private String b;
	private Long checkPoint;

	public String toDataFileLine(List<String> fields) {
		BeanMap beanMap = new BeanMap(this);
		return fields.stream()
				.map(field -> toDataFileItem(field, beanMap.get(field)))
				.collect(Collectors.joining("\t"));
	}

	public String toDataFileItem(String field, Object value) {
		Assert.notNull(value, "av=" + av + ",field=" + field + ", value is null");
		if (Objects.equals(field, "pubTime")) {
			return pubTime.split(" ")[0];
		}
		return value.toString();
	}

	public VideoDataFileItemV2 setIsLen(String isLen) {
		this.isLen = isLen;
		return this;
	}

	public VideoDataFileItemV2 setIsLen(long rank, long hisRank, long moreHisRank) {
		boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0;
		isLen &= rank <= 30 && hisRank <= 30 && moreHisRank <=30;
		return setIsLen(isLen? "true" : "false");
	}

}
