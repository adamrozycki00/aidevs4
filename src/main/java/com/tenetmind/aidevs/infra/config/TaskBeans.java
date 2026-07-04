package com.tenetmind.aidevs.infra.config;

import com.tenetmind.aidevs.domain.model.task02.Task02;
import com.tenetmind.aidevs.domain.model.task02.tools.Toolbox;
import com.tenetmind.aidevs.domain.model.task03.task.Task03;
import com.tenetmind.aidevs.domain.ports.out.ApiCaller;
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

  @Autowired
  private ApiCaller apiCaller;

  @Bean
  public Task02 task02() {
    return new Task02(tools, llmClient, 100);
  }

  @Bean
  public com.tenetmind.aidevs.domain.model.task03.tools.Toolbox task03Toolbox() {
    return new com.tenetmind.aidevs.domain.model.task03.tools.Toolbox(apiCaller);
  }

  @Bean
  public Task03 task03() {
    return new Task03(task03Toolbox(), llmClient);
  }
}
