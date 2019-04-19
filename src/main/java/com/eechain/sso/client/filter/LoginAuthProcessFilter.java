package com.eechain.sso.client.filter;


import com.eechain.sso.client.utils.UrlUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Create by haloo on 2019-04-19
 */
public class LoginAuthProcessFilter extends AbstractPreAuthProcessFilter {

  private static final String USER = "username";
  private static final String PASSWORD = "password";

  @Override
  protected Object getPrincipal(HttpServletRequest request) {
    return extractRequestBodyWithParam(request, USER);
  }

  @Override
  protected Object getCredentials(HttpServletRequest request) {
    return extractRequestBodyWithParam(request, PASSWORD);
  }

  private static String extractRequestBodyWithParam(HttpServletRequest request, String paramName) {
    String method = request.getMethod();

    String header = request.getHeader(paramName);
    if (!StringUtils.isEmpty(header)) {
      return header;
    }

    if ("get".equalsIgnoreCase(method)) {
      String queryString = request.getQueryString();
      Map<String, String> map = UrlUtils.parseQueryString(queryString);
      return map.get(paramName);
    } else if ("post".equalsIgnoreCase(method)) {
      return request.getParameter(paramName);
    }
    return null;
  }
}
