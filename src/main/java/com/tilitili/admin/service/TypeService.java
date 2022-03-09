package com.tilitili.admin.service;

import com.tilitili.common.entity.view.resource.Resource;
import com.tilitili.common.mapper.rank.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeService {

    private final TypeMapper typeMapper;

    @Autowired
    public TypeService(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    public List<Resource> getTypeResource() {
        return typeMapper.listTypeName().stream()
                .map(Resource::new)
                .collect(Collectors.toList());
    }

}
