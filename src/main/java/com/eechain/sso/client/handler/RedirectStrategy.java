package com.eechain.sso.client.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create b
 * y haloo on 2019-04-09
 */
public interface RedirectStrategy {

  /**
   * 重定向决策类
   *
   * @param request
   * @param response
   * @param targetUrl
   * @throws Exception
   */
  void redirect(HttpServletRequest request, HttpServletResponse response,
                String targetUrl) throws IOException, ServletException;
}
