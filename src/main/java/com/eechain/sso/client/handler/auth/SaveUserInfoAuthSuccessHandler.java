package com.eechain.sso.client.handler.auth;

import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.constant.RequestHeader;
import com.eechain.sso.client.context.AuthContext;
import com.eechain.sso.client.context.AuthContextHolder;
import com.eechain.sso.client.context.DefaultAuthContext;
import com.eechain.sso.client.handler.AuthSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证成功，保存信息到上下文
 * Create by haloo on 2019-04-18
 */
public class SaveUserInfoAuthSuccessHandler implements AuthSuccessHandler {

  private AuthContext authContext;

  private AuthSuccessHandler delegate;

  public SaveUserInfoAuthSuccessHandler() {
    this.authContext = new DefaultAuthContext();
  }

  public SaveUserInfoAuthSuccessHandler(AuthSuccessHandler delegate) {
    this.delegate = delegate;
  }

  public SaveUserInfoAuthSuccessHandler(AuthContext authContext) {
    this.authContext = authContext;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    this.authContext.setAuthentication(authentication);
    AuthContextHolder.setContext(this.authContext);
    Object credential = authentication.getCredential();
    response.setHeader(RequestHeader.ACCESS, (String) credential);
    if (delegate != null) {
      delegate.onAuthenticationSuccess(request, response, authentication);
    }
  }


  public void setAuthContext(AuthContext authContext) {
    this.authContext = authContext;
  }

  public void setDelegate(AuthSuccessHandler delegate) {
    this.delegate = delegate;
  }
}
