package com.tilitili.admin.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoInfo {
    private Long av;
    private String name;
    private String img;
    private String type;
    private String owner;
    private Boolean copyright;
    private String pubTime;
    private Long startTime;
    private String bv;
    private String description;
    private Long state;
    private Long attribute;
    private Long duration;
    private Long missionId;
    private String dynamic;
    private Boolean isDelete;
}
