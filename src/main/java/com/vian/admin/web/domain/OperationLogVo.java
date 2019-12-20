package com.vian.admin.web.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("操作日志")
@Data
public class OperationLogVo {
    private String id;
    @ApiModelProperty(notes = "操作的资源")
    private String resource;  
    @ApiModelProperty(notes = "请求方式")
    private String requestMethod;  
    @ApiModelProperty(notes = "操作的类")
    private String beanName;  
    @ApiModelProperty(notes = "操作的模块")
    private String methodName;  
    @ApiModelProperty(notes = "请求的参数")
    private String requestParams;  
    @ApiModelProperty(notes = "返回数据")
    private String responseData; 
    @ApiModelProperty(notes = "请求ip")
    private String ipAddress;
    private String createUserId;
    private String createTime;
}
