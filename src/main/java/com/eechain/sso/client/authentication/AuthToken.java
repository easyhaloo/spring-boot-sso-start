package com.eechain.sso.client.authentication;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Create by haloo on 2019-04-18
 */
@ToString
@Data
public class AuthToken implements Serializable {

  private static final long serialVersionUID = -299367384270741980L;
  private final Object principal;

  private final Object credential;


  private String loginName;
  private String password;
  private Long appId;
  private String apiKey;

  private boolean authentication;

  private String type;

  public AuthToken(Object principal, Object credential) {
    this.principal = principal;
    this.credential = credential;
    setAuthentication(true);
  }

  public Object getPrincipal() {
    return principal;
  }

  public Object getCredential() {
    return credential;
  }

  public void setAuthentication(boolean authentication) {
    this.authentication = authentication;
  }

  public boolean isAuthentication() {
    return authentication;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
