package com.eechain.sso.client.filter;

import com.eechain.sso.client.authentication.AuthToken;
import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.context.AuthContextHolder;
import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.handler.AuthFailureHandler;
import com.eechain.sso.client.handler.AuthManager;
import com.eechain.sso.client.handler.AuthSuccessHandler;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 前置认证处理器
 * Create by haloo on 2019-04-09
 */
@Slf4j
public abstract class AbstractPreAuthProcessFilter implements Filter {

  private AuthSuccessHandler authSuccessHandler;
  private AuthFailureHandler authFailureHandler;

  private boolean checkForPrincipalChanges;
  private boolean invalidateSessionOnPrincipalChange = true;

  private AuthManager authManager;

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain)
      throws IOException, ServletException {

    if (requiresAuthentication((HttpServletRequest) req)) {
      log.debug("authentication is changed , now need reAuthenticated");
      doAuthenticate((HttpServletRequest) req, (HttpServletResponse) resp);
    }
    filterChain.doFilter(req, resp);
  }


  private void doAuthenticate(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    Object principal = getPrincipal(request);
    Object credentials = getCredentials(request);

    if (principal == null) {
      log.warn("principal is null,authentication is reject.");
      throw new AuthenticationException("用户名不存在");
    }

    if (credentials == null) {
      log.warn("credentials is null,is invalid  ticket info.");
      throw new AuthenticationException("验证失败");
    }

    AuthToken authToken = new AuthToken(principal, credentials);

    try {
      Authentication authentication = authManager.onAuthenticated(authToken);
      successfulAuthentication(request, response, authentication);
    } catch (AuthenticationException e) {
      failureAuthentication(request, response, e);
      throw e;
    }
  }


  private boolean requiresAuthentication(HttpServletRequest request) {
    Authentication currentUser = AuthContextHolder.getContext().getAuthentication();
    if (currentUser == null) {
      log.warn("the authContextHolder is not hold the current user");
      return true;
    }

    if (currentUser.getCredential() == null) {
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


  protected void successfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response,
                                          Authentication currentAuthentication)
      throws IOException, ServletException {
    AuthContextHolder.getContext().setAuthentication(currentAuthentication);

    if (authSuccessHandler != null) {
      authSuccessHandler.onAuthenticationSuccess(request, response, currentAuthentication);
    }
  }

  protected void failureAuthentication(HttpServletRequest request,
                                       HttpServletResponse response, AuthenticationException ex)
      throws IOException, ServletException {
    AuthContextHolder.clearContext();
    if (authFailureHandler != null) {
      authFailureHandler.onAuthenticationFailure(request, response, ex);
    }
  }

  protected boolean principalChanged(HttpServletRequest request, Authentication authentication) {
    Object principal = getPrincipal(request);
    if ((principal instanceof String) && authentication.getName().equals(principal)) {
      return false;
    }

    if (principal != null && principal.equals(authentication.getPrincipal())) {
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

  public void setAuthManager(AuthManager authManager) {
    this.authManager = authManager;
  }

  public AuthManager getAuthManager() {
    return authManager;
  }

  protected abstract Object getPrincipal(HttpServletRequest request);


  protected abstract Object getCredentials(HttpServletRequest request);
}
