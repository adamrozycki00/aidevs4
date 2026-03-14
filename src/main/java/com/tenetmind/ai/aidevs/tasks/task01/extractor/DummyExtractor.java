package com.tenetmind.ai.aidevs.tasks.task01.extractor;

import java.util.List;

import static java.util.Collections.singletonList;

public class DummyExtractor implements TagExtractor {

  @Override
  public List<String> extract(String text) {
    return singletonList("transport");
  }
}
