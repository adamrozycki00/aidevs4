package com.tenetmind.aidevs.domain.tasks.task01.extractor;

import java.util.List;

import static java.util.Collections.singletonList;

public class DummyTagExtractor implements TagExtractor {

  @Override
  public List<String> extract(String text) {
    return singletonList("transport");
  }
}
