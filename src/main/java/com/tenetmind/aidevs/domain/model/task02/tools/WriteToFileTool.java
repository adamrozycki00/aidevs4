package com.tenetmind.aidevs.domain.model.task02.tools;

import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionRequest;
import com.tenetmind.aidevs.domain.model.llmchat.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.tenetmind.aidevs.domain.model.task02.tools.Toolbox.parseArguments;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;

@Slf4j
public class WriteToFileTool implements Toolbox.Tool {

  private static final String BASE_DIR = "/Users/arozycki/Dev/aidevs/task2";

  @Override
  public String execute(Message.ToolCall toolCall) {
    log.info("Executing WriteToFileTool for toolCall {}", toolCall);
    Map<String, Object> args = parseArguments(toolCall.function().arguments());

    Object fileNameRaw = args.get("file_name");
    if (!(fileNameRaw instanceof String fileName) || fileName.isBlank()) {
      throw new IllegalArgumentException("Missing/invalid argument 'file_name'");
    }

    Object contentRaw = args.get("content");
    if (!(contentRaw instanceof String content)) {
      throw new IllegalArgumentException("Missing/invalid argument 'content'");
    }

    writeToFile(fileName, content);
    return "OK";
  }

  public static ChatCompletionRequest.Tool writeToFile() {
    var function = ChatCompletionRequest.Function.builder()
        .name("write_to_file")
        .description("Writes a UTF-8 string into a file")
        .parameters(Map.of(
            "type", "object",
            "properties", Map.of(
                "file_name", Map.of(
                    "type", "string",
                    "description", "Name of the file to write"
                ),
                "content", Map.of(
                    "type", "string",
                    "description", "String content to write"
                )
            ),
            "required", List.of("file_name", "content"),
            "additionalProperties", false
        ))
        .build();
    return new ChatCompletionRequest.Tool("function", function);
  }

  private static void writeToFile(String fileName, String content) {
    writeToFileInDir(BASE_DIR, fileName, content);
  }

  static void writeToFileInDir(String baseDir, String fileName, String content) {
    if (isNull(fileName) || fileName.isBlank()) {
      throw new IllegalArgumentException("fileName must not be null/blank");
    }
    if (isNull(baseDir) || baseDir.isBlank()) {
      throw new IllegalArgumentException("baseDir must not be null/blank");
    }
    if (isNull(content)) {
      throw new IllegalArgumentException("content must not be null");
    }

    Path base = Paths.get(baseDir).toAbsolutePath().normalize();

    // Treat a leading slash as an attempt to use an absolute path; resolve it as relative by stripping it.
    String normalizedInput = fileName.startsWith("/") ? fileName.substring(1) : fileName;
    Path resolved = base.resolve(normalizedInput).normalize();

    if (!resolved.startsWith(base)) {
      throw new IllegalArgumentException("Access denied (path traversal): " + fileName);
    }

    try {
      Path parent = resolved.getParent();
      if (!isNull(parent)) {
        Files.createDirectories(parent);
      }

      Files.writeString(resolved, content, UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Error writing file: " + resolved, e);
    }
  }
}

