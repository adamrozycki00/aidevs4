package com.tenetmind.aidevs.infra.config;

import com.tenetmind.aidevs.domain.TaskFacade;
import com.tenetmind.aidevs.domain.model.Task;
import com.tenetmind.aidevs.domain.model.Tasks;
import com.tenetmind.aidevs.domain.model.task02.tools.ToolFactory;
import com.tenetmind.aidevs.domain.model.task02.tools.Toolbox;
import com.tenetmind.aidevs.domain.ports.out.ApiCaller;
import com.tenetmind.aidevs.domain.ports.out.AnswerVerifier;
import com.tenetmind.aidevs.domain.ports.out.LlmClient;
import com.tenetmind.aidevs.infra.client.brave.BraveClient;
import com.tenetmind.aidevs.infra.client.openrouter.OpenRouterClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;

@Configuration
@Import(TaskBeans.class)
public class AppConfig {

  @Autowired
  ApplicationContext ctx;

  @Bean
  public Tasks tasks() {
    var allTasks = new ArrayList<>(ctx.getBeansOfType(Task.class).values());
    return new Tasks(allTasks);
  }

  @Bean
  public TaskFacade taskFacade() {
    return new TaskFacade(tasks(), answerVerifier());
  }

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
  public AnswerVerifier answerVerifier() {
    return new BraveClient(restClient(), secrets().getBrave());
  }

  @Bean
  public ApiCaller apiCaller() {
    return new BraveClient(restClient(), secrets().getBrave());
  }

  @Bean
  public ToolFactory toolFactory() {
    return new ToolFactory(apiCaller());
  }

  @Bean
  public Toolbox tools() {
    return new Toolbox(toolFactory());
  }
}
