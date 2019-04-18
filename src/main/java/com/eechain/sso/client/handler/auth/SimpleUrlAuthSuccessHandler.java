package com.eechain.sso.client.handler.auth;

import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.handler.AbstractAuthTargetUrlRequestHandler;
import com.eechain.sso.client.handler.AuthSuccessHandler;
import com.eechain.sso.client.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by haloo on 2019-04-18
 */
@Slf4j
public class SimpleUrlAuthSuccessHandler
    extends AbstractAuthTargetUrlRequestHandler implements AuthSuccessHandler {

  private String targetUrl;

  public SimpleUrlAuthSuccessHandler() {
  }

  public SimpleUrlAuthSuccessHandler(String targetUrl) {
    this.targetUrl = targetUrl;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    redirectStrategy.redirect(request, response, targetUrl);
  }


  public void setTargetUrl(String targetUrl) {
    boolean validRedirectUrl = UrlUtils.isValidRedirectUrl(targetUrl);
    Assert.isTrue(validRedirectUrl,
        "targetUrl : { " + targetUrl + "}  is not valid redirect url.");
    this.targetUrl = targetUrl;
  }
}
