package com.eechain.sso.client;

import com.eechain.sso.client.filter.LoginAuthProcessFilter;
import com.eechain.sso.client.filter.LogoutProcessFilter;
import com.eechain.sso.client.filter.TicketAuthProcessFilter;
import com.eechain.sso.client.handler.AuthManager;
import com.eechain.sso.client.handler.AuthSuccessHandler;
import com.eechain.sso.client.handler.LogoutSuccessHandler;
import com.eechain.sso.client.handler.auth.HttpStatusReturingAuthSuccessHandler;
import com.eechain.sso.client.handler.auth.SaveUserInfoAuthSuccessHandler;
import com.eechain.sso.client.handler.logout.CompositeLogoutHandler;
import com.eechain.sso.client.handler.logout.ContextLogoutHandler;
import com.eechain.sso.client.handler.logout.HttpStatusReturingLogoutSuccessHandler;
import com.eechain.sso.client.manager.ConditionClientAuthManager;
import com.eechain.sso.client.manager.Generator;
import com.eechain.sso.client.manager.OkRestTemplate;
import com.eechain.sso.client.manager.ClientAuthManager;
import com.eechain.sso.client.manager.DefaultAuthManager;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
  @ConditionalOnClass(OkHttpClient.class)
  public OkRestTemplate okRestTemplate() {
    return new OkRestTemplate();
  }

  @Bean
  @ConditionClientAuthManager
  public AuthManager clientAuthManager(Generator generator,
                                       OkRestTemplate okRestTemplate) {
    System.out.println("client AuthManager init");
    return new ClientAuthManager(okRestTemplate, generator);
  }

  @Bean
  @ConditionalOnMissingBean
  public AuthManager defaultAuthManager() {
    return new DefaultAuthManager();
  }


  @Bean
  public FilterRegistrationBean ticketFilter(AuthManager authManager) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    TicketAuthProcessFilter ticketAuthProcessFilter =
        new TicketAuthProcessFilter(authManager);
    registrationBean.setFilter(ticketAuthProcessFilter);
    registrationBean.addUrlPatterns("/*");
    registrationBean.setName("ticket");
    registrationBean.setOrder(Integer.MAX_VALUE >> 2);
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
    LogoutProcessFilter logoutProcessFilter =
        new LogoutProcessFilter(compositeLogoutHandler, logoutSuccessHandler);
    registrationBean.setFilter(logoutProcessFilter);
    registrationBean.addUrlPatterns("/logout/*");
    registrationBean.setOrder(Integer.MAX_VALUE);
    registrationBean.setName("logout");
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean loginFilter(AuthManager authManager) {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    LoginAuthProcessFilter loginAuthProcessFilter =
        new LoginAuthProcessFilter();

    AuthSuccessHandler returingAuthSuccessHandler
        = new HttpStatusReturingAuthSuccessHandler();
    AuthSuccessHandler authSuccessHandler =
        new SaveUserInfoAuthSuccessHandler(returingAuthSuccessHandler);
    loginAuthProcessFilter.setAuthSuccessHandler(authSuccessHandler);
    loginAuthProcessFilter.setAuthManager(authManager);
    registrationBean.setFilter(loginAuthProcessFilter);
    registrationBean.addUrlPatterns("/login/*");
    registrationBean.setOrder(Integer.MAX_VALUE);
    registrationBean.setName("login");
    return registrationBean;
  }

}
