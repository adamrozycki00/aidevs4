package com.tenetmind.aidevs.infra.config;

import com.tenetmind.aidevs.domain.model.task01.Task01;
import com.tenetmind.aidevs.domain.model.task01.extractor.LlmTagExtractor;
import com.tenetmind.aidevs.domain.model.task02.Task02;
import com.tenetmind.aidevs.domain.model.Toolbox;
import com.tenetmind.aidevs.domain.model.task03.Task03;
import com.tenetmind.aidevs.domain.ports.out.LlmClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskBeans {

  @Autowired
  private Toolbox tools;

  @Autowired
  private LlmClient llmClient;

  @Bean
  public Task01 task01() {
    var tagExtractor = new LlmTagExtractor(llmClient);
    return new Task01(tagExtractor);
  }

  @Bean
  public Task02 task02() {
    return new Task02(tools, llmClient, 40);
  }

  @Bean
  public Task03 task03() {
    return new Task03(tools, llmClient);
  }
}
