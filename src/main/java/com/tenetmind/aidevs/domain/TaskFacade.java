package com.tenetmind.aidevs.domain;

import com.tenetmind.aidevs.domain.ports.AnswerSender;
import com.tenetmind.aidevs.domain.tasks.Task;
import com.tenetmind.aidevs.domain.tasks.Tasks;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
public class TaskFacade {

  private final AnswerSender answerSender;
  private final Tasks tasks;

  public List<AnswerSender.Response> solveByName(List<Tasks.TaskName> taskNames) {
    return taskNames.stream()
        .map(tasks::getByName)
        .map(this::solveAndSendAnswer)
        .toList();
  }

  public List<AnswerSender.Response> solveAll() {
    return solveByName(asList(Tasks.TaskName.values()));
  }

  private AnswerSender.Response solveAndSendAnswer(Task task) {
    var answer = task.solve();
    return answerSender.send(task, answer);
  }
}
