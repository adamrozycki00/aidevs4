package com.tenetmind.ai.aidevs.task01.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.ai.aidevs.client.OpenRouterClient;
import com.tenetmind.ai.aidevs.task01.Task01;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
public class AgentExtractor implements TagExtractor {

  private static final List<String> TAGS = List.of("IT", "transport", "edukacja", "medycyna", "praca z ludźmi", "praca z pojazdami", "praca fizyczna");
  private static final String JSON_SCHEMA_PATH = "tags.json";
  private static final String PROMPT = """
      Assign related tags to the following text:
      
      <query>
        %s
      </query>
      
      The tags should be related to the content of the text and should be selected from this list: %s.
      """;

  private final OpenRouterClient client;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<String> extract(String text) {
    String prompt = PROMPT.formatted(text, TAGS);
    Object responseSchema = readJsonSchema(JSON_SCHEMA_PATH);
    var resp = callOpenRouter(prompt, responseSchema);
    return resp.tags();
  }

  private TagsResponse callOpenRouter(String prompt, Object jsonSchema) {
    Map<String, Object> requestBody = client.buildRequest(prompt, jsonSchema);
    log.info("Calling OpenRouter with request body: {}", requestBody);

    OpenRouterResponse response = client.call(requestBody);
    log.info("OpenRouter full response: {}", response);

    if (isNull(response) || response.choices().isEmpty()) {
      throw new IllegalStateException("Empty response from OpenRouter");
    }

    String content = response.choices().getFirst().message().content();
    log.info("Response content: {}", content);

    return mapToTagResponse(content);
  }

  private TagsResponse mapToTagResponse(String content) {
    try {
      return mapper.readValue(content, TagsResponse.class);
    } catch (Exception e) {
      throw new IllegalStateException("Model output is not valid TagsResponse JSON: " + content, e);
    }
  }

  private static Object readJsonSchema(String path) {
    URL resource = Task01.class.getClassLoader().getResource(path);

    if (resource == null) {
      throw new IllegalStateException("search.json resource not found on classpath");
    }

    try {
      String readString = Files.readString(Path.of(resource.toURI()));

      try {
        return new ObjectMapper().readValue(readString, Object.class);
      } catch (Exception e) {
        throw new IllegalStateException("Invalid JSON schema: " + readString, e);
      }

    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Unable to read search.json", e);
    }
  }
}
