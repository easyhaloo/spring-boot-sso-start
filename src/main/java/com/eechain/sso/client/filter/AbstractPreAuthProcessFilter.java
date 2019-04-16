package com.eechain.sso.client.filter;

import com.eechain.sso.client.context.AuthContextHolder;
import com.eechain.sso.client.handler.AuthFailureHandler;
import com.eechain.sso.client.handler.AuthSuccessHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public abstract class AbstractPreAuthProcessFilter implements Filter {


  private AuthSuccessHandler authSuccessHandler;
  private AuthFailureHandler authFailureHandler;

  private boolean checkForPrincipalChanges;
  private boolean invalidateSessionOnPrincipalChange = true;

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {

    if (requiresAuthentication((HttpServletRequest) req)) {
      doAuthenticate((HttpServletRequest) req, (HttpServletResponse) resp);
    }
    filterChain.doFilter(req, resp);
  }


  private void doAuthenticate(HttpServletRequest request, HttpServletResponse response) {
    Object authentication;
    Object principal = getPrincipal(request);
    Object credentials = getCredentials(request);

    if (principal == null) {
      return;
    }


  }


  private boolean requiresAuthentication(HttpServletRequest request) {
    Object currentUser = AuthContextHolder.getContext().getAuthentication();
    if (currentUser == null) {
      return true;
    }

    if (!checkForPrincipalChanges) {
      return false;
    }

    if (!principalChanged(request, currentUser)) {
      return false;
    }

    if (invalidateSessionOnPrincipalChange) {
      AuthContextHolder.clearContext();
      HttpSession session = request.getSession(false);
      if (session != null) {
        log.debug("Invalidating existing session");
        session.invalidate();
      }
    }
    return true;
  }


  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          Object currentAuthentication) throws Exception {
    AuthContextHolder.getContext().setAuthentication(currentAuthentication);

    if (authSuccessHandler != null) {
      authSuccessHandler.onAuthenticationSuccess(request, response, currentAuthentication);
    }
  }

  protected void failureAuthentication(HttpServletRequest request, HttpServletResponse response,
                                       Object currentAuthentication) throws Exception {
    AuthContextHolder.clearContext();
    if (authFailureHandler != null) {
      authFailureHandler.onAuthenticationFailure(request, response, null);
    }
  }

  protected boolean principalChanged(HttpServletRequest request, Object authentication) {
    Object principal = getPrincipal(request);
    if ((principal instanceof String) && authentication.equals(principal)) {
      return false;
    }

    if (principal != null && principal.equals(authentication)) {
      return false;
    }

    return true;
  }


  public void setAuthFailureHandler(AuthFailureHandler authFailureHandler) {
    this.authFailureHandler = authFailureHandler;
  }

  public void setAuthSuccessHandler(AuthSuccessHandler authSuccessHandler) {
    this.authSuccessHandler = authSuccessHandler;
  }

  public void setCheckForPrincipalChanges(boolean checkForPrincipalChanges) {
    this.checkForPrincipalChanges = checkForPrincipalChanges;
  }

  public void setInvalidateSessionOnPrincipalChange(boolean invalidateSessionOnPrincipalChange) {
    this.invalidateSessionOnPrincipalChange = invalidateSessionOnPrincipalChange;
  }

  protected abstract Object getPrincipal(HttpServletRequest request);


  protected abstract Object getCredentials(HttpServletRequest request);
}
