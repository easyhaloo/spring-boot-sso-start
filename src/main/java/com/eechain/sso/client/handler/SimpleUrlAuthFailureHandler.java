package com.eechain.sso.client.handler;

import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public class SimpleUrlAuthFailureHandler extends AbstractAuthTaretUrlRequestHandler implements AuthFailureHandler {

  private String defaultFailureUrl;
  private boolean allowSessionCreation = true;
  private boolean forwardToDestination = false;

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  public SimpleUrlAuthFailureHandler() {

  }

  public SimpleUrlAuthFailureHandler(String defaultFailureUrl) {
    this.defaultFailureUrl = defaultFailureUrl;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws Exception {

    if (defaultFailureUrl == null) {
      response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    } else {
      saveException(request, ex);
      if (forwardToDestination) {
        request.getRequestDispatcher(defaultFailureUrl)
            .forward(request, response);
      } else {

      }
    }
  }

  private void saveException(HttpServletRequest request, AuthenticationException ex) {
    if (forwardToDestination) {
      request.setAttribute("exception", ex);
    } else {
      HttpSession session = request.getSession(false);
      if (session != null || allowSessionCreation) {
        request.getSession().setAttribute("exception", ex);
      }
    }
  }


  public void setDefaultFailureUrl(String defaultFailureUrl) {
    boolean validRedirectUrl = UrlUtils.isValidRedirectUrl(defaultFailureUrl);
    Assert.isTrue(validRedirectUrl, "defaultFailureUrl : { " + defaultFailureUrl + "}  is not valid redirect url.");
    this.defaultFailureUrl = defaultFailureUrl;
  }

  public boolean isForwardToDestination() {
    return forwardToDestination;
  }

  public void setForwardToDestination(boolean forwardToDestination) {
    this.forwardToDestination = forwardToDestination;
  }

  public boolean isAllowSessionCreation() {
    return allowSessionCreation;
  }

  public void setAllowSessionCreation(boolean allowSessionCreation) {
    this.allowSessionCreation = allowSessionCreation;
  }
}
