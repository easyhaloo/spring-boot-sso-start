package com.eechain.springbootssostarter.utils;

import com.eechain.sso.client.utils.RSAUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Create by haloo on 2019-04-23
 */
public class RSAUtilsTests {

  @Test
  public void testRsa() throws Exception {

    Long time = System.currentTimeMillis();
    byte[] bytes = RSAUtils.encryptByPrivateKey(String.valueOf(time).getBytes(), "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJTcRDpQs+yi4qtoPsTdKgwGLj9TdWLzpNXTg7TrjR9aP+300Gg3okWcQ07uFwyrQY4A4//VAgr8EPaVxIswZ3nXjhtBs2nvDLSx+//dZeGlB5yelvNyi78Q2O8qUeIpEy5EPEirfHJU3u+6wss+LU5Y+vPk9pfCQz2gyxz480SxAgMBAAECgYBHYOsyTzpHML+h00Wt3iLoSxpn8Rvri8qxYU/ZM82iiAL5lhdMsciUul4n/dB9mazAa7roFHp7S+zSYHQzDc2/iYM+Suo4HQzpENE/rra0xP8aTZ+yHIU0QxFdQBfh3tJGE4yQY/t2kV6oLcq6LcHXNDF1tNM91D8LNx/EMQ+YQQJBANmnsz+Y/5dvj9B+XOf1AC4MpG90e8QXJvMQX3TDIfIh/txqIOhgmvLiBqYfop+kPKdBZsIs7WFHhvH208Ouu1kCQQCvFejPMTmbv/5LK9OXy43cYzVGq2b0hy2I9+nm9t65qGPUNiDRJWrAjJ3FlevJS62l9L5lDQO1REkzzYFvkKEZAkEAuAkagS3m/67vFUWjXOZGLqm16B+//r/syR0Q+XN9InUoQErkZFg67B+9q32YTg/a0Tl1n73JQbXvHmysBguakQJANBpyCM6eVTJCi75EHqUt10sSvLAfWF+t9lfInLoUt+1bn/hntbXL2fc+sGEYJPRHfd6illHUK6phmm/qt/ezaQJADHh9N43jrbDW5nrommf9l+K4ps1DjOudS8a834a0kRJ8bfseARLnEprA5vAErtAvb8+rwpPWIUtaPPqvEsmo5A==");
    System.out.println(RSAUtils.encryptBase64(bytes));
    byte[] bytes1 = RSAUtils.decryptByPublicKey(
        RSAUtils.decryptBase64(
            RSAUtils.encryptBase64(bytes)
        )

        , "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCU3EQ6ULPsouKraD7E3SoMBi4/U3Vi86TV04O0640fWj/t9NBoN6JFnENO7hcMq0GOAOP/1QIK/BD2lcSLMGd5144bQbNp7wy0sfv/3WXhpQecnpbzcou/ENjvKlHiKRMuRDxIq3xyVN7vusLLPi1OWPrz5PaXwkM9oMsc+PNEsQIDAQAB");
    System.out.println(new String(bytes1));
    Assert.assertArrayEquals(String.valueOf(time).getBytes(),bytes1);
  }
}
