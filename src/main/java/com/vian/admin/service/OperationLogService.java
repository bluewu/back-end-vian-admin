package com.vian.admin.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.vian.admin.dal.OperationLogRepositroy;
import com.vian.admin.entity.OperationLog;
import com.vian.admin.entity.QOperationLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OperationLogService {
  @Inject private OperationLogRepositroy operationLogRepositroy;

  /** 记录操作日志 */
  public void saveLog(OperationLog log) {
    operationLogRepositroy.save(log);
  }

  public Page<OperationLog> findAll(
      String resource,
      String requestMethod,
      String ipAddress,
      String createUserId,
      String startDate,
      String endDate,
      Pageable pageable) {
    Optional<BooleanExpression> expression =
        getQueryExpression(resource, requestMethod, ipAddress, createUserId, startDate, endDate);
    return expression
        .map(exp -> operationLogRepositroy.findAll(exp, pageable))
        .orElseGet(() -> operationLogRepositroy.findAll(pageable));
  }

  private Optional<BooleanExpression> getQueryExpression(
      String resource,
      String requestMethod,
      String ipAddress,
      String createUserId,
      String startDate,
      String endDate) {
    QOperationLog query = QOperationLog.operationLog;
    List<BooleanExpression> booleanExps = new ArrayList<>(5);
    if (StringUtils.isNotBlank(resource)) {
      booleanExps.add(query.resource.like("%" + resource + "%"));
    }
    if (StringUtils.isNotBlank(requestMethod)) {
      booleanExps.add(query.requestMethod.equalsIgnoreCase(requestMethod));
    }
    if (StringUtils.isNotBlank(ipAddress)) {
      booleanExps.add(query.ipAddress.like("%" + ipAddress + "%"));
    }
    if (StringUtils.isNotBlank(createUserId)) {
      booleanExps.add(query.createUserId.equalsIgnoreCase(createUserId));
    }
    if (StringUtils.isNotBlank(startDate)) {
      startDate = startDate + " 00:00:00";
      LocalDateTime dateTime =
          LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      booleanExps.add(query.createTime.goe(dateTime));
    }
    if (StringUtils.isNotBlank(endDate)) {
      endDate = endDate + " 23:59:59";
      LocalDateTime dateTime =
          LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      booleanExps.add(query.createTime.loe(dateTime));
    }
    return booleanExps.stream().reduce((acc, element) -> acc.and(element));
  }
}
