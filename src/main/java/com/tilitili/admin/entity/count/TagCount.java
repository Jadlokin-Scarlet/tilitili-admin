package com.tilitili.admin.entity.count;

import com.tilitili.admin.entity.count.sub.TopTagCount;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class TagCount {
    private List<TopTagCount> topTagList;
}
