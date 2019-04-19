package com.eechain.sso.client.handler.logout;

import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.handler.AuthManager;
import com.eechain.sso.client.handler.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-19
 */
public class SSOLogoutHandler implements LogoutHandler {

  private final AuthManager authManager;

  public SSOLogoutHandler(AuthManager authManager) {
    this.authManager = authManager;
  }

  @Override
  public void onLogout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
    authManager.logout(authentication);
  }
}
