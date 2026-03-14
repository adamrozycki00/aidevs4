package com.tenetmind.ai.aidevs.client;

import com.tenetmind.ai.aidevs.config.Secrets;
import com.tenetmind.ai.aidevs.task01.extractor.OpenRouterResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
public class OpenRouterClient {

  private static final String OPEN_ROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

  private final RestClient restClient;
  private final Secrets.OpenRouter secrets;

  public @NonNull Map<String, Object> buildRequest(String prompt, Object jsonSchema) {
    return Map.of(
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
  }

  public @Nullable OpenRouterResponse call(Map<String, Object> requestBody) {
    return restClient.post()
        .uri(OPEN_ROUTER_URL)
        .header(AUTHORIZATION, "Bearer " + secrets.getApiKey())
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(requestBody)
        .retrieve()
        .body(OpenRouterResponse.class);
  }
}
