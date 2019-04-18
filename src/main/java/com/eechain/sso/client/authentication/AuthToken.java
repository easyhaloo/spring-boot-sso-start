package com.eechain.sso.client.authentication;

import java.io.Serializable;

/**
 * Create by haloo on 2019-04-18
 */
public class AuthToken implements Serializable {

  private static final long serialVersionUID = -299367384270741980L;
  private final Object principal;

  private final Object credential;

  private boolean authentication;


  public AuthToken(Object principal, Object credential) {
    this.principal = principal;
    this.credential = credential;
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
}
