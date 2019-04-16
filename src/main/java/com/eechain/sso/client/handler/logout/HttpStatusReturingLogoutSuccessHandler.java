package com.eechain.sso.client.handler.logout;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Create by haloo on 2019-04-09
 */
public class HttpStatusReturingLogoutSuccessHandler implements LogoutSuccessHandler {

  private final HttpStatus httpStatus;

  public HttpStatusReturingLogoutSuccessHandler(@NotNull HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }


  public HttpStatusReturingLogoutSuccessHandler() {
    httpStatus = HttpStatus.OK;
  }


  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Object authentication) throws Exception {
    response.setStatus(httpStatus.value());
    response.getWriter().flush();
  }
}
