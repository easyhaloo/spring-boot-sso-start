package com.eechain.sso.client;

import com.eechain.sso.client.filter.LoginAuthProcessFilter;
import com.eechain.sso.client.filter.LogoutProccessFileter;
import com.eechain.sso.client.filter.TicketAuthProccessFileter;
import com.eechain.sso.client.handler.AuthManager;
import com.eechain.sso.client.handler.LogoutSuccessHandler;
import com.eechain.sso.client.handler.logout.CompositeLogoutHandler;
import com.eechain.sso.client.handler.logout.ContextLogoutHandler;
import com.eechain.sso.client.handler.logout.HttpStatusReturingLogoutSuccessHandler;
import com.eechain.sso.client.manager.ClientAuthManager;
import com.eechain.sso.client.manager.ConditionClientAuthManager;
import com.eechain.sso.client.manager.Generator;
import com.eechain.sso.client.manager.OkRestTemplate;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

/**
 * Create by haloo on 2019-04-08
 */
@Configuration
@Import(ClientProperties.class)
public class ClientAutoConfigure {


  @Bean
  public Generator generator(ClientProperties clientProperties) {
    return new Generator(clientProperties);
  }


  @Bean
  @ConditionClientAuthManager
  public AuthManager clientAuthManager(Generator generator,
                                       OkRestTemplate okRestTemplate) {
    return new ClientAuthManager(okRestTemplate, generator);
  }


  @Bean
  public FilterRegistrationBean ticketFilter(AuthManager authManager) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    TicketAuthProccessFileter ticketAuthProccessFileter =
        new TicketAuthProccessFileter(authManager);
    registrationBean.setFilter(ticketAuthProccessFileter);
    registrationBean.addUrlPatterns("/**");
    registrationBean.setName("ticket");
    return registrationBean;
  }

  /**
   * 配置单点退出过滤器
   *
   * @return
   */
  @Bean
  public FilterRegistrationBean logoutFilter() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    ContextLogoutHandler logoutHandler = new ContextLogoutHandler();
    CompositeLogoutHandler compositeLogoutHandler =
        new CompositeLogoutHandler(Arrays.asList(logoutHandler));
    LogoutSuccessHandler logoutSuccessHandler =
        new HttpStatusReturingLogoutSuccessHandler();
    LogoutProccessFileter logoutProccessFileter =
        new LogoutProccessFileter(compositeLogoutHandler, logoutSuccessHandler);
    registrationBean.setFilter(logoutProccessFileter);
    registrationBean.addUrlPatterns("/logout/*");
    registrationBean.setOrder(Integer.MIN_VALUE);
    registrationBean.setName("logout");
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean loginFilter(AuthManager authManager) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    LoginAuthProcessFilter loginAuthProcessFilter =
        new LoginAuthProcessFilter();
    loginAuthProcessFilter.setAuthManager(authManager);
    registrationBean.setFilter(loginAuthProcessFilter);
    registrationBean.addUrlPatterns("/login/*");
    registrationBean.setOrder(Integer.MAX_VALUE);
    registrationBean.setName("login");
    return registrationBean;
  }

}
