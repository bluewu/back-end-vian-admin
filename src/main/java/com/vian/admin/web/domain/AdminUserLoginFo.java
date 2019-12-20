package com.vian.admin.web.domain;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("用户登录请求模型")
@Getter
@Setter
public class AdminUserLoginFo {
  @NotNull(message = "登录名不能为空")
  private String username;

  @NotNull(message = "密码不能为空")
  private String password;
}
