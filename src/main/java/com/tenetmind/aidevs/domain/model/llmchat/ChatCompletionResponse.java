package com.tenetmind.aidevs.domain.model.llmchat;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatCompletionResponse(List<Choice> choices) {

  public Message getFirstMessage() {
    if (isEmpty(choices)) {
      return null;
    }

    return choices.getFirst().message();
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Choice(Message message) {}
}
