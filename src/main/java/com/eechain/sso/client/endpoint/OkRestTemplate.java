package com.eechain.sso.client.endpoint;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Callback;
import okhttp3.Call;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OK rest模版，用于跟sso-service 通信，或者其他子系统通信的工具
 * Create by haloo on 2019-04-18
 */
@ConditionalOnClass(OkHttpClient.class)
@Configuration
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
  @Setter
  @Getter
  private int maxRetryCount = 3;
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

  public Response syncGet(String url) throws IOException {

    this.request = new Request.Builder()
        .url(url)
        .build();

    Call call = this.okHttpClient.newCall(request);
    Response response = call.execute();
    return response;
  }

  public void asyncget(String url) throws IOException {

    this.request = new Request.Builder()
        .url(url)
        .build();

    this.okHttpClient.newCall(request).enqueue(new ResponseCallback(okHttpClient, request));
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
