package com.tilitili.admin.query;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
public class BaseQuery<T extends BaseQuery<T>> {
    private String sorter;
    private String sorted;

    private Integer top;

    public T setSorter(String sorter) {
        this.sorter = sorter;
        return (T) this;
    }

    public T setSorted(String sorted) {
        this.sorted = sorted;
        return (T) this;
    }

    public T setTop(Integer top) {
        this.top = top;
        return (T) this;
    }
}
