package com.tenetmind.ai.aidevs.task01;

import com.tenetmind.aidevs.domain.tasks.task01.Task01;
import com.tenetmind.aidevs.domain.tasks.task01.extractor.DummyExtractor;
import org.junit.jupiter.api.Test;

class Task01Test {

  @Test
  void getAnswer() {
    Task01 task01 = new Task01(new DummyExtractor());
    var res = task01.getAnswer();
    IO.println(res);
  }
}
