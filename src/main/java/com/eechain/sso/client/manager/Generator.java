package com.eechain.sso.client.manager;

import com.eechain.sso.client.ClientProperties;
import com.eechain.sso.client.utils.RSAUtils;
import com.eechain.sso.client.utils.RandomString;
import org.springframework.context.annotation.Import;


/**
 * Create by haloo on 2019-04-18
 */
@Import(ClientProperties.class)
public class Generator {


  private static final RandomString RANDOM_STRING = new RandomString();
  private static final String SIGN_DATA = "EEC:SSO";

  private final ClientProperties properties;

  public Generator(ClientProperties properties) {
    this.properties = properties;
  }

  public String requestId() {
    return RANDOM_STRING.generate();
  }


  public String origin() {
    return RANDOM_STRING.generate();
  }

  public String sdkAccess() {
    String origin = RANDOM_STRING.generate();
    return sdkAccess(origin);
  }


  public String sdkAccess(String origin) {
    String secureKey = properties.getSecureKey();
    try {
      byte[] bytes = RSAUtils.encryptByPrivateKey(origin.getBytes("utf-8"), secureKey);
      return origin + RSAUtils.encryptBase64(bytes);
    } catch (Exception e) {
      return null;
    }
  }

  public String sign() {
    return sign(SIGN_DATA);
  }

  public String sign(String origin) {
    String apiKey = properties.getApiKey();
    try {
      return RSAUtils.generatorHMAC(origin, apiKey);
    } catch (Exception e) {
      return null;
    }
  }

  public String apiKey() {
    return properties.getApiKey();
  }

  public String secureKey() {
    return properties.getSecureKey();
  }

  public String serviceUrl() {
    return properties.getServiceUrl();
  }
}
