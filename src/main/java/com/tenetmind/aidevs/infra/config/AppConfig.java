package com.tenetmind.aidevs.infra.config;

import com.tenetmind.aidevs.domain.ports.AnswerSender;
import com.tenetmind.aidevs.domain.ports.LlmClient;
import com.tenetmind.aidevs.infra.client.brave.BraveClient;
import com.tenetmind.aidevs.infra.client.openrouter.OpenRouterClient;
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
  public LlmClient llmClient() {
    return new OpenRouterClient(restClient(), secrets().getOpenRouter());
  }

  @Bean
  public AnswerSender answerSender() {
    return new BraveClient(restClient(), secrets().getBrave());
  }
}
