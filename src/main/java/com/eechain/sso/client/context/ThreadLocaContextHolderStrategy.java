package com.eechain.sso.client.context;

import org.springframework.util.Assert;

/**
 * Create by haloo on 2019-04-09
 */
public final class ThreadLocaContextHolderStrategy implements ContextHolderStrategy {

  private static final ThreadLocal<AuthContext> CONTEXT_HOLDER = new ThreadLocal<>();

  @Override
  public void clearContext() {
    CONTEXT_HOLDER.remove();
  }

  @Override
  public AuthContext getContext() {
    AuthContext authContext = CONTEXT_HOLDER.get();

    if (authContext == null) {
      authContext = createEmptyContext();
      CONTEXT_HOLDER.set(authContext);
    }
    return authContext;
  }

  @Override
  public void setContext(AuthContext authContext) {
    Assert.notNull(authContext, "authContext not be allowed null");
    CONTEXT_HOLDER.set(authContext);
  }

  @Override
  public AuthContext createEmptyContext() {
    return new DefaultAuthContext();
  }
}
