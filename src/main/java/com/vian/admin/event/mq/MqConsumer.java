package com.vian.admin.event.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vian.admin.constants.MqConstants;
import com.vian.admin.entity.OperationLog;
import com.vian.admin.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/** mq消费者 */
@Component
@Slf4j
public class MqConsumer {
  @Autowired private ObjectMapper objectMapper;
  @Autowired @Lazy OperationLogService operationLogService;

  /** 添加操作日志 */
  @RabbitListener(
    containerFactory = MqConstants.containerFactory,
    bindings =
        @QueueBinding(
          value = @Queue(value = MqConstants.queue_operationLog, durable = "true"),
          exchange =
              @Exchange(
                value = MqConstants.exchange,
                durable = "true",
                type = ExchangeTypes.TOPIC
              ), // durable = "true" 是否持久化
          key = MqConstants.rountingKey_operationLog
        )
  )
  public void listenerOperationLog(Message message) throws Exception {
    OperationLog optLog = objectMapper.readValue(message.getBody(), OperationLog.class);
    log.debug("mq监听：添加操作日志，request={}", optLog);
    operationLogService.saveLog(optLog);
  }
}
