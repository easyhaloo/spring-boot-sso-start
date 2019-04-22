package com.eechain.sso.client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by haloo on 2019-04-22
 */
@RestController
public class TestController {
  @GetMapping
  public String test() {
    return "test";
  }
}
