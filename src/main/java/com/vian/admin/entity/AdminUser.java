package com.vian.admin.entity;

import com.vian.microservice.entity.BaseEntityWithId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "v_user")
@Getter
@Setter
public class AdminUser extends BaseEntityWithId {

  @Indexed(unique = true, dropDups = true)
  private String username;

  private String password;

  private String name;

  private List<String> authorities;
}
