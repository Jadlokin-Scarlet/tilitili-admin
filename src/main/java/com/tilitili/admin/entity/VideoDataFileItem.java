package com.tilitili.admin.entity;

import lombok.*;
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
public class VideoDataFileItem implements Serializable {
	private Long av;
	private String name;
	private String img;
	private String type;
	private String owner;
	private Integer copyright;
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

	public VideoDataFileItem setIsLen(Integer isLen) {
		this.isLen = isLen;
		return this;
	}

	public VideoDataFileItem setIsLen(long rank, long hisRank, long moreHisRank) {
		boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0;
		isLen &= rank <= 30 && hisRank <= 30 && moreHisRank <=30;
		return setIsLen(isLen? 1 : 0);
	}

	public VideoDataFileItem setView(Integer view) {
		this.view = view;
		return this;
	}

	public VideoDataFileItem setView(Long view) {
		this.view = view.intValue();
		return this;
	}

	public VideoDataFileItem setReply(Integer reply) {
		this.reply = reply;
		return this;
	}

	public VideoDataFileItem setReply(Long reply) {
		this.reply = reply.intValue();
		return this;
	}

	public VideoDataFileItem setFavorite(Integer favorite) {
		this.favorite = favorite;
		return this;
	}

	public VideoDataFileItem setFavorite(Long favorite) {
		this.favorite = favorite.intValue();
		return this;
	}

	public VideoDataFileItem setCoin(Integer coin) {
		this.coin = coin;
		return this;
	}

	public VideoDataFileItem setCoin(Long coin) {
		this.coin = coin.intValue();
		return this;
	}

	public VideoDataFileItem setPage(Integer page) {
		this.page = page;
		return this;
	}

	public VideoDataFileItem setPage(Long page) {
		this.page = page.intValue();
		return this;
	}
}
