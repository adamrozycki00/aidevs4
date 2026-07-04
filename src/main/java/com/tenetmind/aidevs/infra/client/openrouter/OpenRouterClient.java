package com.tenetmind.aidevs.infra.client.openrouter;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenetmind.aidevs.domain.model.llmchat.ChatCompletionResponse;
import com.tenetmind.aidevs.domain.ports.out.LlmClient;
import com.tenetmind.aidevs.infra.config.Secrets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class OpenRouterClient implements LlmClient {

  private static final String OPEN_ROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

  private final RestClient restClient;
  private final Secrets.OpenRouter secrets;
  private final ObjectMapper objectMapper = new ObjectMapper();

  {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setDefaultPropertyInclusion(
        JsonInclude.Value.construct(JsonInclude.Include.NON_NULL, JsonInclude.Include.NON_NULL)
    );
  }

  @Override
  public @NonNull ChatCompletionResponse call(Map<String, Object> requestBody) {
    log.info("Calling OpenRouter API with request body: {}", requestBody);
    var resp = restClient.post()
        .uri(OPEN_ROUTER_URL)
        .header(AUTHORIZATION, "Bearer " + secrets.getApiKey())
        .contentType(APPLICATION_JSON)
        .accept(APPLICATION_JSON)
        .body(requestBody)
        .retrieve()
        .body(ChatCompletionResponse.class);
    
    log.info("OpenRouter API response: {}", resp);

    if (isNull(resp)) {
      throw new IllegalStateException("OpenRouter response is null");
    }

    return resp;
  }
}
