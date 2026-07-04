package com.tenetmind.aidevs.domain.model.task03.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import com.tenetmind.aidevs.domain.ports.out.ApiCaller;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Toolbox {

  private final Map<String, Function<Message.ToolCall, String>> executors = new HashMap<>();

  public Toolbox(ApiCaller apiCaller) {
    var checkPackageTool = new CheckPackageTool(apiCaller);
    var redirectPackageTool = new RedirectPackageTool(apiCaller);
    executors.put("check_package", checkPackageTool::execute);
    executors.put("redirect_package", redirectPackageTool::execute);
  }

  /**
   * Executes a single tool call and returns the tool result content.
   * <p>
   * The caller is expected to wrap this string into a tool-result message:
   * {@code Message.toolResult(toolCall.id(), result)}.
   */
  public String execute(Message.ToolCall toolCall) {
    if (isNull(toolCall) || isNull(toolCall.function()) || isNull(toolCall.function().name())) {
      throw new IllegalArgumentException("toolCall/function/name must not be null");
    }

    var executor = executors.get(toolCall.function().name());

    if (isNull(executor)) {
      throw new IllegalArgumentException("Unknown tool: " + toolCall.function().name());
    }

    return executor.apply(toolCall);
  }

  public static Map<String, Object> parseArguments(String argumentsJson) {
    if (isBlank(argumentsJson)) {
      return Map.of();
    }

    try {
      return new ObjectMapper().readValue(argumentsJson, new TypeReference<>() {
      });
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid tool arguments JSON: " + argumentsJson, e);
    }
  }

  @FunctionalInterface
  public interface Tool {
    String execute(Message.ToolCall toolCall);
  }
}
