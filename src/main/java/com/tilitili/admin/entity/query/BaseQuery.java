package com.tilitili.admin.entity.query;

import lombok.Getter;

@Getter
public class BaseQuery<T extends BaseQuery<T>> {
    private String sorter;
    private String sorted = "desc";
    private Integer start = 0;
    private Integer pageSize = 20;
    private Integer current = 1;

    public T setSorter(String sorter) {
        this.sorter = sorter;
        this.sorted = "desc";
        return (T) this;
    }

    public T setSorter(String sorter, String sorted) {
        this.sorter = sorter;
        this.sorted = sorted;
        return (T) this;
    }

    public T setSorted(String sorted) {
        this.sorted = sorted;
        return (T) this;
    }

    public T setStart(Integer start) {
        this.start = start;
        return (T) this;
    }

    public T setCurrent(Integer current) {
        this.current = current;
        return (T)this;
    }

    public T setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T)this;
    }

    public Integer getStart() {
        if (start == 0) {
            return (current - 1) * pageSize;
        }
        return start;
    }

    public Integer getCurrent() {
        if (current == 0) {
            return start / pageSize + 1;
        }
        return current;
    }
}
