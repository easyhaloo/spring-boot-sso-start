package com.eechain.sso.client.handler.logout;


import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.handler.AbstractAuthTargetUrlRequestHandler;
import com.eechain.sso.client.handler.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-09
 */
public class SimpleUrlLogoutSuccessHandler extends AbstractAuthTargetUrlRequestHandler
    implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) throws Exception {
    super.handle(request, response, authentication);
  }
}
