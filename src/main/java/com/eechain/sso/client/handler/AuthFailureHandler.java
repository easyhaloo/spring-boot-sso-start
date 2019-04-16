package com.eechain.sso.client.handler;

import com.eechain.sso.client.exception.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-09
 */
public interface AuthFailureHandler {

  /**
   *  认证失败，调用此处理器
   * @param request
   * @param response
   * @param ex
   * @throws Exception
   */
  void onAuthenticationFailure(HttpServletRequest request,
                               HttpServletResponse response, AuthenticationException ex)
      throws Exception;
}
