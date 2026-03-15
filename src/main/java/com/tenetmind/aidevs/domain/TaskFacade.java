package com.tenetmind.aidevs.domain;

import com.tenetmind.aidevs.domain.model.Tasks;
import com.tenetmind.aidevs.domain.ports.AnswerSender;
import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public class TaskFacade {

  private final Tasks tasks;
  private final AnswerSender answerSender;

  public void solveAll() {
    var allTaskNames = Tasks.TaskName.values();
    stream(allTaskNames)
        .forEach(this::solveByName);
  }

  public void solveByName(Tasks.TaskName taskName) {
    var task = tasks.getByName(taskName);
    task.solve();
  }

  public AnswerSender.Response sendAnswerByName(Tasks.TaskName taskName) {
    var task = tasks.getByName(taskName);
    return answerSender.send(task);
  }
}
