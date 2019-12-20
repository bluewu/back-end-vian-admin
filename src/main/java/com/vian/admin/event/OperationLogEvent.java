package com.vian.admin.event;

import com.vian.admin.entity.OperationLog;
import com.vian.core.configuration.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationLogEvent extends BaseEvent<OperationLog> {

  public OperationLogEvent(Object source, OperationLog operationLog) {
    super(source, operationLog);
  }
}
