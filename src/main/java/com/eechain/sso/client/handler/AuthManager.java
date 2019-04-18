package com.eechain.sso.client.handler;

import com.eechain.sso.client.authentication.AuthToken;
import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.exception.AuthenticationException;


/**
 * 认证管理类
 * Create by haloo on 2019-04-18
 */
public interface AuthManager {

  /**
   * 认证成功，则返回认证信息
   * 返回失败，则抛出AuthenticationException
   *
   * @param authToken
   * @return
   * @throws AuthenticationException
   */
  Authentication onAuthenticated(AuthToken authToken) throws AuthenticationException;
}
