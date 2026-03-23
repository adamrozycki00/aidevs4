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
public record Message(
    String role,
    String content,
    @JsonProperty("tool_calls") List<ToolCall> toolCalls,
    @JsonProperty("tool_call_id") String toolCallId
) {

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record ToolCall(String id, FunctionCall function) {
    @JsonProperty("type")
    public String type() {
      return "function";
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record FunctionCall(String name, String arguments) {
  }

  /**
   * Convenience factory for tool result messages in chat-completions:
   * {"role":"tool","tool_call_id":"...","content":"..."}
   */
  public static Message toolResult(String toolCallId, String content) {
    return new Message("tool", content, null, toolCallId);
  }
}
