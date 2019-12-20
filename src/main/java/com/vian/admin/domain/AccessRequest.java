package com.vian.admin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccessRequest {
  String userId;
  String clientId;
  String secret;
  String authorities; // 用户权限（逗号隔开） 如 ROLE_ADMIN,ROLE_USER
}
