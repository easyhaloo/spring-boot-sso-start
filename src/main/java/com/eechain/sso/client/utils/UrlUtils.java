package com.eechain.sso.client.utils;

import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Create by haloo on 2019-04-09
 */
public final class UrlUtils {

  static final Pattern ABSOLUTE_PATTERN = Pattern.compile("\\A[a-z0-9.+-]+://.*",
      Pattern.CASE_INSENSITIVE);

  private UrlUtils() {
  }


  public static Map<String, String> parseQueryString(String queryString) {
    if (StringUtils.isEmpty(queryString)) {
      return Collections.emptyMap();
    }
    String[] queryItems = queryString.split("&");
    Map<String, String> map = new HashMap<>();
    for (String queryItem : queryItems) {
      int i = queryItem.indexOf("=");
      if (i < 0) {
        continue;
      }
      String[] params = queryItem.split("=");
      if (params.length != 2) {
        continue;
      }
      map.put(params[0], params[1]);
    }
    return map;
  }


  public static boolean isValidRedirectUrl(String url) {
    return url != null && url.startsWith("/") && isAbsoluteUrl(url);
  }

  public static boolean isAbsoluteUrl(String url) {
    if (url == null) {
      return false;
    }
    return ABSOLUTE_PATTERN.matcher(url).matches();
  }
}
