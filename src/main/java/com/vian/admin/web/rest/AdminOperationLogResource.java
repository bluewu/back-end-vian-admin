package com.vian.admin.web.rest;

import com.vian.admin.entity.OperationLog;
import com.vian.admin.service.OperationLogService;
import com.vian.admin.web.domain.OperationLogVo;
import com.vian.admin.web.mapper.OperationLogWebMapper;
import com.vian.microservice.util.PaginationUtil;
import com.vian.web.domain.res.VianResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@Api(tags = "后台-操作日志API")
@RestController
@RequestMapping("/api/admin")
public class AdminOperationLogResource {
  @Inject OperationLogService operationLogService;
  @Inject OperationLogWebMapper operationLogWebMapper;

  @ApiOperation(value = "查询操作日志", nickname = "findAll", httpMethod = "GET", notes = "默认创建时间降序倒序")
  @GetMapping("/operationLog")
  @ApiImplicitParams({
    @ApiImplicitParam(
        name = "resource",
        value = "请求资源(模糊查询) 如/vian-bcs/api/admin/newsUser",
        paramType = "query"),
    @ApiImplicitParam(name = "method", value = "请求方式 POST PUT DELETE", paramType = "query"),
    @ApiImplicitParam(name = "ipAddress", value = "ip地址(模糊查询)", paramType = "query"),
    @ApiImplicitParam(name = "createUserId", value = "创建人", paramType = "query"),
    @ApiImplicitParam(name = "startDate", value = "开始时间 yyyy-MM-dd格式", paramType = "query"),
    @ApiImplicitParam(name = "endDate", value = "结束时间 yyyy-MM-dd格式", paramType = "query")
  })
  public ResponseEntity<VianResponse<List<OperationLogVo>>> findAll(
      String resource,
      String method,
      String ipAddress,
      String createUserId,
      String startDate,
      String endDate,
      @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable) {

    Page<OperationLog> page =
        operationLogService.findAll(
            resource, method, ipAddress, createUserId, startDate, endDate, pageable);
    HttpHeaders headers =
        PaginationUtil.generatePaginationHttpHeaders(page, "/api/admin/operationLog");
    return new ResponseEntity<>(
        VianResponse.builder().result(operationLogWebMapper.dtos2vos(page.getContent())).build(),
        headers,
        HttpStatus.OK);
  }
}
