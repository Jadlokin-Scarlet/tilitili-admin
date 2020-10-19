package com.tilitili.admin.mapper;

import com.tilitili.admin.entity.Admin;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
public interface AdminMapper {

    @Select("select * from admin where id = #{id}")
    Admin getAdminById(Long id);

    @Select("select * from admin where user_name = #{userName}")
    @ResultMap("adminResults")
    Admin getAdminByName(String userName);

}
