package com.eechain.sso.client.utils;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.Map;

/**
 * Create by haloo on 2019-04-18
 */
public final class OKHttpUtils {

  private  static final Gson GSON = new Gson();

  public static RequestBody parseParams(Map<String, Object> params) {
    String jsonData = GSON.toJson(params);
    return RequestBody.
        create(MediaType.parse("application/json;charset=utf-8"), jsonData);
  }

  public static String parseResponse(Response response) throws IOException {
    return response.body().string();
  }


  public static <T> T convertToObject(Response response, Class<T> clazz) throws IOException {
    if (response.code() != 200) {
      throw new RuntimeException("the response code is not allowed convertToObject, code :  " +
          response.code());
    }

    ResponseBody responseBody = response.body();
    if (responseBody == null) {
      throw new RuntimeException("the response is null  ");
    }

    String contentType = response.header("Content-Type");
    System.out.println(contentType.contains("text/javascript"));
    if (contentType == null || contentType == ""
        || !contentType.contains("application/json")) {
      throw new RuntimeException("response Content-Type unsupported ,Content-Type :  " +
          contentType);
    }
    String json = responseBody.string();
    return GSON.fromJson(json, clazz);
  }
}
