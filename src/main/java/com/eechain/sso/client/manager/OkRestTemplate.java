package com.eechain.sso.client.manager;

import com.eechain.sso.client.utils.OKHttpUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * OK rest模版，用于跟sso-service 通信，或者其他子系统通信的工具
 * Create by haloo on 2019-04-18
 */
@Slf4j
public class OkRestTemplate {

  private OkHttpClient okHttpClient;

  private Request request;

  @Setter
  @Getter
  private int connectionTimeout = 10;
  @Setter
  @Getter
  private int readTimeout = 30;
  @Setter
  @Getter
  private int writeTimeout = 60;
  /**
   * 最大的重试次数
   */
  @Setter
  @Getter
  private int maxRetryCount = 3;
  /**
   * 是否开启重试机制，默认开启
   */
  @Setter
  @Getter
  private boolean enabledRetry = true;


  public OkRestTemplate() {
    this.okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(connectionTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .writeTimeout(writeTimeout, TimeUnit.SECONDS)
        .retryOnConnectionFailure(enabledRetry)
        .build();
  }


  /**
   * 同步调用get方法
   *
   * @param url
   * @return
   * @throws IOException
   */
  public Response syncGet(String url) throws IOException {
    this.request = createNoHeaderGetRequest(url);
    Call call = this.okHttpClient.newCall(request);
    if (isEnabledRetry()) {
      return retryRequest(call, maxRetryCount);
    }
    return retryRequest(call, 1);
  }

  /**
   * 同步调用带请求头带Get方法
   *
   * @param url
   * @param headers
   * @return
   * @throws IOException
   */
  public Response syncGetWithHeaders(String url, Map<String, String> headers) throws IOException {
    this.request = createHeaderGetRequest(url, headers);
    Call call = this.okHttpClient.newCall(request);
    if (isEnabledRetry()) {
      return retryRequest(call, maxRetryCount);
    }
    return retryRequest(call, 1);
  }

  /**
   * 异步调用无请求头带Get方法
   *
   * @param url
   * @throws IOException
   */
  public void asyncGet(String url) throws IOException {
    this.request = createNoHeaderGetRequest(url);
    this.okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
  }

  /**
   * 异步调用带请求头Get方法
   *
   * @param url
   * @param headers
   * @throws IOException
   */
  public void asyncGetWithHeaders(String url, Map<String, String> headers) throws IOException {
    this.request = createHeaderGetRequest(url, headers);
    this.okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
  }


  /**
   * 同步调用无请求头Post方法
   *
   * @param url
   * @param params
   * @return
   * @throws IOException
   */
  public Response syncPost(String url, Map<String, Object> params) throws IOException {
    return syncPost(url, params);
  }

  public Response syncPost(String url, Object param) throws IOException {
    RequestBody requestBody = OKHttpUtils.parseParam(param);
    this.request = new Request.Builder()
        .post(requestBody)
        .url(url)
        .build();

    Call call = this.okHttpClient.newCall(request);
    return call.execute();
  }

  /**
   * 同步调用带请求头Post参数
   *
   * @param url
   * @param params
   * @param headers
   * @return
   * @throws IOException
   */
//  public Response syncPostWithHeaders(String url, Map<String, Object> params,
//                                      Map<String, String> headers) throws IOException {
//    request = createHeaderPostRequest(url, params, headers);
//    Call call = okHttpClient.newCall(request);
//
//    if (isEnabledRetry()) {
//      return retryRequest(call, maxRetryCount);
//    }
//    return retryRequest(call, 1);
//  }
  public Response syncPostWithHeaders(String url, Object params,
                                      Map<String, String> headers) throws IOException {
    request = createHeaderPostRequest(url, params, headers);
    Call call = okHttpClient.newCall(request);
    log.info("the request : {} ,the params : {}", request, params);
    if (isEnabledRetry()) {
      return retryRequest(call, maxRetryCount);
    }
    return retryRequest(call, 1);
  }

  /**
   * 异步调用不带请求头方法
   *
   * @param url
   * @param params
   * @throws IOException
   */
  public void asyncPost(String url, Map<String, Object> params) throws IOException {
    this.request = createNoHeaderPostRequest(url, params);
    this.okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
  }

  public void asyncPost(String url, Object param) throws IOException {
    this.request = createNoHeaderPostRequest(url, param);
    this.okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
  }

  /**
   * 异步调用带请求头方法
   *
   * @param url
   * @param params
   * @param headers
   * @throws IOException
   */
  public void asyncPostWithHeaders(String url, Map<String, Object> params,
                                   Map<String, String> headers) throws IOException {
    this.request = createHeaderPostRequest(url, params, headers);
    okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
  }

  public void asyncPostWithHeaders(String url, Object param,
                                   Map<String, String> headers) throws IOException {
    this.request = createHeaderPostRequest(url, param, headers);
    okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
  }


  private Response retryRequest(Call call, Integer maxRetryCount) throws IOException {
    Response response = null;
    int retry = 0;
    while (retry++ < maxRetryCount
        && response == null) {
      response = call.execute();
    }
    if (response == null) {
      throw new IOException("target host is not response.");
    }
    return response;
  }

  private static Request createNoHeaderPostRequest(String url, Map<String, Object> params) {
    RequestBody requestBody = OKHttpUtils.parseParams(params);
    return new Request.Builder()
        .url(url)
        .post(requestBody)
        .build();
  }

  private static Request createNoHeaderPostRequest(String url, Object param) {
    RequestBody requestBody = OKHttpUtils.parseParam(param);
    return new Request.Builder()
        .url(url)
        .post(requestBody)
        .build();
  }

  private static Request createHeaderPostRequest(String url, Map<String, Object> params,
                                                 Map<String, String> headers) {
    RequestBody requestBody = OKHttpUtils.parseParams(params);
    return new Request.Builder()
        .post(requestBody)
        .url(url)
        .headers(Headers.of(headers))
        .build();
  }

  private static Request createHeaderPostRequest(String url, Object param,
                                                 Map<String, String> headers) {
    RequestBody requestBody = OKHttpUtils.parseParam(param);
    return new Request.Builder()
        .post(requestBody)
        .url(url)
        .headers(Headers.of(headers))
        .build();
  }

  private static Request createNoHeaderGetRequest(String url) {
    return new Request.Builder()
        .get()
        .url(url)
        .build();
  }

  private static Request createHeaderGetRequest(String url, Map<String, String> headers) {
    return new Request.Builder()
        .get()
        .url(url)
        .headers(Headers.of(headers))
        .build();
  }

  private final class ResponseCallback implements Callback {
    private int retryCount = 0;
    private final OkHttpClient client;
    private final Request request;

    ResponseCallback(OkHttpClient client, Request request) {
      this.client = client;
      this.request = request;
    }

    @Override
    public void onFailure(Call call, IOException e) {
      if (isEnabledRetry() && retryCount++ < maxRetryCount) {
        this.client.newCall(request).enqueue(this);
      }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
      this.retryCount = 0;
    }

  }

}
