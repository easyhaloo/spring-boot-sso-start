package com.eechain.sso.client.handler;

import com.eechain.sso.client.authentication.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-09
 */
@FunctionalInterface
public interface LogoutSuccessHandler {

  /**
   * 单点退出成功后调用
   * - 发送状态
   * - 重定向
   * - 转发
   *
   * @param request
   * @param response
   * @param authentication
   * @throws Exception
   */
  void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) throws Exception;
}
