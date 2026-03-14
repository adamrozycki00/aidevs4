package com.ebay.ai.aidevs;

import org.springframework.boot.SpringApplication;

public class TestAidevsApplication {

  public static void main(String[] args) {
    SpringApplication.from(AidevsApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
