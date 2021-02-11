package com.tilitili.admin.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.tilitili.common.entity.view.BaseModel;
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
public class VideoDataFileItem implements Serializable {
	@JsonView(VideoView.class)
	private Long av;
	@JsonView(VideoView.class)
	private String name;
	@JsonView(VideoView.class)
	private String img;
	@JsonView(AdminView.class)
	private String type;
	@JsonView(AdminView.class)
	private String owner;
	@JsonView(AdminView.class)
	private String subOwner;
	@JsonView(AdminView.class)
	private Boolean copyright;
	@JsonView(AdminView.class)
	private String pubTime;
	@JsonView(VideoView.class)
	private Long startTime;

	@JsonView(AdminView.class)
	private Integer view;
	@JsonView(AdminView.class)
	private Integer reply;
	@JsonView(AdminView.class)
	private Integer favorite;
	@JsonView(AdminView.class)
	private Integer coin;
	@JsonView(AdminView.class)
	private Integer point;
	@JsonView(AdminView.class)
	private Integer rank;

	@JsonView(VideoView.class)
	private Integer hisRank;
	@JsonView(VideoView.class)
	private Boolean isUp;
	@JsonView(VideoView.class)
	private Boolean isLen;

	//视频中的占位字段
	@JsonView(VideoView.class)
	private String avStr;
	@JsonView(AdminView.class)
	private String nameStr;
	@JsonView(VideoView.class)
	private String typeStr;
	@JsonView(VideoView.class)
	private String ownerStr;
	@JsonView(VideoView.class)
	private String subOwnerStr;
	@JsonView(VideoView.class)
	private String pubTimeStr;

	@JsonView(VideoView.class)
	private String viewStr;
	@JsonView(VideoView.class)
	private String replyStr;
	@JsonView(VideoView.class)
	private String favoriteStr;
	@JsonView(VideoView.class)
	private String coinStr;
	@JsonView(VideoView.class)
	private String pointStr;
	@JsonView(VideoView.class)
	private String rankStr;

	@JsonView(VideoView.class)
	private String hisRankStr;

	//不导出至视频的数据
	@JsonView(AdminView.class)
	private String bv;
	@JsonView(AdminView.class)
	private Integer page;
	@JsonView(AdminView.class)
	private Boolean isPointWarning;
	@JsonView(AdminView.class)
	private Long viewPoint;
	@JsonView(AdminView.class)
	private String a;
	@JsonView(AdminView.class)
	private String b;
	@JsonView(AdminView.class)
	private Long checkPoint;

	public interface VideoView extends BaseModel.BaseView {}

	public interface AdminView extends VideoView {}

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

	public VideoDataFileItem setIsLen(long rank, long hisRank, long moreHisRank) {
		boolean isLen = rank > 0 && hisRank > 0 && moreHisRank > 0;
		isLen &= rank <= 30 && hisRank <= 30 && moreHisRank <=30;
		return setIsLen(isLen);
	}

}
