package com.vian.admin.constants;

public interface MqConstants {
  /** ---------------交换机---------------- */
  String containerFactory = "rabbitListenerContainerFactory";
  /** ---------------交换机---------------- */
  String exchange = "exchange-admin";

  /** 操作日志队列 */
  String queue_operationLog = "admin-operationLog";

  /** 操作日志路由 */
  String rountingKey_operationLog = "adminOperationLog";
}
