package com.eechain.sso.client.handler.logout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Cookie处理Handler
 * Create by haloo on 2019-04-09
 */
@Slf4j
public class CookieLogoutHandler implements LogoutHandler {

  private final List<String> cookiesToClear;

  public CookieLogoutHandler(String... cookies) {
    Assert.notNull(cookies, "cookies is not null");
    cookiesToClear = Arrays.asList(cookies);
  }

  @Override
  public void onLogout(HttpServletRequest request, HttpServletResponse response, Object authentication) {
    for (String cookieName : cookiesToClear) {
      Cookie cookie = new Cookie(cookieName, null);
      String cookiePath = request.getContextPath() + "/";
      cookie.setPath(cookiePath);
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
  }
}
