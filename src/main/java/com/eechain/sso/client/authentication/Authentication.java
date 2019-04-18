package com.eechain.sso.client.authentication;

import java.io.Serializable;

/**
 *  认证信息接口
 * Create by haloo on 2019-04-18
 */
public interface Authentication extends Serializable {

  Object getPrincipal();

  Object getCredential();

  Boolean isAuthentication();

}
