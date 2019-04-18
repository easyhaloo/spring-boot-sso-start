package com.eechain.sso.client.handler;

import com.eechain.sso.client.authentication.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-09
 */
public interface AuthSuccessHandler {

  /**
   * 认证成功调用的处理器
   *
   * @param request
   * @param response
   * @param authentication
   * @throws Exception
   */
  void onAuthenticationSuccess(HttpServletRequest request
      , HttpServletResponse response, Authentication authentication) throws Exception;
}
