package com.tenetmind.aidevs.domain.model.task01.extractor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LlmResponse(List<Choice> choices) {

  public String getContent() {
    if (isEmpty(choices)) {
      return "";
    }

    return choices.getFirst().message().content();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Choice(Message message) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Message(String content) {}
}
