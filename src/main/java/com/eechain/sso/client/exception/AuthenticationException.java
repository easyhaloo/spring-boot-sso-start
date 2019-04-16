package com.eechain.sso.client.exception;

/**
 * Create by haloo on 2019-04-09
 */
public class AuthenticationException extends RuntimeException {


  public AuthenticationException(String message, Throwable t) {
    super(message, t);
  }

  public AuthenticationException(String message) {
    super(message);
  }
}
