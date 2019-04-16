package com.eechain.sso.client.handler;

import com.eechain.sso.client.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public abstract class AbstractAuthTaretUrlRequestHandler {

  private String targetUrlParameter;

  private String defaultTargetUrl = "/";

  private boolean alwaysUseDefaultTargetUrl = false;
  private boolean useReferer = false;

  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();


  protected void handle(HttpServletRequest request, HttpServletResponse response,
                        Object authentication) throws Exception {
    String targetUrl = determineTargetUrl(request);
    if (response.isCommitted()) {
      log.debug("response  has already been committed. Unable  to redirect to  {}  ", targetUrl);
      return;
    }
    redirectStrategy.redirect(request, response, targetUrl);
  }


  protected String determineTargetUrl(HttpServletRequest request) {
    if (isAlwaysUseDefaultTargetUrl()) {
      return defaultTargetUrl;
    }

    String targetUrl = null;

    if (targetUrlParameter != null) {
      targetUrl = request.getParameter(targetUrlParameter);
      if (StringUtils.hasText(targetUrl)) {
        return targetUrl;
      }
    }

    if (useReferer && !StringUtils.hasLength(targetUrl)) {
      targetUrl = request.getHeader("Referer");
      log.debug("Using Referer as targetUrl : {}", targetUrl);
    }

    if (!StringUtils.hasText(targetUrl)) {
      targetUrl = defaultTargetUrl;
      log.debug("Using defaultTargetUrl : {}", defaultTargetUrl);
    }
    return targetUrl;
  }


  public void setDefaultTargetUrl(String defaultTargetUrl) {
    Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultTargetUrl), "defaultTargetUrl must " +
        "start with '/'  or http(s)");
    this.defaultTargetUrl = defaultTargetUrl;
  }


  public void setTargetUrlParameter(String targetUrlParameter) {
    if (StringUtils.isEmpty(targetUrlParameter)) {
      Assert.hasText(targetUrlParameter, "targetUrlParameter cannot be empty");
    }
    this.targetUrlParameter = targetUrlParameter;
  }

  public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
    this.redirectStrategy = redirectStrategy;
  }

  public RedirectStrategy getRedirectStrategy() {
    return redirectStrategy;
  }

  public boolean isAlwaysUseDefaultTargetUrl() {
    return alwaysUseDefaultTargetUrl;
  }

  public void setUseReferer(boolean useReferer) {
    this.useReferer = useReferer;
  }
}