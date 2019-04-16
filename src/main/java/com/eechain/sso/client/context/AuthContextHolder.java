package com.eechain.sso.client.context;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;

import static com.eechain.sso.client.context.ContextStrategy.CUSTOMER;

/**
 * Create by haloo on 2019-04-09
 */
public class AuthContextHolder {
  private static ContextHolderStrategy contextHolderStrategy;

//  private static int initializeCount = 0;

  private static ContextStrategy strategy;

  static {
    init();
  }


  private static void init() {
    switch (strategy) {
      case THREAD_LOCAL:
        contextHolderStrategy = new ThreadLocaContextHolderStrategy();
      case INHERITABLE_THREAD_LOCAL:
        contextHolderStrategy = new InheritableThreadLocalContextHolderStrategy();
      case CUSTOMER:
        try {
          Class<?> clazz = Class.forName(CUSTOMER.getStrategyName());
          Constructor<?> constructor = clazz.getConstructor();
          contextHolderStrategy = (ContextHolderStrategy) constructor.newInstance();
        } catch (Exception e) {
          ReflectionUtils.handleReflectionException(e);
        }
      default:
        contextHolderStrategy = new ThreadLocaContextHolderStrategy();
    }
  }


  public static void setStrategyName(ContextStrategy strategy) {
    AuthContextHolder.strategy = strategy;
    init();
  }


  public static void clearContext() {
    contextHolderStrategy.clearContext();
  }


  public static AuthContext getContext() {
    return contextHolderStrategy.getContext();
  }

  public static void setContext(AuthContext authContext) {
    contextHolderStrategy.setContext(authContext);
  }

}
