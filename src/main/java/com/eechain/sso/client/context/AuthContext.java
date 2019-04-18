package com.eechain.sso.client.context;

import com.eechain.sso.client.authentication.Authentication;

import java.io.Serializable;

/**
 * 认证信息上下文
 * Create by haloo on 2019-04-09
 */
public interface AuthContext extends Serializable {

  Authentication getAuthentication();


  void setAuthentication(Authentication authentication);
}
