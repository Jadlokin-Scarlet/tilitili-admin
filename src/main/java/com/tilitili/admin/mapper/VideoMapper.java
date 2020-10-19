package com.tilitili.admin.mapper;

import com.tilitili.admin.entity.Video;
import com.tilitili.admin.entity.query.VideoQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VideoMapper {
	List<Video> list(VideoQuery videoQuery);
}
