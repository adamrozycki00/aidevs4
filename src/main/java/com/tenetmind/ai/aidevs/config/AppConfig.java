package com.tenetmind.ai.aidevs.config;

import com.tenetmind.ai.aidevs.sender.AnswerSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

  @Bean
  public AnswerSender responseSender(RestClient restClient) {
    return new AnswerSender(restClient);
  }
}
