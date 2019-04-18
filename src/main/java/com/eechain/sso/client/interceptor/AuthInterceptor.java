package com.eechain.sso.client.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-08
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {


  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
    return false;
  }

}
