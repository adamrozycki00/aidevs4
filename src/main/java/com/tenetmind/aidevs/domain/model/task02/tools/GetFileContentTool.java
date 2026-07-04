package com.tenetmind.aidevs.domain.model.task02.tools;

import com.tenetmind.aidevs.domain.model.Toolbox;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.tenetmind.aidevs.domain.model.Toolbox.parseArguments;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.*;
import static java.util.Objects.isNull;

@Slf4j
public class GetFileContentTool implements Toolbox.Tool {
  
  private static final String BASE_DIR = "/Users/adam.rozycki/Dev/aidevs/task2";

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing GetFileContentTool for toolCall {}", toolCall);
    var args = parseArguments(toolCall.function().arguments());
    Object fileNameRaw = args.get("file_name");

    if (!(fileNameRaw instanceof String fileName) || fileName.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'file_name'");
    }

    return getFileContentTool(fileName);
  }

  public static ChatCompletionRequest.Tool getFileContentTool() {
    var function = ChatCompletionRequest.Function.builder()
        .name("get_file_content")
        .description("Fetches the content of a file given its name, resolved against a configured base directory")
        .parameters(Map.of(
            "type", "object",
            "properties", Map.of(
                "file_name", Map.of(
                    "type", "string",
                    "description", "Path or name of the file to read (relative to the base directory)"
                )
            ),
            "required", List.of("file_name"),
            "additionalProperties", false
        ))
        .build();
    return new ChatCompletionRequest.Tool("function", function);
  }

  private static String getFileContentTool(String fileName) {
    return getFileContentFromDir(BASE_DIR, fileName);
  }

  static String getFileContentFromDir(String baseDir, String fileName) {
    if (isNull(fileName) || fileName.isBlank()) {
      throw new IllegalArgumentException("fileName must not be null/blank");
    }

    if (isNull(baseDir) || baseDir.isBlank()) {
      throw new IllegalArgumentException("baseDir must not be null/blank");
    }

    Path base = Paths.get(baseDir).toAbsolutePath().normalize();

    String normalizedInput = fileName.startsWith("/") ? fileName.substring(1) : fileName;
    Path resolved = base.resolve(normalizedInput).normalize();

    if (!resolved.startsWith(base)) {
      throw new IllegalArgumentException("Access denied (path traversal): " + fileName);
    }

    if (!exists(resolved)) {
      throw new IllegalStateException("File not found: " + resolved);
    }

    if (isDirectory(resolved)) {
      throw new IllegalStateException("Expected a file but got a directory: " + resolved);
    }

    try {
      return readString(resolved, UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Error reading file: " + resolved, e);
    }
  }
}

