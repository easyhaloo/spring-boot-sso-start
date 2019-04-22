package com.eechain.sso.client.handler.auth;

import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.handler.AuthSuccessHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Create by haloo on 2019-04-22
 */
@ToString
public class HttpStatusReturingAuthSuccessHandler implements AuthSuccessHandler {

  private int code;
  private String message;

  public HttpStatusReturingAuthSuccessHandler() {
    this.code = HttpStatus.OK.value();
    this.message = "认证成功";
  }

  public HttpStatusReturingAuthSuccessHandler(int code) {
    this.code = code;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response, Authentication authentication) 
      throws IOException, ServletException {

    response.setContentType("application/json;charset=utf-8");
    PrintWriter writer = response.getWriter();

    Gson gson = new Gson();
    JsonObject jsonObject = new JsonObject();
    String g = gson.toJson(this);
    jsonObject.addProperty("status", g);
    jsonObject.addProperty("access_token", (String) authentication.getCredential());
    writer.println(jsonObject);
    writer.flush();
  }
}
