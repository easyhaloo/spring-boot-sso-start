package com.eechain.sso.client.constant;

/**
 * 应答状态码
 * Create by haloo on 2019-04-16
 */
public enum ResponseCode {

  Success(2000, "登陆成功"),
  AccountNotExist(4000, "账户不存在"),
  PasswordInvalid(5000, "密码验证失败");

  int code;
  String reason;


  ResponseCode(int code, String reason) {
    this.code = code;
    this.reason = reason;
  }

  public int getCode() {
    return code;
  }

  public String getReason() {
    return reason;
  }

  public static ResponseCode ofCode(int code) {
    for (ResponseCode resp : ResponseCode.values()) {

      if (code == resp.code) {
        return resp;
      }
    }
    throw new IllegalArgumentException("Unsupported Code:" + code);
  }

}
