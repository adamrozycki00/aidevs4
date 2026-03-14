package com.tenetmind.aidevs.infra.config;

import com.tenetmind.aidevs.infra.client.openrouter.OpenRouterClient;
import com.tenetmind.aidevs.infra.client.brave.AnswerSender;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

  @Bean
  @ConfigurationProperties("secrets")
  public Secrets secrets() {
    return new Secrets();
  }

  @Bean
  public RestClient restClient() {
    return RestClient.create();
  }

  @Bean
  public OpenRouterClient openRouterClient() {
    return new OpenRouterClient(restClient(), secrets().getOpenRouter());
  }

  @Bean
  public AnswerSender responseSender() {
    return new AnswerSender(restClient(), secrets().getBrave());
  }
}
