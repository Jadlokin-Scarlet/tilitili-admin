package com.tilitili.admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RecommendFileItem {
    private Long av;
    private String operator;
    private Integer startTime;
    private Integer endTime;
    private String face;

    private String avStr;
    private String nameStr;
    private String operatorStr;
    private String ownerStr;
    private String externalOwnerStr;
    private String typeStr;
    private String pubTimeStr;
}
