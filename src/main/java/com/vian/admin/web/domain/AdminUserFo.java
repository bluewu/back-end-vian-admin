package com.vian.admin.web.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "v_user")
@Getter
@Setter
public class AdminUserFo {
  private String id;
  private String username;
  private String password;
  private String name;
  private List<String> authorities;
}
