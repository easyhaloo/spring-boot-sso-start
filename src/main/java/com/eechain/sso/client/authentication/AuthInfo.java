package com.eechain.sso.client.authentication;

import lombok.Data;

import java.io.Serializable;

/**
 * 认证信息返回体
 * Create by haloo on 2019-04-19
 */
@Data
public class AuthInfo implements Authentication, Serializable {
  private static final long serialVersionUID = -8554797799602550641L;
  private String name;
  private Object principle;
  private Object credential;
  private String access_token;
  private String refreshToken;


  @Override
  public Object getPrincipal() {
    return principle;
  }

  @Override
  public Object getCredential() {
    if (access_token != null) {
      return access_token;
    }
    return credential;
  }

  @Override
  public Boolean isAuthentication() {
    return true;
  }

  @Override
  public String getName() {
    return name;
  }
}
