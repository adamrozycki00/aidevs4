package com.tenetmind.aidevs.domain.ports.out;

import com.tenetmind.aidevs.domain.model.Task;
import com.tenetmind.aidevs.domain.model.Tasks;

public interface AnswerVerifier {

  AnswerVerifier.Response verify(Task task);

  record Response(Tasks.TaskName taskName, Object response) {
  }
}
