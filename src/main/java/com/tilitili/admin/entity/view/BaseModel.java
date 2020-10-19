package com.tilitili.admin.entity.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BaseModel {
    private String message;
    private Boolean success;
    private Object data;

    public BaseModel(String message) {
        this.message = message;
        this.success = false;
    }

    public BaseModel(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public BaseModel(String message, boolean success, Object data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }
}
