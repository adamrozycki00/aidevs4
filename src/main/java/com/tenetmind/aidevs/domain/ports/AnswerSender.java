package com.tenetmind.aidevs.domain.ports;

import com.tenetmind.aidevs.domain.tasks.Task;
import com.tenetmind.aidevs.domain.tasks.Tasks;

public interface AnswerSender {

  AnswerSender.Response send(Task task, Object answer);

  record Response(Tasks.TaskName taskName, Object response) {
  }
}
