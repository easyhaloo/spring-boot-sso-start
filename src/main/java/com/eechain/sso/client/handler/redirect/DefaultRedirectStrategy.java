package com.eechain.sso.client.handler.redirect;

import com.eechain.sso.client.handler.RedirectStrategy;
import com.eechain.sso.client.utils.UrlUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public class DefaultRedirectStrategy implements RedirectStrategy {


  private boolean contextRelative;

  @Override
  public void redirect(HttpServletRequest request, HttpServletResponse response,
                       String targetUrl) throws IOException {
    String redirectUrl = calculateRedirectUrl(request.getContextPath(), targetUrl);
    redirectUrl = response.encodeRedirectURL(redirectUrl);
    response.sendRedirect(redirectUrl);
  }


  private String calculateRedirectUrl(String contextPath, String url) {
    if (!UrlUtils.isAbsoluteUrl(url)) {
      if (isContextRelative()) {
        return url;
      } else {
        return contextPath + url;
      }
    }

    if (!isContextRelative()) {
      return url;
    }

    url = url.substring(url.lastIndexOf("://") + 3); //strip scheme
    url = url.substring(url.indexOf(contextPath) + contextPath.length());
    if (url.length() > 1 && url.charAt(0) == '/') {
      url = url.substring(1);
    }
    return url;
  }


  public void setContextRelative(boolean contextRelative) {
    this.contextRelative = contextRelative;
  }

  public boolean isContextRelative() {
    return contextRelative;
  }
}
