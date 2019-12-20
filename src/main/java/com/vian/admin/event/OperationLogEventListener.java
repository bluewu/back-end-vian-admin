package com.vian.admin.event;

import com.vian.admin.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OperationLogEventListener {
  @Autowired private OperationLogService operationLogService;
  /** 保存点赞或收藏记录 */
  @EventListener
  public void saveLog(OperationLogEvent event) {
    // 保存记录
    operationLogService.saveLog(event.getRequest());
  }
}
