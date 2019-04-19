package com.eechain.sso.client.manager;

import com.eechain.sso.client.authentication.AuthInfo;
import com.eechain.sso.client.authentication.AuthToken;
import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.utils.OKHttpUtils;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by haloo on 2019-04-18
 */
@ConditionClientAuthManager
public class ClientAuthManager extends AbstractAuthManager {

  private static final String HEADER_X_GL_SDK_ACCESS = "X-GL-SDK-ACCESS";
  private static final String HEADER_X_GL_REQUEST_ID = "X-GL-REQUEST-ID";
  private static final String HEADER_X_GL_API_KEY = "X-GL-API-KEY";
  private static final String HEADER_X_GL_DIGEST = "X-GL-DIGEST";
  private static final Integer MAX_RETRY_COUNT = 3;

  @Autowired
  private OkRestTemplate okRestTemplate;


  @Autowired
  private Generator generator;

  @Override
  public Authentication onAuthenticated(AuthToken authToken)
      throws AuthenticationException {
    String serviceUrl = generator.serviceUrl();
    String targetUrl = serviceUrl + AUTH_URL;
    // 当response无返回时，重试，最大重试次数由 MAX_RETRY_COUNT决定
    Response response = null;
    int retry = 0;
    try {
      while (response == null && retry++ < MAX_RETRY_COUNT) {
        response = okRestTemplate.syncPostWithHeaders(targetUrl, authToken, fillHeaders());
      }
      Authentication authInfo = OKHttpUtils.convertToObject(response, AuthInfo.class);
      return authInfo;
    } catch (IOException e) {
      throw new AuthenticationException("Authentication is Failure." +
          "the response code :" + response.code() + " ", e);
    }
  }


  private Map<String, String> fillHeaders() {
    Map<String, String> map = new HashMap(4);
    map.put(HEADER_X_GL_API_KEY, generator.apiKey());
    map.put(HEADER_X_GL_REQUEST_ID, generator.requestId());
    map.put(HEADER_X_GL_SDK_ACCESS, generator.sdkAccess());
    map.put(HEADER_X_GL_DIGEST, generator.sign());
    return map;
  }

}
