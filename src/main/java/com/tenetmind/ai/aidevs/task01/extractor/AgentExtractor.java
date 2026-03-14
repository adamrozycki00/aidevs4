package com.tenetmind.ai.aidevs.task01.extractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.ai.aidevs.task01.Task01;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@RequiredArgsConstructor
public class AgentExtractor implements TagExtractor {

  private static final String OPEN_ROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";
  private static final String OPEN_ROUTER_API_KEY = "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";
  private static final List<String> TAGS = List.of("IT", "transport", "edukacja", "medycyna", "praca z ludźmi", "praca z pojazdami", "praca fizyczna");
  private static final String JSON_SCHEMA_PATH = "tags.json";
  private static final String PROMPT = """
      Assign related tags to the following text:
      
      <query>
        %s
      </query>

      The tags should be related to the content of the text and should be selected from this list: %s.
      The
      """;

  private final RestClient restClient;
  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public List<String> extract(String text) {
    String prompt = PROMPT.formatted(text, TAGS);
    Object responseSchema = readJsonSchema(JSON_SCHEMA_PATH);
    var resp = callOpenRouter(OPEN_ROUTER_URL, OPEN_ROUTER_API_KEY, prompt, responseSchema);
    return resp.tags();
  }

  private TagsResponse callOpenRouter(String openRouterUrl, String apiKey, String prompt, Object jsonSchema) {
    Map<String, Object> requestBody = Map.of(
        "model", "openai/gpt-4.1-mini",
        "messages", List.of(
            Map.of(
                "role", "user",
                "content", prompt
            )
        ),
        "response_format", Map.of(
            "type", "json_schema",
            "json_schema", Map.of(
                "name", "structured_response",
                "strict", true,
                "schema", jsonSchema
            )
        )
    );

    log.info("Calling OpenRouter with request body: {}", requestBody);

    OpenRouterResponse response = restClient.post()
        .uri(openRouterUrl)
        .header(AUTHORIZATION, "Bearer " + apiKey)
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(requestBody)
        .retrieve()
        .body(OpenRouterResponse.class);

    log.info("OpenRouter full response: {}", response);

    if (response == null || response.choices().isEmpty()) {
      throw new IllegalStateException("Empty response from OpenRouter");
    }

    String content = response.choices().getFirst().message().content();
    log.info("Response content: {}", content);

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
