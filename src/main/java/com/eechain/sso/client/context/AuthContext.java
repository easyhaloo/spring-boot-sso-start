package com.eechain.sso.client.context;

import java.io.Serializable;

/**
 * 认证信息上下文
 * Create by haloo on 2019-04-09
 */
public interface AuthContext extends Serializable {

  Object getAuthentication();


  void setAuthentication(Object authentication);
}
