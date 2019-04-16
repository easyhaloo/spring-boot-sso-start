package com.eechain.sso.client.handler.logout;

import com.eechain.sso.client.context.AuthContext;
import com.eechain.sso.client.context.AuthContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 上下文处理handler
 * Create by haloo on 2019-04-09
 */
@Slf4j
public class ContextLogoutHandler implements LogoutHandler {

  private boolean invalidateHttpSession = true;
  private boolean clearAuthentication = true;


  @Override
  public void onLogout(HttpServletRequest request, HttpServletResponse response, Object authentication) {
    Assert.notNull(request, "HttpServletRequest required");
    // 让session 失效
    if (invalidateHttpSession) {
      HttpSession session = request.getSession(false);
      if (session != null) {
        log.debug("Invalidating session : {}", session.getId());
        session.invalidate();
      }
    }
    // 清除上下文信息
    if (clearAuthentication) {
      AuthContext context = AuthContextHolder.getContext();
      context.setAuthentication(null);
    }
    AuthContextHolder.clearContext();
  }

  public boolean isInvalidateHttpSession() {
    return invalidateHttpSession;
  }

  public void setInvalidateHttpSession(boolean invalidateHttpSession) {
    this.invalidateHttpSession = invalidateHttpSession;
  }

  public boolean isClearAuthentication() {
    return clearAuthentication;
  }

  public void setClearAuthentication(boolean clearAuthentication) {
    this.clearAuthentication = clearAuthentication;
  }
}
