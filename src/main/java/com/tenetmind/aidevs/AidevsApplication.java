package com.tenetmind.aidevs;

import com.tenetmind.aidevs.domain.tasks.task01.Task01;
import com.tenetmind.aidevs.infra.client.brave.AnswerSender;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.springframework.boot.WebApplicationType.NONE;

@SpringBootApplication
public class AidevsApplication implements ApplicationRunner {

  @Autowired
  private Task01 task;

  @Autowired
  private AnswerSender sender;

  public static void main(String[] args) {
    var ctx = new SpringApplicationBuilder(AidevsApplication.class).web(NONE).run(args);
    int exitCode = SpringApplication.exit(ctx);
    System.exit(exitCode);
  }

  @Override
  public void run(@NonNull ApplicationArguments args) {
    var response = sender.send(task.getName(), task.getAnswer());
    IO.println(response);
  }
}
