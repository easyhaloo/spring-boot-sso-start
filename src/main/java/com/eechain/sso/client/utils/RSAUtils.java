package com.eechain.sso.client.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by haloo on 2019-04-08
 */
public final class RSAUtils {


  public static final String KEY_ALGORITHM = "RSA";

  /**
   * 签名算法
   */
  public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

  private static final String PUBLIC_KEY = "RSAPublicKey";

  private static final String PRIVATE_KEY = "RSAPrivateKey";

  private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

  private static final String MD5_ALGORITHM = "md5";

  // 最大的加密串长度
  private static final int MAX_ENCRYPT_BLOCK = 117;
  // 最大的解密串长度
  private static final int MAX_DECRYPT_BLOCK = 128;

  private RSAUtils() {
  }


  public static byte[] decryptBase64(String key) {
    return Base64.decodeBase64(key);
  }


  public static String encryptBase64(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }


  public static Map<String, Key> initKey() throws Exception {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
    keyPairGenerator.initialize(512);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    Map<String, Key> keyMap = new HashMap<>(2);
    keyMap.put(PUBLIC_KEY, keyPair.getPublic());
    keyMap.put(PRIVATE_KEY, keyPair.getPrivate());
    return keyMap;
  }


  public static String sign(byte[] data, String privateKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, SignatureException {
    PrivateKey pvk = generatorPrivateKey(privateKey);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initSign(pvk);
    signature.update(data);
    return encryptBase64(signature.sign());
  }


  public static boolean verify(byte[] data, String publicKey, String sign)
      throws NoSuchAlgorithmException, InvalidKeySpecException,
      InvalidKeyException, SignatureException {
    PublicKey pubKey = generatorPublicKey(publicKey);
    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
    signature.initVerify(pubKey);
    signature.update(data);
    return signature.verify(decryptBase64(sign));
  }


  public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
    return cryptByPrivateKey(data, key, Cipher.ENCRYPT_MODE);
  }


  public static byte[] cryptByPrivateKey(byte[] data, String key, int mode) throws Exception {
    PrivateKey pvk = generatorPrivateKey(key);
    Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
    cipher.init(mode, pvk);
    return cryptWithBlock(data, cipher, MAX_ENCRYPT_BLOCK);
  }


  public static String getPrivateKey(Map<String, Key> keyMap) {
    Key key = keyMap.get(PRIVATE_KEY);
    return encryptBase64(key.getEncoded());
  }


  public static String getPublickKey(Map<String, Key> keyMap) {
    Key key = keyMap.get(PUBLIC_KEY);
    return encryptBase64(key.getEncoded());
  }


  public static String generatorHMAC(String data, String key)
      throws NoSuchAlgorithmException, InvalidKeyException {
    byte[] keyBytes = key.getBytes();
    SecretKey secretKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
    Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
    mac.init(secretKey);
    byte[] bytes = mac.doFinal(data.getBytes());
    return encryptBase64(bytes);
  }


  private static byte[] cryptWithBlock(byte[] data, Cipher cipher, int blockSize)
      throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int i = 0, offset = 0, inLen = data.length;
    byte[] cache;

    while (inLen - offset > 0) {
      if (inLen - offset > blockSize) {
        cache = cipher.doFinal(data, offset, blockSize);
      } else {
        cache = cipher.doFinal(data, offset, inLen - offset);
      }
      baos.write(cache, 0, cache.length);
      i++;
      offset = i * blockSize;
    }
    byte[] bytes = baos.toByteArray();
    baos.close();
    return bytes;
  }


  /**
   * 生成公钥
   *
   * @param key
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  private static PublicKey generatorPublicKey(String key)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = decryptBase64(key);
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
    return publicKey;
  }


  /**
   * 生成私钥
   *
   * @param key
   * @return
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  private static PrivateKey generatorPrivateKey(String key)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] keyBytes = decryptBase64(key);
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    PrivateKey pvk = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    return pvk;
  }


}
