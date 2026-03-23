package com.tenetmind.aidevs;

import com.tenetmind.aidevs.domain.TaskFacade;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static com.tenetmind.aidevs.domain.model.Tasks.TaskName.FIND_HIM;
import static org.springframework.boot.WebApplicationType.NONE;

@Slf4j
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
    taskFacade.solveByName(FIND_HIM);
    var response = taskFacade.verifyAnswerByName(FIND_HIM);

    log.info("Verifier response:\n{}", response);
  }
}
