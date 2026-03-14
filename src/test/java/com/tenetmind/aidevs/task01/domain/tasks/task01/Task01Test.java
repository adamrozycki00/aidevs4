package com.tenetmind.aidevs.task01.domain.tasks.task01;

import com.tenetmind.aidevs.domain.tasks.task01.Task01;
import com.tenetmind.aidevs.domain.tasks.task01.extractor.DummyExtractor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
class Task01Test {

  @Test
  void shouldGetCorrectAnswerForDummyExtractor() {
    // given Task 01 with DummyExtractor, which always returns "transport" tag for any input
    Task01 task01 = new Task01(new DummyExtractor());

    // when
    var answer = (List<Object>) task01.solve();

    // then the answer should contain 31 people that fulfill all criteria ignoring the "transport" tag
    assertThat(answer).hasSize(31);
  }
}
