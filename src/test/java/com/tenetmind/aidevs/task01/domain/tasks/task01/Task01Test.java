package com.tenetmind.aidevs.task01.domain.tasks.task01;

import com.tenetmind.aidevs.domain.model.task01.Task01;
import com.tenetmind.aidevs.domain.model.task01.extractor.DummyTagExtractor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
class Task01Test {

  @Test
  void shouldGetCorrectAnswerForDummyExtractor() {
    // given a DummyTagExtractor that returns the "transport" tag for any input
    var dummyExtractor = new DummyTagExtractor();
    // and a Task01 instance that selects people fulfilling certain criteria from a file,
    // which contains 31 people that meet the criteria when the "transport" tag is ignored
    Task01 task01 = new Task01(dummyExtractor);
    int expectedPeopleCount = 31;

    // when
    task01.solve();

    // then the answer should contain all people that meet the criteria when the "transport" tag is ignored
    var answer = (List<Object>) task01.getAnswer();
    assertThat(answer).hasSize(expectedPeopleCount);
  }
}
