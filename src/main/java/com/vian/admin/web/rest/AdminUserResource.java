package com.vian.admin.web.rest;

import com.vian.admin.domain.Authorities;
import com.vian.admin.entity.AdminUser;
import com.vian.admin.service.AdminUserService;
import com.vian.admin.web.domain.AdminUserFo;
import com.vian.admin.web.domain.AdminUserLoginFo;
import com.vian.admin.web.domain.AdminUserVo;
import com.vian.admin.web.mapper.AdminUserMapper;
import com.vian.microservice.security.SecurityUtils;
import com.vian.microservice.util.PaginationUtil;
import com.vian.web.domain.res.VianResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "后台-用户服务API")
@RestController
@RequestMapping("/api")
public class AdminUserResource {
  @Inject AdminUserService adminUserService;
  @Inject AdminUserMapper adminUserMapper;

  @ApiOperation(value = "登录", nickname = "login", httpMethod = "POST")
  @PostMapping("/public/adminUser/login")
  public ResponseEntity<VianResponse<OAuth2AccessToken>> login(
      @RequestBody @Valid AdminUserLoginFo fo) {
    return ResponseEntity.ok(adminUserService.login(fo.getUsername(), fo.getPassword()));
  }

  @ApiOperation(value = "添加管理员", nickname = "createAdminUser", httpMethod = "POST")
  @Secured(Authorities.ROLE_ADMIN)
  @PostMapping("/adminUser")
  public ResponseEntity<VianResponse<AdminUserVo>> createAdminUser(
      @RequestBody @Valid AdminUserFo fo) {
    AdminUser adminUser = adminUserService.updataUser(adminUserMapper.fo2entity(fo));
    return ResponseEntity.ok(
        VianResponse.builder().result(adminUserMapper.entity2vo(adminUser)).build());
  }

  @ApiOperation(value = "更新管理员信息", nickname = "updataAdminUser", httpMethod = "PUT")
  @PutMapping("/adminUser")
  @Secured(Authorities.ROLE_ADMIN)
  public ResponseEntity<VianResponse<AdminUserVo>> updataAdminUser(
      @RequestBody @Valid AdminUserFo fo) {
    AdminUser adminUser = adminUserService.updataUser(adminUserMapper.fo2entity(fo));
    return ResponseEntity.ok(
        VianResponse.builder().result(adminUserMapper.entity2vo(adminUser)).build());
  }

  @ApiOperation(
      value = "根据token获取用户信息",
      nickname = "getTokenUserById",
      httpMethod = "GET",
      response = List.class,
      notes = "根据token中的userid查询用户信息")
  @GetMapping("/adminUser/")
  public ResponseEntity<VianResponse<AdminUserVo>> getTokenUserById() {
    AdminUser adminUser = adminUserService.getUserById(SecurityUtils.getCurrentUserId());
    return ResponseEntity.ok(
        VianResponse.builder().result(adminUserMapper.entity2vo(adminUser)).build());
  }

  @ApiOperation(
      value = "获取用户信息",
      nickname = "getUserById",
      httpMethod = "GET",
      response = List.class,
      notes = "根据Id查询用户信息")
  @GetMapping("/adminUser/{id}")
  public ResponseEntity<VianResponse<AdminUserVo>> getUserById(
      @ApiParam(value = "id", required = true) @PathVariable String id) {
    AdminUser adminUser = adminUserService.getUserById(id);
    return ResponseEntity.ok(
        VianResponse.builder().result(adminUserMapper.entity2vo(adminUser)).build());
  }

  @ApiOperation(
      value = "获取所有用户信息",
      nickname = "userList",
      httpMethod = "GET",
      response = List.class,
      notes = "默认创建时间降序排列")
  @GetMapping("/adminUser")
  public ResponseEntity<VianResponse<List<AdminUserVo>>> userList(
      @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<AdminUser> page = adminUserService.findAllUsers(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/adminUser");
    return new ResponseEntity<>(
        VianResponse.builder().result(adminUserMapper.entitys2vos(page.getContent())).build(),
        headers,
        HttpStatus.OK);
  }

  @ApiOperation(value = "删除用户信息", httpMethod = "DELETE")
  @DeleteMapping("/adminUser/{id}")
  public ResponseEntity<VianResponse> delete(
      @ApiParam(value = "id", required = true) @PathVariable String id) {
    adminUserService.delete(id);
    return ResponseEntity.ok(VianResponse.builder().result("删除成功").build());
  }
}
