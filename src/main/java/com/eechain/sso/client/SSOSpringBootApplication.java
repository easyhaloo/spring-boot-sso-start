package com.eechain.sso.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Create by haloo on 2019-04-19
 */
@SpringBootApplication
public class SSOSpringBootApplication implements CommandLineRunner {


  @Autowired
  private ClientProperties clientProperties;

  public static void main(String[] args) {
    SpringApplication.run(SSOSpringBootApplication.class);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println(clientProperties);
  }
}
