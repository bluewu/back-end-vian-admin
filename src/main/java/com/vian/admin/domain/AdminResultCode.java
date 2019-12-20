package com.vian.admin.domain;

import com.vian.domain.ResultCode;

public enum AdminResultCode implements ResultCode {
  OPERATION_SUCCESS("0", "操作成功"),
  OPERATION_FAILD("70500", "操作失败"),
  USERNAME_OR_PASSWORD_ERROR("10000", "用户名或密码错误"),
  USER_NOT_EXIST("70001", "用户不存在"),
  ROLE_NOT_EXIST("70002", "角色不存在"),
  NO_RESOURCE_AUTHORITY("70001", "无权限访问该资源"),
  CJGLY_CAN_NOT_DELETE("70010", "超级管理员不可删除"),
  ;
  private String resultCode;
  private String msg;

  AdminResultCode(String resultCode, String msg) {
    this.resultCode = resultCode;
    this.msg = msg;
  }

  public String getResultCode() {
    return resultCode;
  }

  public String getMsg() {
    return msg;
  }
}
