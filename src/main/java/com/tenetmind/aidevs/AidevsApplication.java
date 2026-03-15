package com.tenetmind.aidevs;

import com.tenetmind.aidevs.domain.TaskFacade;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.List;

import static com.tenetmind.aidevs.domain.tasks.Tasks.TaskName.PEOPLE;
import static org.springframework.boot.WebApplicationType.NONE;

@SpringBootApplication
public class AidevsApplication implements ApplicationRunner {

  @Autowired
  private TaskFacade taskFacade;

  static void main(String[] args) {
    var ctx = new SpringApplicationBuilder(AidevsApplication.class).web(NONE).run(args);
    int exitCode = SpringApplication.exit(ctx);
    System.exit(exitCode);
  }

  @Override
  public void run(@NonNull ApplicationArguments args) {
    var responses = taskFacade.solveByName(List.of(PEOPLE));
    responses.forEach(IO::println);
  }
}
