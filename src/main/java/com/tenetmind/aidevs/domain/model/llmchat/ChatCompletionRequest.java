package com.tenetmind.aidevs.domain.model.llmchat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Builder
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatCompletionRequest(
    String model,
    List<Message> messages,
    Double temperature,
    List<Tool> tools,
    @JsonProperty("response_format") ResponseFormat responseFormat
) {

  @JsonInclude(NON_NULL)
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Tool(String type, Function function) {
  }

  @Builder
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Function(String name, String description, Object parameters) {
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ResponseFormat(@JsonProperty("json_schema") JsonSchema jsonSchema) {
    @JsonProperty("type")
    public String type() {
      return "json_schema";
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record JsonSchema(String name, Boolean strict, Object schema) {
  }
}


