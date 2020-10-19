package com.tilitili.admin.entity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageModel<T> {
    private Integer total;
    private Integer pageSize;
    private Integer current;
    private List<T> list;

    public static <T> BaseModel of(Integer total, Integer pageSize, Integer current, List<T> list) {
        PageModel<T> pageModel = new PageModel<T>(total, pageSize, current, list);
        return new BaseModel("查询成功", true, pageModel);
    }

}
