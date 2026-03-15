package com.tenetmind.aidevs.infra.config;

import com.tenetmind.aidevs.domain.ports.LlmClient;
import com.tenetmind.aidevs.domain.tasks.task01.Task01;
import com.tenetmind.aidevs.domain.tasks.task01.extractor.AgenticTagExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskBeans {

  @Autowired
  private LlmClient client;

  @Bean
  public Task01 task01() {
    var tagExtractor = new AgenticTagExtractor(client);
    return new Task01(tagExtractor);
  }
}
