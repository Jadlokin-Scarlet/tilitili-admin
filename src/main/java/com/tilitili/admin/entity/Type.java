package com.tilitili.admin.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

@Data
@Accessors(chain=true)
public class Type implements Serializable {
    private Integer id;

    private Integer fatherId;

    private String name;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Type)) return false;
        Type type = (Type) o;
        return getId().equals(type.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}