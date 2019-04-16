package com.eechain.sso.client.handler.logout;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public class RequestHeaderClearingLogoutHandller implements LogoutHandler {

  private static final Map<String, String> HEAD_MAP = new HashMap<String, String>() {
    {
      put("X-GL-ACCESS", "X-GL-ACCESS");
      put("X-GL-SDK-ACCESS", "X-GL-SDK-ACCESS");
      put("X-GL-REQUEST-ID", "X-GL-REQUEST-ID");
      put("X-GL-DIGEST", "X-GL-DIGEST");
    }
  };

  @Override
  public void onLogout(HttpServletRequest request,
                       HttpServletResponse response, Object authentication) {
    HEAD_MAP.forEach((key, value) -> {

    });
  }
}
