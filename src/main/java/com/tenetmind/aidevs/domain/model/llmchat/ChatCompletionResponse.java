package com.tenetmind.aidevs.domain.model.llmchat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import static java.util.Objects.isNull;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatCompletionResponse(List<Choice> choices) {

  public String getContent() {
    if (isEmpty(choices)) {
      return "";
    }

    String content = choices.getFirst().message().content();
    return isNull(content) ? "" : content;
  }

  public Message getFirstMessage() {
    if (isEmpty(choices)) {
      return null;
    }

    return choices.getFirst().message();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Choice(Message message) {}
}
