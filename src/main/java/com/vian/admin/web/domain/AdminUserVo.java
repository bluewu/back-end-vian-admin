package com.vian.admin.web.domain;

import lombok.Data;

import java.util.List;

@Data
public class AdminUserVo {
  private String id;
  private String username;
  private String name;
  private List<String> authorities;
  private String createTime;
}
