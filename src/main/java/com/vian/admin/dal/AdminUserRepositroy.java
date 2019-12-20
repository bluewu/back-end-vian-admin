package com.vian.admin.dal;

import com.vian.admin.entity.AdminUser;
import com.vian.microservice.repository.DefaultDatabaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminUserRepositroy extends DefaultDatabaseRepository<AdminUser> {
  AdminUser findOneByIdAndDeletedFlag(String id, int deleteFlag);

  AdminUser findOneByUsernameAndDeletedFlag(String login, int deleteFlag);

  Page<AdminUser> findAllByDeletedFlag(int deleteFlag, Pageable pageable);
}
