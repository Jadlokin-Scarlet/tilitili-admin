package com.tilitili.admin.mapper;

import com.tilitili.admin.entity.VideoInfo;
import com.tilitili.admin.entity.query.VideoInfoQuery;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VideoInfoMapper {

    List<VideoInfo> listVideoByCondition(VideoInfoQuery videoInfoQuery);

    int countVideoByCondition(VideoInfoQuery videoInfoQuery);

    @Update("update video_info set is_delete = 1 where av = #{av}")
    void deleteVideo(Long av);

    @Update("update video_info set is_delete = 0 where av = #{av}")
    void recoveryVideo(Long av);
}
