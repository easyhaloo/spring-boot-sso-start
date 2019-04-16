package com.eechain.sso.client.context;

/**
 * Create by haloo on 2019-04-09
 */
public interface ContextHolderStrategy {

  void clearContext();


  AuthContext getContext();


  void setContext(AuthContext authContext);

  AuthContext createEmptyContext();
}
