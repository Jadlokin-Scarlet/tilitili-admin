package com.tilitili.admin.entity.query;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskQuery extends BaseQuery<TaskQuery> {
    private Long id;
    private Long av;
    private Integer status;
    private Integer type;
}
