package com.tenetmind.aidevs.domain.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.llmchat.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class Toolbox {

  private final Map<String, Function<Message.ToolCall, String>> executors = new HashMap<>();

  public Toolbox(ToolFactory factory) {
    // task02
    executors.put("get_file_content", factory.createGetFileContentTool()::execute);
    executors.put("write_to_file", factory.createWriteToFileTool()::execute);
    executors.put("get_person_locations", factory.createGetPersonLocationsTool()::execute);
    executors.put("calculate_distance", factory.createCalculateDistanceTool()::execute);
    executors.put("get_person_access_level", factory.createGetPersonAccessLevel()::execute);

    // task03
    executors.put("check_package", factory.checkPackageTool()::execute);
    executors.put("redirect_package", factory.redirectPackageTool()::execute);
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
