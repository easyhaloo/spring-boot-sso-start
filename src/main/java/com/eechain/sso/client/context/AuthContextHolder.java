package com.eechain.sso.client.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;

import static com.eechain.sso.client.context.ContextStrategy.CUSTOMER;
import static com.eechain.sso.client.context.ContextStrategy.THREAD_LOCAL;

/**
 * Create by haloo on 2019-04-09
 */
@Slf4j
public final class AuthContextHolder {
  private static ContextHolderStrategy contextHolderStrategy;


  private static ContextStrategy strategy;

  static {
    init();
  }


  private static void init() {
    if (strategy == null) {
      strategy = THREAD_LOCAL;
    }
    switch (strategy) {
      case THREAD_LOCAL:
        contextHolderStrategy = new ThreadLocalContextHolderStrategy();
        break;
      case INHERITABLE_THREAD_LOCAL:
        contextHolderStrategy = new InheritableThreadLocalContextHolderStrategy();
        break;
      case CUSTOMER:
        try {
          Class<?> clazz = Class.forName(CUSTOMER.getStrategyName());
          if (isInterface(clazz, ContextHolderStrategy.class)) {
            Constructor<?> constructor = clazz.getConstructor();
            contextHolderStrategy = (ContextHolderStrategy) constructor.newInstance();
          }
          throw new RuntimeException("the subclass is not implements : " +
              CUSTOMER.getStrategyName() + " ContextHolderStrategy");
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


  private static boolean isInterface(Class subClass, Class superClass) {

    if (subClass == null || superClass == null) {
      return false;
    }

    if (!superClass.isAssignableFrom(subClass)) {
      return false;
    }

    Class[] interfaces = subClass.getInterfaces();

    if (interfaces == null || interfaces.length == 0) {
      return false;
    }

    Class inter = interfaces[0];

    return inter == superClass;
  }


}
