package com.eechain.sso.client.filter;

import com.eechain.sso.client.authentication.AuthInfo;
import com.eechain.sso.client.context.AuthContextHolder;
import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.handler.AuthManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by haloo on 2019-04-19
 */

public class TicketAuthProccessFileter extends AbstractPreAuthProcessFilter {

  public TicketAuthProccessFileter(AuthManager authManager) {
    setAuthManager(authManager);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    Object credentials = getCredentials(request);
    if (credentials == null) {
      super.doFilter(req, resp, filterChain);
    }
    Object principal = getPrincipal(request);
    AuthInfo authInfo = new AuthInfo();
    authInfo.setPrinciple(principal);
    authInfo.setCredential(credentials);
    // 检验，不同使用失败认证策略
    try {
      getAuthManager().verifyTick(authInfo);
    } catch (AuthenticationException ex) {
      failureAuthentication(request, response, ex);
    }
    filterChain.doFilter(req, resp);
  }

  @Override
  protected Object getPrincipal(HttpServletRequest request) {
    return AuthContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  protected Object getCredentials(HttpServletRequest request) {
    return AuthContextHolder.getContext().getAuthentication().getCredential();
  }
}
