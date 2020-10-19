package com.tilitili.admin.entity.query;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class VideoInfoQuery extends BaseQuery<VideoInfoQuery> {
    private Long av;
    private Boolean isDelete;
    private String name;
    private String owner;
    private String bv;
    private String type;
    private Boolean copyright;
}
