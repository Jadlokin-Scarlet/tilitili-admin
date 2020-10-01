package com.tilitili.admin.mapper;

import com.tilitili.admin.entity.Type;
import com.tilitili.admin.entity.Video;
import com.tilitili.admin.query.VideoQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VideoMapper {
	List<Video> list(VideoQuery videoQuery);
}
