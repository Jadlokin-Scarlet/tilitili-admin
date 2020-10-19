package com.tilitili.admin.mapper;

import com.tilitili.admin.entity.VideoData;
import com.tilitili.admin.entity.query.VideoDataQuery;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface VideoDataMapper {


    List<VideoData> listVideoByCondition(VideoDataQuery videoInfoQuery);

    int countVideoByCondition(VideoDataQuery videoInfoQuery);

    @Select("select issue from video_data group by issue order by issue desc")
    List<Integer> listIssue();

    @Update("update video_data set rank = #{rank} where av = #{av}")
    void updateRank(VideoData videoData);

}
