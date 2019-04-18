package com.eechain.sso.client.context;

import com.eechain.sso.client.authentication.Authentication;

import java.util.Objects;

/**
 * Create by haloo on 2019-04-09
 */
public class DefaultAuthContext implements AuthContext {

  private static final long serialVersionUID = -1L;

  private Authentication authentication;


  public DefaultAuthContext() {
  }

  public DefaultAuthContext(Authentication authentication) {
    this.authentication = authentication;
  }


  @Override
  public Authentication getAuthentication() {
    return authentication;
  }

  @Override
  public void setAuthentication(Authentication authentication) {
    this.authentication = authentication;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DefaultAuthContext that = (DefaultAuthContext) o;
    return Objects.equals(authentication, that.authentication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authentication);
  }
}
