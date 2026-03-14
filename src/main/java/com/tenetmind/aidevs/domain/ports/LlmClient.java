package com.tenetmind.aidevs.domain.ports;

import com.tenetmind.aidevs.domain.tasks.task01.extractor.OpenRouterResponse;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public interface LlmClient {

  @NonNull Map<String, Object> buildRequest(String prompt, Object jsonSchema);

  OpenRouterResponse call(Map<String, Object> requestBody);
}
