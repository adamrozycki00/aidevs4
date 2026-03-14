package com.tenetmind.ai.aidevs.task01.config;

import com.tenetmind.ai.aidevs.task01.Task01;
import com.tenetmind.ai.aidevs.task01.extractor.AgentExtractor;
import com.tenetmind.ai.aidevs.task01.extractor.DummyExtractor;
import com.tenetmind.ai.aidevs.task01.extractor.TagExtractor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Task01Config {

  @Bean
  @ConditionalOnProperty(name = "agent.extractor.enabled", havingValue = "false", matchIfMissing = true)
  public TagExtractor dummyExtractor() {
    return new DummyExtractor();
  }

  @Bean
  @ConditionalOnProperty(name = "agent.extractor.enabled", havingValue = "true")
  public TagExtractor agentExtractor() {
    return new AgentExtractor(restClient());
  }

  @Bean
  public Task01 task01(TagExtractor tagExtractor) {
    return new Task01(tagExtractor);
  }

  @Bean
  public RestClient restClient() {
    return RestClient.create();
  }
}
