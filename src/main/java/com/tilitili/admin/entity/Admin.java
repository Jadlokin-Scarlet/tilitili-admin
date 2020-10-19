package com.tilitili.admin.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin {
    private Long id;
    private String userName;
    private String password;
    private Integer status;
    private Integer type;
}
