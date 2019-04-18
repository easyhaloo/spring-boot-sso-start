package com.eechain.sso.client.handler.auth;

import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.handler.AuthFailureHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 认证失败，直接返回状态码
 * Create by haloo on 2019-04-18
 */
@Slf4j
@Setter
@Getter
public class HttpStatusReturingAuthFailureHandler implements AuthFailureHandler {

  private int code;

  private String message;

  public HttpStatusReturingAuthFailureHandler() {
    this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException ex) throws Exception {
    log.debug("user authentication is failure , response code : {} ", response.getStatus(), ex);
    this.code = response.getStatus();
    this.message = ex.getMessage();
    response.setContentType("application/json;charset=utf-8");
    PrintWriter writer = response.getWriter();
    writer.println(this);
    writer.flush();
  }

}
