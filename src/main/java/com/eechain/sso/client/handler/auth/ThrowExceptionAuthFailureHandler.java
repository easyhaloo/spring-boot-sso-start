package com.eechain.sso.client.handler.auth;

import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.handler.AuthFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证失败，直接抛出异常信息
 * Create by haloo on 2019-04-18
 */
public class ThrowExceptionAuthFailureHandler implements AuthFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException ex) throws AuthenticationException {
    throw ex;
  }
}
