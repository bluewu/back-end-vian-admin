package com.vian.admin.entity;

import com.vian.microservice.entity.BaseEntityWithId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "operation_log")
@Getter
@Setter
@ToString
public class OperationLog extends BaseEntityWithId {
  private String resource; // 操作的资源
  private String requestMethod; // 请求方式
  private String beanName; // 操作的类
  private String methodName; // 操作的模块
  private String requestParams; // 请求的参数
  private String responseData; // 返回数据
  private String ipAddress;
}
