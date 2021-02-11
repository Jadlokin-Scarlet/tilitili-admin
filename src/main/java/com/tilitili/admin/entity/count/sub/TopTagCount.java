package com.tilitili.admin.entity.count.sub;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TopTagCount {
    private String tag;
    private Integer number;
}
