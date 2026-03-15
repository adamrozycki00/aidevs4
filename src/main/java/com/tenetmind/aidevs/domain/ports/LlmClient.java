package com.tenetmind.aidevs.domain.ports;

import com.tenetmind.aidevs.domain.model.task01.extractor.LlmResponse;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public interface LlmClient {

  @NonNull Map<String, Object> buildRequest(String prompt, Object jsonSchema);

  @NonNull LlmResponse call(Map<String, Object> requestBody);
}
