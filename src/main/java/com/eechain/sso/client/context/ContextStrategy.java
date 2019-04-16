package com.eechain.sso.client.context;

/**
 * Create by haloo on 2019-04-09
 */
public enum ContextStrategy {

  THREAD_LOCAL("threadLocal"),
  INHERITABLE_THREAD_LOCAL("inheritableThreadLocal"),
  CUSTOMER("customer");
  private String strategyName;

  ContextStrategy(String strategyName) {
    this.strategyName = strategyName;
  }

  public void setStrategyName(String strategyName) {
    this.strategyName = strategyName;
  }

  public String getStrategyName() {
    return strategyName;
  }

  /**
   * 供外部调用自定义实现策略
   *
   * @param customerStrategyClassName 自定义策略类全限定名
   * @return
   */
  public static ContextStrategy customerContextStrategy(String customerStrategyClassName) {
    CUSTOMER.setStrategyName(customerStrategyClassName);
    return CUSTOMER;
  }
}
