package com.tilitili.admin.entity.count;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class VideoInfoCountRequest {
    private Integer time;
    private String type;
}
