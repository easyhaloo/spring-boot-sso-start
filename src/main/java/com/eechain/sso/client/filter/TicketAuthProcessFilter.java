package com.eechain.sso.client.filter;

import com.eechain.sso.client.authentication.AuthInfo;
import com.eechain.sso.client.constant.RequestHeader;
import com.eechain.sso.client.context.AuthContextHolder;
import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.handler.AuthManager;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class TicketAuthProcessFilter extends AbstractPreAuthProcessFilter {

  private static final String[] WHITE = {"/login", "/logout"};

  public TicketAuthProcessFilter(AuthManager authManager) {
    setAuthManager(authManager);
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
      throws IOException, ServletException {
    log.info("cross ticketAuthProcessFilter....");
    if (excludeFilterPath(((HttpServletRequest) req).getRequestURI())) {
      filterChain.doFilter(req, resp);
    } else {

      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) resp;
      Object credentials = getCredentials(request);
      // 认证凭据不在 去登陆
      if (credentials == null) {
        super.doFilter(req, resp, filterChain);
      }
      AuthInfo authInfo = new AuthInfo();
      authInfo.setCredential(credentials);
      // 检验，不通过使用失败认证策略
      try {
        getAuthManager().verifyTick(authInfo);
      } catch (AuthenticationException ex) {
        failureAuthentication(request, response, ex);
      }
      if (!response.isCommitted()) {
        filterChain.doFilter(req, resp);
      }
    }
  }

  // 过滤白名单
  private static boolean excludeFilterPath(String url) {
    for (String s : WHITE) {
      if (url.contains(s)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected Object getPrincipal(HttpServletRequest request) {
    if (AuthContextHolder.getContext().getAuthentication() == null) {
      return null;
    }
    return AuthContextHolder.getContext().getAuthentication().getPrincipal();
  }

  @Override
  protected Object getCredentials(HttpServletRequest request) {
    if (AuthContextHolder.getContext().getAuthentication() == null) {
      return request.getHeader(RequestHeader.ACCESS);
    }
    return AuthContextHolder.getContext().getAuthentication().getCredential();
  }
}
