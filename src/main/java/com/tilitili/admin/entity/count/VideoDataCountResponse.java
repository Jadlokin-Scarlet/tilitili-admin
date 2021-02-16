package com.tilitili.admin.entity.count;

import com.tilitili.admin.entity.count.sub.VideoDataAddCount;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class VideoDataCountResponse {
    private List<VideoDataAddCount> videoDataAddCountList;
}
