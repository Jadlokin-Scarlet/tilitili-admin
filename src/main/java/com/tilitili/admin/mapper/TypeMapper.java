package com.tilitili.admin.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TypeMapper {

    @Select("select name from type where status = 0")
    List<String> listTypeName();

}
