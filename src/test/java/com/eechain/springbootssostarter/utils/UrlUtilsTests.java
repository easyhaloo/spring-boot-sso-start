package com.eechain.springbootssostarter.utils;

import com.eechain.sso.client.utils.UrlUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;


/**
 * Create by haloo on 2019-04-19
 */
public class UrlUtilsTests {


  @Test
  public void testParseQueryString() {

    String queryString = "xx=xc&asd=xxas&xasx='xx'&c=&g=123";

    Map<String, String> map = UrlUtils.parseQueryString(queryString);

    Assert.assertTrue(!map.isEmpty() && map.size() == 4);

  }
}
