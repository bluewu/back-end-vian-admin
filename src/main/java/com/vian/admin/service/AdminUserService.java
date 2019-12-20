package com.vian.admin.service;

import com.vian.admin.client.VianOauth2Client;
import com.vian.admin.dal.AdminUserRepositroy;
import com.vian.admin.domain.AccessRequest;
import com.vian.admin.domain.AdminResultCode;
import com.vian.admin.domain.Authorities;
import com.vian.admin.entity.AdminUser;
import com.vian.exceptions.VianServerException;
import com.vian.microservice.autoconfigure.VianMicroserviceProperties;
import com.vian.microservice.entity.BaseEntity;
import com.vian.microservice.security.SecurityUtils;
import com.vian.web.domain.res.VianResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminUserService {
  @Inject private AdminUserRepositroy adminUserRepositroy;
  @Inject private VianOauth2Client vianOauth2Client;
  @Inject private PasswordEncoder passwordEncoder;
  @Inject private VianMicroserviceProperties vianMicroserviceProperties;

  /** 登录 */
  public VianResponse<OAuth2AccessToken> login(String login, String password) {
    AdminUser adminUser =
        Optional.ofNullable(
                adminUserRepositroy.findOneByUsernameAndDeletedFlag(
                    login, BaseEntity.NOT_DELETED_FLAG))
            .orElseThrow(() -> new VianServerException(AdminResultCode.USER_NOT_EXIST));
    if (!passwordEncoder.matches(password, adminUser.getPassword())) {
      throw new VianServerException(AdminResultCode.USERNAME_OR_PASSWORD_ERROR);
    }
    AccessRequest request = new AccessRequest();
    request.setClientId(
        vianMicroserviceProperties.getSecurity().getClientAuthorization().getClientId());
    request.setSecret(
        vianMicroserviceProperties.getSecurity().getClientAuthorization().getClientSecret());
    request.setUserId(adminUser.getId());
    request.setAuthorities(adminUser.getAuthorities().stream().collect(Collectors.joining(",")));
    return vianOauth2Client.postAccessToken(request);
  }

  public Page<AdminUser> findAllUsers(Pageable pageable) {
    // TODO: 2018/5/5 管理员列表查询方法更改
    //    return adminUserRepositroy.findAllByDeletedFlag(BaseEntity.NOT_DELETED_FLAG, pageable);
    return adminUserRepositroy.findAll(pageable);
  }

  /** 根据id查询用户 */
  public AdminUser getUserById(String userId) {
    AdminUser adminUser =
        Optional.ofNullable(
                adminUserRepositroy.findOneByIdAndDeletedFlag(userId, BaseEntity.NOT_DELETED_FLAG))
            .orElseThrow(() -> new VianServerException(AdminResultCode.USER_NOT_EXIST));
    return adminUser;
  }

  /** 创建和更新管理员信息 */
  public AdminUser updataUser(AdminUser adminUser) {
    // 密码加密
    if (StringUtils.isBlank(adminUser.getId())) {
      adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
      adminUser.setAuthorities(Arrays.asList(Authorities.ROLE_USER));
    } else {
      AdminUser oldUser =
          Optional.ofNullable(adminUserRepositroy.findOne(adminUser.getId()))
              .orElseThrow(() -> new VianServerException(AdminResultCode.USER_NOT_EXIST));
      // 只修改密码、姓名、权限
      if (StringUtils.isNotBlank(adminUser.getPassword())) {
        oldUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
      }
      if (StringUtils.isNotBlank(adminUser.getName())) {
        oldUser.setName(adminUser.getName());
      }
      if (!CollectionUtils.isEmpty(adminUser.getAuthorities())) {
        oldUser.setAuthorities(adminUser.getAuthorities());
      }
      adminUser = oldUser;
    }
    return adminUserRepositroy.save(adminUser);
  }

  /** 重置密码 */
  public AdminUser resetPassword(String userId, String password) {
    if (StringUtils.isBlank(userId)) {
      userId = SecurityUtils.getCurrentUserId();
    }
    AdminUser adminUser = getUserById(userId);
    adminUser.setPassword(passwordEncoder.encode(password));
    adminUser = adminUserRepositroy.save(adminUser);
    return adminUser;
  }

  public void delete(String id) {
    AdminUser oldUser =
        Optional.ofNullable(adminUserRepositroy.findOne(id))
            .orElseThrow(() -> new VianServerException(AdminResultCode.USER_NOT_EXIST));
    List<String> authorities = oldUser.getAuthorities();
    if (!CollectionUtils.isEmpty(authorities)) {
      if (authorities.contains("ROLE_CJADMIN")) {
        throw new VianServerException(AdminResultCode.CJGLY_CAN_NOT_DELETE);
      }
    }
    adminUserRepositroy.delete(id);
  }
}
