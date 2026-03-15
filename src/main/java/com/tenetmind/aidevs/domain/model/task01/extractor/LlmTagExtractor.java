package com.tenetmind.aidevs.domain.model.task01.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.task01.Task01;
import com.tenetmind.aidevs.domain.ports.LlmClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LlmTagExtractor implements TagExtractor {

  private static final List<String> TAGS = List.of("IT", "transport", "edukacja", "medycyna", "praca z ludźmi", "praca z pojazdami", "praca fizyczna");
  private static final String JSON_SCHEMA_PATH = "tags.json";
  private static final String PROMPT = """
      Assign related tags to the following text:
      
      <query>
        %s
      </query>
      
      The tags should be related to the content of the text and should be selected from this list: %s.
      """;

  private final LlmClient client;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<String> extract(String text) {
    String prompt = PROMPT.formatted(text, TAGS);
    var responseSchema = readJsonSchema();
    var resp = callLlm(prompt, responseSchema);
    return resp.tags();
  }

  private TagsResponse callLlm(String prompt, Object jsonSchema) {
    var requestBody = client.buildRequest(prompt, jsonSchema);
    log.info("Calling OpenRouter with request body: {}", requestBody);

    LlmResponse response = client.call(requestBody);
    log.info("OpenRouter full response: {}", response);

    String content = response.getContent();
    log.info("Response content: {}", content);

    return toTagsResponse(content);
  }

  private TagsResponse toTagsResponse(String content) {
    try {
      return mapper.readValue(content, TagsResponse.class);
    } catch (Exception e) {
      throw new IllegalStateException(
          "Model output did not contain a valid " + TagsResponse.class.getSimpleName() + ": " + content, e);
    }
  }

  private Object readJsonSchema() {
    URL resource = Task01.class.getClassLoader().getResource(JSON_SCHEMA_PATH);
    if (resource == null) {
      throw new IllegalStateException("search.json resource not found on classpath");
    }

    try {
      String readString = Files.readString(Path.of(resource.toURI()));

      try {
        return mapper.readValue(readString, Object.class);
      } catch (Exception e) {
        throw new IllegalStateException("Invalid JSON schema: " + readString, e);
      }

    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Unable to read search.json", e);
    }
  }
}
