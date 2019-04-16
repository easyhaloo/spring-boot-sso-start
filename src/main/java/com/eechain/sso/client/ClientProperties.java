package com.eechain.sso.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Create by haloo on 2019-04-08
 */
@ConfigurationProperties(prefix = "sso.client")
@Component
@Getter
@Setter
public class ClientProperties {

  @NotNull
  private String apiKey;
  @NotNull
  private String secureKey;
  @NotNull
  private Long appId;
  // 默认开启单点登陆
  private boolean enabled = true;
  // "web" or "client"
  private String type;

}
