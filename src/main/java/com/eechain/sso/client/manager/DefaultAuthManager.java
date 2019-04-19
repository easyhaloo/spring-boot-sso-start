package com.eechain.sso.client.manager;

import com.eechain.sso.client.authentication.AuthToken;
import com.eechain.sso.client.authentication.Authentication;
import com.eechain.sso.client.exception.AuthenticationException;
import com.eechain.sso.client.handler.AuthManager;

/**
 * Create by haloo on 2019-04-19
 */
public class DefaultAuthManager implements AuthManager {
  @Override
  public Authentication onAuthenticated(AuthToken authToken)
      throws AuthenticationException {
    return null;
  }
}
