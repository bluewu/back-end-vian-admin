package com.vian.admin.web.mapper;

import com.vian.admin.entity.OperationLog;
import com.vian.admin.web.domain.OperationLogVo;
import com.vian.web.mapper.WebVOMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperationLogWebMapper  extends WebVOMapper<OperationLog, OperationLogVo> {
    @Override
    @Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    OperationLogVo dto2vo(OperationLog entity);
}
