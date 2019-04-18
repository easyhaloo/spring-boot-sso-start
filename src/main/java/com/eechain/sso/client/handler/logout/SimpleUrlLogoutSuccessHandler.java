package com.eechain.sso.client.handler.logout;


import com.eechain.sso.client.handler.AbstractAuthTargetUrlRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-09
 */
public class SimpleUrlLogoutSuccessHandler extends AbstractAuthTargetUrlRequestHandler
    implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                              Object authentication) throws Exception {
    super.handle(request, response, authentication);
  }
}
