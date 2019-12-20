package com.vian.admin.config;

import com.alibaba.fastjson.JSON;
import com.vian.admin.entity.OperationLog;
import com.vian.admin.event.OperationLogEvent;
import com.vian.core.configuration.event.EventPublisher;
import com.vian.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class RequestLogAspect {
  @Autowired private EventPublisher eventPublisher;
  private ThreadLocal<OperationLog> logThreadLocal = new ThreadLocal<>();

  @Pointcut("execution(* com.vian.admin.web.rest.*.*(..))")
  public void pointcut() {
    log.info("拦截请求start");
  }

  @Before("pointcut()")
  public void doBefore(JoinPoint joinPoint) {

    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    String beanName = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    String uri = request.getRequestURI();
    if ("GET".equals(request.getMethod())) {
      return;
    }

    Object[] paramsArray = joinPoint.getArgs();
    log.info("请求日志拦截： uri={}, method={}, request={}", uri, request.getMethod(), paramsArray);
    // 组装日志数据
    OperationLog optLog = new OperationLog();
    optLog.setResource(uri);
    optLog.setRequestMethod(request.getMethod());
    optLog.setBeanName(beanName);
    optLog.setMethodName(methodName);
    optLog.setRequestParams(argsArrayToString(paramsArray));
    optLog.setIpAddress(HttpRequestUtil.getGateWayIpAddress(request));
    logThreadLocal.set(optLog);
  }

  @AfterReturning(returning = "result", pointcut = "pointcut()")
  public void doAfterReturning(Object result) {
    try {
      // 处理完请求，记录日志
      OperationLog optLog = logThreadLocal.get();
      if (null != optLog) {
        optLog.setResponseData(JSON.toJSONString(result));
        eventPublisher.publish(new OperationLogEvent(this, optLog));
      }
    } catch (Exception e) {
      log.error("***操作请求日志记录失败doAfterReturning()***", e);
    } finally {
      // 清除threadlocal
      logThreadLocal.remove();
    }
  }

  /**
   * 请求参数拼装
   *
   * @param paramsArray
   * @return
   */
  private String argsArrayToString(Object[] paramsArray) {
    String params = "";
    if (paramsArray != null && paramsArray.length > 0) {
      for (int i = 0; i < paramsArray.length; i++) {
        Object jsonObj = JSON.toJSON(paramsArray[i]);
        params += jsonObj.toString() + " ";
      }
    }
    return params.trim();
  }
}
