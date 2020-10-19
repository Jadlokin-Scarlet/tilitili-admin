package com.tilitili.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Resource<T> {
    private T value;
    private T text;

    public Resource(T text) {
        this.text = text;
        this.value = text;
    }
}
