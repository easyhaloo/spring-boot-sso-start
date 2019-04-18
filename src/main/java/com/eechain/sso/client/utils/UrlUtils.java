package com.eechain.sso.client.utils;

import java.util.regex.Pattern;

/**
 * Create by haloo on 2019-04-09
 */
public final class UrlUtils {

  static final Pattern ABSOLUTE_PATTERN = Pattern.compile("\\A[a-z0-9.+-]+://.*",
      Pattern.CASE_INSENSITIVE);

  private UrlUtils() {
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
