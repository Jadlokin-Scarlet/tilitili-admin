package com.tilitili.admin.entity.message;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TaskMessage {
    private Long id;
    private Long av;
}
