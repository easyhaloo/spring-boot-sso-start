package com.eechain.sso.client.filter;

import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.context.AuthContextHolder;
import com.eechain.sso.client.handler.LogoutHandler;
import com.eechain.sso.client.handler.LogoutSuccessHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.Filter;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by haloo on 2019-04-19
 */
@Slf4j
public class LogoutProccessFileter implements Filter {

  private final LogoutHandler logoutHandler;

  private final LogoutSuccessHandler logoutSuccessHandler;


  public LogoutProccessFileter(LogoutHandler logoutHandler,
                               LogoutSuccessHandler logoutSuccessHandler) {
    this.logoutHandler = logoutHandler;
    this.logoutSuccessHandler = logoutSuccessHandler;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    if (isLogout(req)) {
      Authentication authentication = AuthContextHolder.getContext().getAuthentication();
      if (this.logoutHandler != null) {
        this.logoutHandler.onLogout(req, resp, authentication);
      }
      if (this.logoutSuccessHandler != null) {
        this.logoutSuccessHandler.onLogoutSuccess(req, resp, authentication);
      }
    }
    chain.doFilter(request, response);
  }

  private static boolean isLogout(HttpServletRequest request) {
    String requestUrl = request.getRequestURI();
    if ("logout".equalsIgnoreCase(requestUrl)) {
      return true;
    }
    return false;
  }
}
