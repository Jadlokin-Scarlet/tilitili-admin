package com.tilitili.admin.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain=true)
public class Owner implements Serializable {
    private Long uid;

    private String name;

    private String face;

    private Short vipType;

    private static final long serialVersionUID = 1L;
}