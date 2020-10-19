package com.tilitili.admin.entity.query;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
public class VideoQuery extends BaseQuery<VideoQuery> {
    private Integer issue;
}
