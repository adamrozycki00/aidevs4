package com.tenetmind.aidevs;

import static com.tenetmind.aidevs.domain.model.Tasks.TaskName.FIND_HIM;

import com.tenetmind.aidevs.domain.TaskFacade;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class AidevsApplication implements ApplicationRunner {

  @Autowired
  private ApplicationContext ctx;

  @Autowired
  private TaskFacade taskFacade;

  @Value("${task-mode:false}")
  private boolean taskMode;

  private void runTasks() {
    taskFacade.solveByName(FIND_HIM);
    var response = taskFacade.verifyAnswerByName(FIND_HIM);
    log.info("Verifier response:\n{}", response);

    int exitCode = SpringApplication.exit(ctx);
    System.exit(exitCode);
  }

  public static void main(String[] args) {
    SpringApplication.run(AidevsApplication.class, args);
  }

  @Override
  public void run(@NonNull ApplicationArguments args) {
    if (taskMode) {
      runTasks();
    }
  }
}
