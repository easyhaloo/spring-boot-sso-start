package com.eechain.sso.client.handler.logout;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by haloo on 2019-04-09
 */
public final class CompositeLogoutHandler implements LogoutHandler {

  private List<LogoutHandler> logoutHandlers = new ArrayList<>();

  public CompositeLogoutHandler(List<LogoutHandler> logoutHandlers) {
    this.logoutHandlers = logoutHandlers;
  }

  public CompositeLogoutHandler(LogoutHandler first, LogoutHandler... logoutHandlers) {
    this.logoutHandlers.add(first);
    if (logoutHandlers != null) {
      for (LogoutHandler logoutHandler : logoutHandlers) {
        this.logoutHandlers.add(logoutHandler);
      }
    }
  }

  @Override
  public void onLogout(HttpServletRequest request, HttpServletResponse response, Object authentication) {
    this.logoutHandlers.forEach((handler) -> onLogout(request, response, authentication));
  }
}