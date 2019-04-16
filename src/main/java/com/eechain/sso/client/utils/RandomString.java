package com.eechain.sso.client.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Create by haloo on 2019-04-08
 */
public class RandomString {

  private static final char[] DEFAULT_CODEC =
      {
          '0', '1', '2', '3', '4', '5', '6', '7',
          '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
          'g', 'h', 'i', 'g', 'k', 'l', 'm', 'n',
          'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
          'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
          'E', 'F', 'G', 'H', 'I', 'G', 'K', 'L',
          'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
          'U', 'V', 'W', 'X', 'Y', 'Z'
      };


  private int length;

  private Random random = new SecureRandom();


  public RandomString(int length) {
    this.length = length;
  }

  public RandomString() {
    this(16);
  }


  public String generate() {
    byte[] codec = new byte[length];
    random.nextBytes(codec);
    return generatorAuthCode(codec);
  }

  private String generatorAuthCode(byte[] verifyCode) {
    char[] codec = new char[verifyCode.length];
    for (int i = 0; i < codec.length; i++) {
      codec[i] = DEFAULT_CODEC[(verifyCode[i] & 0xFF) % DEFAULT_CODEC.length];
    }
    return new String(codec);
  }

  public void setLength(int length) {
    this.length = length;
  }

  public void setRandom(Random random) {
    this.random = random;
  }
}
