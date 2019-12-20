package com.vian.admin.web.mapper;

import com.vian.admin.entity.AdminUser;
import com.vian.admin.web.domain.AdminUserFo;
import com.vian.admin.web.domain.AdminUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {
  @Mapping(target = "createTime", source = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
  AdminUserVo entity2vo(AdminUser entity);

  List<AdminUserVo> entitys2vos(List<AdminUser> entitys);

  AdminUser fo2entity(AdminUserFo fo);
}
