package com.eechain.sso.client.manager;

import com.eechain.sso.client.authentication.AuthInfo;
import com.eechain.sso.client.authentication.AuthToken;
import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.constant.RequestHeader;
import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.utils.OKHttpUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by haloo on 2019-04-18
 */
@Slf4j
public class ClientAuthManager extends AbstractAuthManager {

  private static final String HEADER_X_GL_SDK_ACCESS = "X-GL-SDK-ACCESS";
  private static final String HEADER_X_GL_REQUEST_ID = "X-GL-REQUEST-ID";
  private static final String HEADER_X_GL_API_KEY = "X-GL-API-KEY";
  private static final String HEADER_X_GL_DIGEST = "X-GL-DIGEST";
  private static final String HEADER_X_GL_AGENT = "X-GL-SSO";
  private static final String HEADER_X_GL_TYPE = "X-GL-TYPE";
  private static final String HEADER_X_GL_APP_ID = "X-GL-APP-ID";

  private static final Integer MAX_RETRY_COUNT = 3;


  private final OkRestTemplate okRestTemplate;

  private final Generator generator;

  public ClientAuthManager(OkRestTemplate okRestTemplate, Generator generator) {
    this.okRestTemplate = okRestTemplate;
    this.generator = generator;
    // 重试策略由调用者自己控制
    this.okRestTemplate.setEnabledRetry(false);
  }

  @Override
  public Authentication onAuthenticated(AuthToken authToken)
      throws AuthenticationException {
    String serviceUrl = generator.serviceUrl();
    String loginUrl = serviceUrl + LOGIN_URL;
    // 当response无返回时，重试，最大重试次数由 MAX_RETRY_COUNT决定
    Response response = null;
    int retry = 0;
    try {
      authToken.setType("client");
      authToken.setLoginName((String) authToken.getPrincipal());
      authToken.setPassword((String) authToken.getCredential());
      authToken.setApiKey(generator.apiKey());
      authToken.setAppId(generator.appId());
      while (response == null && retry++ < MAX_RETRY_COUNT) {
        response = okRestTemplate.syncPostWithHeaders(loginUrl, authToken, fillHeaders());
        log.debug("request {}  is not response , accept retry count :{} ", loginUrl, retry);
      }
      AuthInfo authInfo = OKHttpUtils.convertToObject(response, AuthInfo.class);
      if (authInfo.getPrincipal() instanceof String) {
        authInfo.setName((String) authInfo.getPrincipal());
      }
      authInfo.setCredential(authToken.getCredential());
      authInfo.setPrinciple(authToken.getPrincipal());
      return authInfo;
    } catch (IOException e) {
      log.error("logout is error , the code : {} , the cause :{}",
          response.code(), e.getMessage());
      throw new AuthenticationException("Authentication is Failure." +
          "the response code :" + response.code() + " ", e);
    }
  }

  @Override
  public void verifyTick(Authentication authentication) throws AuthenticationException {
    String serviceUrl = generator.serviceUrl();
    String authUrl = serviceUrl + AUTH_URL;
    // 当response无返回时，重试，最大重试次数由 MAX_RETRY_COUNT决定
    Response response = null;
    int retry = 0;
    try {
      while (response == null && retry++ < MAX_RETRY_COUNT) {
        response = okRestTemplate.syncGetWithHeaders(authUrl,
            fillHeaders(RequestHeader.ACCESS,
                (String) authentication.getCredential()));
        log.debug("request {}  is not response , accept retry count :{} ", authUrl, retry);
      }

      if (response.code() != HttpStatus.OK.value()) {
        if (response.code() == HttpStatus.FORBIDDEN.value()) {
          throw new AuthenticationException("没有访问权限，请检验登陆信息是否正常。");
        }
      }
    } catch (Exception e) {
      log.error("verify is error , the code : {} , the cause :{}",
          response.code(), e.getMessage());
      throw new AuthenticationException("Authentication is Failure." +
          "the response code :" + response.code(), e);
    }
  }


  @Override
  public void logout(Authentication authentication) throws AuthenticationException {
    String serviceUrl = generator.serviceUrl();
    String logoutUrl = serviceUrl + LOGOUT_URL;
    Response response = null;
    int retry = 0;

    try {
      while (response == null && retry++ < MAX_RETRY_COUNT) {
        response = okRestTemplate.syncPostWithHeaders(logoutUrl, authentication, fillHeaders());
        log.debug("request {}  is not response , accept retry count :{} ", logoutUrl, retry);
      }
    } catch (IOException e) {
      log.error("logout is error , the code : {} , the cause :{}",
          response.code(), e.getMessage());
      throw new AuthenticationException("Cross SSO Server Logout is failure." +
          "the response code:" + response.code(), e);
    }
  }

  private Map<String, String> fillHeaders(String key, String value) {
    Map<String, String> map = fillHeaders();
    map.put(key, value);
    return map;
  }


  private Map<String, String> fillHeaders() {
    Map<String, String> map = new HashMap(16);
    map.put(HEADER_X_GL_API_KEY, generator.apiKey());
    map.put(HEADER_X_GL_REQUEST_ID, generator.requestId());
    map.put(HEADER_X_GL_SDK_ACCESS, generator.sdkAccess());
    map.put(HEADER_X_GL_DIGEST, generator.sign());
    map.put(HEADER_X_GL_APP_ID,
        String.valueOf(generator.appId()));
    map.put(HEADER_X_GL_TYPE, "client");
    map.put("User-Agent", HEADER_X_GL_AGENT);
    return map;
  }

}
