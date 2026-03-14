package com.tenetmind.aidevs.domain.tasks.task01.config;

import com.tenetmind.aidevs.domain.ports.LlmClient;
import com.tenetmind.aidevs.domain.tasks.task01.Task01;
import com.tenetmind.aidevs.domain.tasks.task01.extractor.AgentExtractor;
import com.tenetmind.aidevs.domain.tasks.task01.extractor.DummyExtractor;
import com.tenetmind.aidevs.domain.tasks.task01.extractor.TagExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Task01Config {

  @Autowired
  private LlmClient client;

  @Bean
  @ConditionalOnProperty(name = "agent.extractor.enabled", havingValue = "false", matchIfMissing = true)
  public TagExtractor dummyExtractor() {
    return new DummyExtractor();
  }

  @Bean
  @ConditionalOnProperty(name = "agent.extractor.enabled", havingValue = "true")
  public TagExtractor agentExtractor() {
    return new AgentExtractor(client);
  }

  @Bean
  public Task01 task01(TagExtractor tagExtractor) {
    return new Task01(tagExtractor);
  }
}
