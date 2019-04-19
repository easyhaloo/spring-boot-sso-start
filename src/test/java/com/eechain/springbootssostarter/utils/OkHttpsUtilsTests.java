package com.eechain.springbootssostarter.utils;

import com.eechain.sso.client.utils.OKHttpUtils;
import lombok.Data;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Call;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by haloo on 2019-04-18
 */
public class OkHttpsUtilsTests {


  @Test
  public void testParseParamsWithMap() {

    Map<String, Object> map = new HashMap<>();
    map.put("xx", "xx");
    map.put("x", 1);
    map.put("xd", new Date());
    RequestBody requestBody = OKHttpUtils.parseParams(map);
    Assert.assertNotNull(requestBody);
  }

  @Test
  public void testParseParams() {

    TestResponse testResponse = new TestResponse();
    testResponse.setEmail("xxx");
    testResponse.setName("ccc");
    testResponse.setPhone(null);
    RequestBody requestBody = OKHttpUtils.parseParam(testResponse);
    Assert.assertNotNull(requestBody);
  }


  @Test
  public void testParseResponse() throws IOException {

    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    Request request = new Request.Builder()
        .get()
        .url("https://raw.github.com/square/okhttp/master/README.md")
        .build();

    Call call = okHttpClient.newCall(request);
    Response response = call.execute();
    String s = OKHttpUtils.parseResponse(response);
    Assert.assertNotNull(s);
  }


  @Test
  public void testParseConvertToObject() throws IOException {
    //http://unionsug.baidu.com/su?wd=js&cb=baiduSU
    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
    Request request = new Request.Builder()
        .get()
        .url("https://api.androidhive.info/volley/person_object.json")
        .build();

    Call call = okHttpClient.newCall(request);
    Response response = call.execute();

    TestResponse testResponse = OKHttpUtils.convertToObject(response, TestResponse.class);
    System.out.println(testResponse);
    Assert.assertNotNull(testResponse);
  }


}


@Data
class TestResponse {
  private String name;
  private String email;
  private Phone phone;


  @Data
  public class Phone {
    private String home;
    private String mobile;

  }

}
