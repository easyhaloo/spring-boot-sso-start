package com.eechain.sso.client.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;

import static com.eechain.sso.client.context.ContextStrategy.CUSTOMER;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public class AuthContextHolder {
  private static ContextHolderStrategy contextHolderStrategy;


  private static ContextStrategy strategy;

  static {
    init();
  }


  private static void init() {
    contextHolderStrategy = new ThreadLocaContextHolderStrategy();
    switch (strategy) {
      case THREAD_LOCAL:
        contextHolderStrategy = new ThreadLocaContextHolderStrategy();
        break;
      case INHERITABLE_THREAD_LOCAL:
        contextHolderStrategy = new InheritableThreadLocalContextHolderStrategy();
        break;
      case CUSTOMER:
        try {
          Class<?> clazz = Class.forName(CUSTOMER.getStrategyName());
          Constructor<?> constructor = clazz.getConstructor();
          contextHolderStrategy = (ContextHolderStrategy) constructor.newInstance();
          break;
        } catch (Exception e) {
          ReflectionUtils.handleReflectionException(e);
        }
    }
    log.debug("using  {} as context strategy ", strategy);
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
