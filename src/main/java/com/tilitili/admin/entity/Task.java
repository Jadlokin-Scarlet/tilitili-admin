package com.tilitili.admin.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class Task {
    private Long id;
    private Long av;
    private Date createTime;
    private Date updateTime;
    private Integer status;
    private Integer type;
    private String remark;
}
