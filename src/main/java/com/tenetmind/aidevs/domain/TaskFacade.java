package com.tenetmind.aidevs.domain;

import com.tenetmind.aidevs.domain.model.Tasks;
import com.tenetmind.aidevs.domain.ports.AnswerVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Arrays.stream;

@Slf4j
@RequiredArgsConstructor
public class TaskFacade {

  private final Tasks tasks;
  private final AnswerVerifier answerVerifier;

  public void solveByName(Tasks.TaskName taskName) {
    var task = tasks.getByName(taskName);
    task.solve();

    log.info("Task solved:\n{}", task);
  }

  public void solveAll() {
    var allTaskNames = Tasks.TaskName.values();
    stream(allTaskNames)
        .forEach(this::solveByName);

    log.info("All tasks solved");
  }

  public AnswerVerifier.Response verifyAnswerByName(Tasks.TaskName taskName) {
    var task = tasks.getByName(taskName);
    return answerVerifier.verify(task);
  }
}
